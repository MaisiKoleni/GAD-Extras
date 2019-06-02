import static java.util.stream.Collectors.*;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * Öffentliche Testfälle von Christian Femers. Siehe
 * https://github.com/MaisiKoleni/GAD-Extras
 * 
 * Bitte nicht mit abgeben. (geht sonst nicht)
 * 
 * @version 1.0
 * 
 * @author Christian Femers (IN.TUM)
 *
 */
public class DoubleHashTest {

	private static final String VERSION = "1.0";

	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(0);

	static {
		tryVersionCheck();

		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		// Test 1, 2, 3
		testInt();
		// Test 4, 5, 6
		testString();
	}

	/**
	 * Test int hashing
	 */
	public static void testInt() {
		int m = 17;
		int n = m * 50;
		double dev = n / m / 2.0;

		DoubleHashInt dhi = new DoubleHashInt(m);
		List<Integer> keys = IntStream.range(0, n).boxed().collect(toList());

		printTestHeadline("TEST INT HASH"); // Test 1

		var hMap = keys.stream().collect(groupingBy(dhi::hash));

		// 1 - checking consistency
		assertEquals(keys.stream().collect(groupingBy(dhi::hash)), hMap, "hash() %s consistent");
		// 2 - checking collisions of hash
		assertEquals(getMedian(hMap.values().stream().map(List::size).collect(toList())), n / m, dev);
		// 3 - checking bounds of the hash values
		checkBounds(hMap.keySet(), 0, m - 1);

		// GRAPH
		printDistribution(hMap, m, false);

		printTestHeadline("TEST INT HASH_TICK"); // Test 2

		var hTickMap = keys.stream().collect(groupingBy(dhi::hashTick));

		// 4 - checking consistency
		assertEquals(keys.stream().collect(groupingBy(dhi::hashTick)), hTickMap, "hashTick() %s consistent");
		// 5 - checking bounds of hashTick values, must not be 0 (-> would eliminate i)
		checkBounds(hTickMap.keySet(), 1, m - 1);
		// 6 - checking collisions of hashTick (zero excluded)
		assertEquals(getMedian(hTickMap.values().stream().map(List::size).collect(toList())), n / (m - 1.0), dev);

		// GRAPH
		printDistribution(hTickMap, m, false);

		printTestHeadline("TEST INT COMBINATION"); // Test 3

		m = 13;
		DoubleHashTable<Integer, String> t = new DoubleHashTable<>(m, new IntHashableFactory());
		var tableHashes = keys.stream().collect(groupingBy(k -> invokeHash(t, k, 0)));

		// 1, 2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D - reachability
		for (int k = 0; k < m; k++) {
			final int key = tableHashes.get(k).get(0);
			var hashes = IntStream.range(0, m).map(i -> invokeHash(t, key, i)).boxed().collect(toSet());
			// every place in the table must be reachable using i (if all others are in use)
			assertEquals(hashes.size(), 13);
		}

		Integer k0 = tableHashes.get(0).get(0);
		Integer k1 = tableHashes.get(0).get(1);
		Integer nextK = tableHashes.values().stream().flatMap(List::stream)
				.filter(k -> invokeHash(t, k, 0) == invokeHash(t, k1, 1)).findAny().get();

		// E - test empty
		assertEquals(t.find(0), Optional.empty(), "0 %s not in the table");

		// F, G - test insert
		assertEquals(t.insert(k0, "a"), true, k0 + " %s successfully inserted in table");
		assertEquals(t.maxRehashes(), 0); // first entry

		// H, I, J - test contained
		assertEquals(t.find(k0), Optional.of("a"), k0 + " %s in the table");
		assertEquals(t.find(k1), Optional.empty(), k1 + " %s not in the table");
		assertEquals(t.find(nextK), Optional.empty(), nextK + " %s not in the table");

		// K, L - insert second
		assertEquals(t.insert(k1, "b"), true, k1 + " %s successfully inserted in table");
		assertEquals(t.maxRehashes(), 1); // second entry using the same hash

		// M, N - test contained
		assertEquals(t.find(k0), Optional.of("a"), k0 + " %s in the table");
		assertEquals(t.find(k1), Optional.of("b"), k1 + " %s in the table");

		// O, P - insert third
		assertEquals(t.insert(nextK, "c"), true, nextK + " %s successfully inserted in table");
		assertEquals(t.maxRehashes(), 1); // second entry using the same hash

		// Q, R, S - test contained
		assertEquals(t.find(k0), Optional.of("a"), k0 + " %s in the table");
		assertEquals(t.find(k1), Optional.of("b"), k1 + " %s not in the table");
		assertEquals(t.find(nextK), Optional.of("c"), nextK + " %s not in the table");

		// T - check collisions
		assertEquals(t.collisions(), 1);
	}

	/**
	 * Test int hashing
	 */
	public static void testString() {
		int m = 17;
		int n = m * 50;
		double dev = n / m / 2.0;
		Random r = new Random(-2965331412945356690L);
		Encoder enc64 = Base64.getEncoder().withoutPadding();

		DoubleHashString dhi = new DoubleHashString(m);
		List<String> keys = r.ints(n).mapToObj(i -> enc64.encodeToString(BigInteger.valueOf(i).toByteArray()))
				.collect(toList());

		printTestHeadline("TEST STRING HASH"); // Test 4

		var hMap = keys.stream().collect(groupingBy(dhi::hash));

		// 1 - checking consistency
		assertEquals(keys.stream().collect(groupingBy(dhi::hash)), hMap, "hash() %s consistent");
		// 2 - checking collisions of hash
		assertEquals(getMedian(hMap.values().stream().map(List::size).collect(toList())), n / m, dev);
		// 3 - checking bounds of the hash values
		checkBounds(hMap.keySet(), 0, m - 1);

		// GRAPH
		printDistribution(hMap, m, false);

		printTestHeadline("TEST STRING HASH_TICK"); // Test 5

		var hTickMap = keys.stream().collect(groupingBy(dhi::hashTick));

		// 4 - checking consistency
		assertEquals(keys.stream().collect(groupingBy(dhi::hashTick)), hTickMap, "hashTick() %s consistent");
		// 5 - checking bounds of hashTick values, must not be 0 (-> would eliminate i)
		checkBounds(hTickMap.keySet(), 1, m - 1);
		// 6 - checking collisions of hashTick (zero excluded)
		assertEquals(getMedian(hTickMap.values().stream().map(List::size).collect(toList())), n / (m - 1.0), dev);

		// GRAPH
		printDistribution(hTickMap, m, false);

		printTestHeadline("TEST STRING COMBINATION"); // Test 6

		m = 13;
		DoubleHashTable<String, String> t = new DoubleHashTable<>(m, new StringHashableFactory());
		var tableHashes = keys.stream().collect(groupingBy(k -> invokeHash(t, k, 0)));

		// 1, 2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D - reachability
		for (int k = 0; k < m; k++) {
			final String key = tableHashes.get(k).get(0);
			var hashes = IntStream.range(0, m).map(i -> invokeHash(t, key, i)).boxed().collect(toSet());
			// every place in the table must be reachable using i (if all others are in use)
			assertEquals(hashes.size(), 13);
		}

		String k0 = tableHashes.get(0).get(0);
		String k1 = tableHashes.get(0).get(1);
		String nextK = tableHashes.values().stream().flatMap(List::stream)
				.filter(k -> invokeHash(t, k, 0) == invokeHash(t, k1, 1)).findAny().get();

		// E - test empty
		assertEquals(t.find(""), Optional.empty(), "empty string %s not in the table");

		// F, G - test insert
		assertEquals(t.insert(k0, "a"), true, k0 + " %s successfully inserted in table");
		assertEquals(t.maxRehashes(), 0); // first entry

		// H, I, J - test contained
		assertEquals(t.find(k0), Optional.of("a"), k0 + " %s in the table");
		assertEquals(t.find(k1), Optional.empty(), k1 + " %s not in the table");
		assertEquals(t.find(nextK), Optional.empty(), nextK + " %s not in the table");

		// K, L - insert second
		assertEquals(t.insert(k1, "b"), true, k1 + " %s successfully inserted in table");
		assertEquals(t.maxRehashes(), 1); // second entry using the same hash

		// M, N - test contained
		assertEquals(t.find(k0), Optional.of("a"), k0 + " %s in the table");
		assertEquals(t.find(k1), Optional.of("b"), k1 + " %s in the table");

		// O, P - insert third
		assertEquals(t.insert(nextK, "c"), true, nextK + " %s successfully inserted in table");
		assertEquals(t.maxRehashes(), 1); // second entry using the same hash

		// Q, R, S - test contained
		assertEquals(t.find(k0), Optional.of("a"), k0 + " %s in the table");
		assertEquals(t.find(k1), Optional.of("b"), k1 + " %s not in the table");
		assertEquals(t.find(nextK), Optional.of("c"), nextK + " %s not in the table");

		// T - check collisions
		assertEquals(t.collisions(), 1);
	}

	private static <K> int invokeHash(DoubleHashTable<K, ?> table, K key, int i) {
		try {
			Method m = table.getClass().getDeclaredMethod("hash", Object.class, Integer.TYPE);
			m.setAccessible(true);
			return (Integer) m.invoke(table, key, i);
		} catch (SecurityException | ReflectiveOperationException e) {
			throw new IllegalStateException("could not invoke hash on DoubleHashTable", e);
		}
	}

	private static double getMedian(Collection<? extends Number> iCol) {
		List<? extends Number> iList = new ArrayList<>(iCol);
		Collections.sort(iList, (a, b) -> Double.compare(a.doubleValue(), b.doubleValue()));
		return (iList.get(iList.size() / 2).doubleValue() + iList.get((iList.size() + 1) / 2).doubleValue()) / 2.0;
	}

	private static void checkBounds(Collection<? extends Number> nums, long min, long max) {
		String testId = getNextTestId();
		LongSummaryStatistics stats = nums.stream().mapToLong(Number::longValue).summaryStatistics();
		if (stats.getMin() >= min)
			System.out.format("  passed %s, result min: %d (>= %d)%n", testId, stats.getMin(), min);
		else
			System.err.format("  test %s failed min: %d (required: >= %d)%n", testId, stats.getMin(), min);
		if (stats.getMax() <= max)
			System.out.format("  passed %s, result max: %d (<= %d)%n", testId, stats.getMax(), max);
		else
			System.err.format("  test %s failed max: %d (required: <= %d)%n", testId, stats.getMax(), max);
	}

	/**
	 * Simple assert-method to compare two objects.
	 */
	private static void assertEquals(Object actual, Object expected, String isMustBe) {
		String testId = getNextTestId();
		if (Objects.equals(actual, expected))
			System.out.format("  passed %s, result: " + isMustBe + "%n", testId, "is");
		else
			System.err.format("  test %s failed: please note: " + isMustBe + "%n", testId, "must be");
	}

	/**
	 * Simple assert-method to compare two int.
	 */
	private static void assertEquals(int actual, int expected) {
		String testId = getNextTestId();
		if (actual == expected)
			System.out.format("  passed %s, result: %d%n", testId, actual);
		else
			System.err.format("  test %s failed: expected %d but was %d%n", testId, expected, actual);
	}

	/**
	 * assert-method to compare two doubles with max difference delta.
	 */
	private static void assertEquals(double actual, double expected, double delta) {
		String testId = getNextTestId();
		if (Math.abs(actual - expected) <= delta)
			System.out.format("  passed %s, result: %f (expected: %f, delta: %f)%n", testId, actual, expected, delta);
		else
			System.err.format("  test %s failed: expected %f but was %f (delta: %f)%n", testId, expected, actual,
					delta);
	}

	private static String getNextTestId() {
		return String.format("%s.%s", testMethod, testNum.incrementAndGet());
	}

	private static void printTestHeadline(String s) {
		System.out.format("Test %s: %s%n", testMethod.incrementAndGet(), s);
		testNum.reset();
	}

	private static void printDistribution(Map<Long, ? extends List<?>> result, int size, boolean detail) {
		var count = new TreeMap<>(result.entrySet().stream().collect(toMap(Entry::getKey, e -> e.getValue().size())));
		long max = count.values().stream().mapToLong(i -> i).max().getAsLong();
		double scale = max / 10.0;
		if (scale < 1.0 || detail)
			scale = 1.0;
		int low = Math.min(count.firstKey().intValue(), 0);
		int high = Math.max(count.lastKey().intValue(), size - 1);
		String indent = " ";
		System.out.print(indent + "\u250C");
		for (int k = low; k <= high; k++) {
			System.out.print("\u2500\u2500\u2500");
		}
		System.out.format("\u2510 (max: %d)%n", max);
		for (double j = max - (scale / 2); j > 0; j -= scale) {
			System.out.print(indent + "\u2502");
			for (int k = low; k <= high; k++) {
				long c = count.getOrDefault((long) k, 0);
				System.out.print(c >= j ? " \u2588 " : (c > Math.max(j - scale / 2, 0) ? " \u2584 " : "   "));
			}
			System.out.println("\u2502");
		}
		System.out.print(indent + "\u2514");
		for (int k = low; k <= high; k++) {
			System.out.print("\u2500\u253C\u2500");
		}
		System.out.format("\u2518 (y-scale: %.1f)%n%s ", scale, indent);
		for (int k = low; k <= high; k++) {
			String num = superscript(k);
			System.out.format(num.length() > 2 ? "%3s" : "%2s ", num);
		}
		System.out.println();
	}

	private static String superscript(long l) {
		String old = String.valueOf(l);
		StringBuilder res = new StringBuilder(old.length());
		for (int i = 0; i < old.length(); i++) {
			switch (old.charAt(i)) {
			case '-':
				res.append('\u207B');
				break;
			case '0':
				res.append('\u2070');
				break;
			case '1':
				res.append('\u00B9');
				break;
			case '2':
				res.append('\u00B2');
				break;
			case '3':
				res.append('\u00B3');
				break;
			default:
				res.append((char) (old.charAt(i) + 0x2040));
				break;
			}
		}
		return res.toString();
	}

	/**
	 * Counter that can count from 1 to 9 then A to Z (35 digits)
	 */
	public static class Counter {
		private int count;

		public Counter(int count) {
			this.count = count;
		}

		public String incrementAndGet() {
			count++;
			return toString();
		}

		@Override
		public String toString() {
			return String.valueOf(toChar());
		}

		public char toChar() {
			return (char) (count + (count < 10 ? 48 : 55));
		}

		public void reset() {
			count = 0;
		}
	}

	private static void tryVersionCheck() {
		try {
			Class.forName("CFUpdate").getDeclaredMethod("checkForNewVersion", String.class, Class.class, String.class)
					.invoke(null, "blatt05", DoubleHashTest.class, VERSION);
		} catch (@SuppressWarnings("unused") ClassNotFoundException e) {
			System.out.println("Automatic update checks are inactive, download the CFUpdate class for that:");
			System.out.println("https://raw.githubusercontent.com/MaisiKoleni/GAD-Extras/master/version/CFUpdate.java");
			System.out.println();
		} catch (IllegalArgumentException | ReflectiveOperationException | SecurityException e) {
			System.err.println("Something went wrong invoking checkForNewerVersion:");
			e.printStackTrace();
		}
	}
}

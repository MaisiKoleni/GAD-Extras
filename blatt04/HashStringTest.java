import static java.util.stream.Collectors.*;

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
import java.util.Random;
import java.util.TreeMap;

/**
 * Öffentliche Testfälle von Christian Femers. Siehe
 * https://github.com/MaisiKoleni/GAD-Extras
 * 
 * Bitte nicht mit abgeben. (geht sonst nicht)
 * 
 * @version 1.1
 * 
 * @since 1.1 fixed median calculation
 * 
 * @author Christian Femers (IN.TUM)
 *
 */
public class HashStringTest {

	private static final String VERSION = "1.1";

	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(0);

	static {
		tryVersionCheck();

		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		// Test 1
		testString13();
		// Test 2
		testString17();
	}

	/**
	 * Test String hashing m == 13
	 */
	public static void testString13() {
		int m = 13;
		int n = m * 50;
		double dev = n / m / 2.0;
		Random r = new Random(-2965331412945356690L);
		Encoder enc64 = Base64.getEncoder().withoutPadding();

		HashString hs = new HashString(m);
		List<String> keys = r.ints(n).mapToObj(i -> enc64.encodeToString(BigInteger.valueOf(i).toByteArray()))
				.collect(toList());

		printTestHeadline("TEST STRING HASH m == 13"); // Test 1

		var hMap = keys.stream().collect(groupingBy(hs::hash));

		// 1 - checking consistency
		assertEquals(keys.stream().collect(groupingBy(hs::hash)), hMap, "hash() %s consistent");
		// 2 - checking bounds of the hash values
		checkBounds(hMap.keySet(), 0, m - 1);
		// 3 - checking collisions of hash
		assertEquals(getMedian(hMap.values().stream().map(List::size).collect(toList())), n / m, dev);

		// GRAPH
		printDistribution(hMap, m, false);
	}

	/**
	 * Test String hashing m == 17
	 */
	public static void testString17() {
		int m = 17;
		int n = m * 50;
		double dev = n / m / 2.0;
		Random r = new Random(-2990996836239250388L);
		Encoder enc64 = Base64.getEncoder().withoutPadding();

		HashString hs = new HashString(m);
		List<String> keys = r.ints(n).mapToObj(i -> enc64.encodeToString(BigInteger.valueOf(i).toByteArray()))
				.collect(toList());

		printTestHeadline("TEST STRING HASH m == 17"); // Test 2

		var hMap = keys.stream().collect(groupingBy(hs::hash));

		// 1 - checking consistency
		assertEquals(keys.stream().collect(groupingBy(hs::hash)), hMap, "hash() %s consistent");
		// 2 - checking bounds of the hash values
		checkBounds(hMap.keySet(), 0, m - 1);
		// 3 - checking collisions of hash
		assertEquals(getMedian(hMap.values().stream().map(List::size).collect(toList())), n / m, dev);

		// GRAPH
		printDistribution(hMap, m, false);
	}

	private static double getMedian(Collection<? extends Number> iCol) {
		List<? extends Number> iList = new ArrayList<>(iCol);
		Collections.sort(iList, (a, b) -> Double.compare(a.doubleValue(), b.doubleValue()));
		if (iList.size() % 2 == 0)
			return (iList.get(iList.size() / 2).doubleValue() + iList.get(iList.size() / 2 - 1).doubleValue()) / 2.0;
		return iList.get(iList.size() / 2).doubleValue();
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

	private static void printDistribution(Map<Integer, ? extends List<?>> hMap, int size, boolean detail) {
		var count = new TreeMap<>(hMap.entrySet().stream().collect(toMap(Entry::getKey, e -> e.getValue().size())));
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
				long c = count.getOrDefault(k, 0);
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
					.invoke(null, "blatt04", HashStringTest.class, VERSION);
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

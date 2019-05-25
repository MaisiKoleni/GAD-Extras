import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Öffentliche Testfälle von Christian Femers. Siehe
 * https://github.com/MaisiKoleni/GAD-Extras
 * 
 * Bitte nicht mit abgeben. (geht sonst nicht)
 * 
 * @version 1.5
 * 
 * @author Christian Femers (IN.TUM)
 *
 */
public class RingQueueTest {

	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(16);

	private static final Pattern fromToPattern = Pattern.compile(" from | to ");
	private static final int[] EMPTY = new int[0];

	static {
		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		// Test H, I
		testMixed();
	}

	/**
	 * Test mixed behaviour
	 */
	public static void testMixed() {
		printTestHeadline("TEST MIXED 1"); // Test H
		RingQueue rq = new RingQueue(2, 4);
		DynamicArray da = getDynamicArray(rq);
		int i;

		// 1, 2, 3
		checkDynArray(da, EMPTY);
		checkSize(rq, 0);
		checkFromTo(rq, 0, 0);

		// 4, 5, 6
		rq.enqueue(4);
		checkDynArray(da, 4, 0);
		checkSize(rq, 1);
		checkFromTo(rq, 0, 0);

		// 7, 8, 9
		rq.enqueue(2);
		checkDynArray(da, 4, 2);
		checkSize(rq, 2);
		checkFromTo(rq, 0, 1);

		// A, B, C, D
		i = rq.dequeue();
		assertEquals(i, 4);
		checkDynArray(da, 4, 2);
		checkSize(rq, 1);
		checkFromTo(rq, 1, 1);

		// E, F, G
		rq.enqueue(8);
		checkDynArray(da, 8, 2);
		checkSize(rq, 2);
		checkFromTo(rq, 1, 0);

		// H, I, J
		rq.enqueue(1);
		checkDynArray(da, 2, 8, 1, 0, 0, 0);
		checkSize(rq, 3);
		checkFromTo(rq, 0, 2);

		// K, L, M
		rq.enqueue(3);
		checkDynArray(da, 2, 8, 1, 3, 0, 0);
		checkSize(rq, 4);
		checkFromTo(rq, 0, 3);

		// N, O, P
		rq.enqueue(7);
		rq.enqueue(6);
		checkDynArray(da, 2, 8, 1, 3, 7, 6);
		checkSize(rq, 6);
		checkFromTo(rq, 0, 5);

		// Q, R, S, T, U, V, W
		i = rq.dequeue();
		assertEquals(i, 2);
		i = rq.dequeue();
		assertEquals(i, 8);
		i = rq.dequeue();
		assertEquals(i, 1);
		i = rq.dequeue();
		assertEquals(i, 3);
		checkDynArray(da, 2, 8, 1, 3, 7, 6);
		checkSize(rq, 2);
		checkFromTo(rq, 4, 5);

		// X, Y, Z
		i = rq.dequeue();
		assertEquals(i, 7);
		checkDynArray(da, 6, 0);
		checkFromTo(rq, 0, 0);

		printTestHeadline("TEST MIXED 2"); // Test I

		// 1, 2, 3
		rq.dequeue();
		checkDynArray(da, EMPTY);
		checkSize(rq, 0);
		checkFromTo(rq, 0, 0);

		// 4, 5, 6
		rq.enqueue(-1);
		rq.enqueue(-2);
		rq.enqueue(-3);
		checkDynArray(da, -1, -2, -3, 0, 0, 0);
		checkSize(rq, 3);
		checkFromTo(rq, 0, 2);

		// 7, 8, 9
		for (int j = 1; j <= 20; j++) {
			rq.enqueue(j);
			rq.dequeue();
		}
		checkDynArray(da, 16, 17, 18, 19, 20, 15);
		checkSize(rq, 3);
		checkFromTo(rq, 2, 4);

		// A, B, C, D
		i = rq.dequeue();
		assertEquals(i, 18);
		checkDynArray(da, 16, 17, 18, 19, 20, 15);
		checkSize(rq, 2);
		checkFromTo(rq, 3, 4);

		// E, F, G, H
		i = rq.dequeue();
		assertEquals(i, 19);
		checkDynArray(da, 20, 0);
		checkSize(rq, 1);
		checkFromTo(rq, 0, 0);

		// I, J, K, L
		i = rq.dequeue();
		assertEquals(i, 20);
		checkDynArray(da, EMPTY);
		checkSize(rq, 0);
		checkFromTo(rq, 0, 0);
	}

	/**
	 * Compares all of the DynamicArrays inner content with the given sequence
	 * 
	 * @param da       the DynamicArray to check
	 * @param contents the array (varargs) that is expected
	 */
	private static void checkDynArray(DynamicArray da, int... contents) {
		String testId = getNextTestId();
		if (da.getInnerLength() != contents.length) {
			System.err.format("  test %s failed: expected length %d array but was %d%n", testId, contents.length,
					da.getInnerLength());
			return;
		}
		List<Integer> differences = new ArrayList<>();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != da.get(i))
				differences.add(i);
		}
		if (differences.isEmpty())
			System.out.format("  passed %s, result: %s%n", testId, da);
		else {
			System.err.format("  test %s failed: array content differs at indices %s%n", testId, differences);
			System.err.format("     expected: %s%n", Arrays.toString(contents));
			System.err.format("     actual:   %s%n", da);
		}
	}

	private static void checkFromTo(RingQueue rq, int fromExpected, int toExpected) {
		String testId = getNextTestId();
		String[] parts = fromToPattern.split(rq.toString());
		int from = Integer.parseInt(parts[1]);
		int to = Integer.parseInt(parts[2]);
		if (rq.isEmpty() && (from != 0 || to != 0))
			System.err.format("  test %s failed: RingQueue is empty, but not: from == to == 0", testId);
		if (from == fromExpected && to == toExpected)
			System.out.format("  passed %s, result: from %d to %d%n", testId, from, to);
		else
			System.err.format("  test %s failed: expected from %d to %d but was from %d to %d%n", testId, fromExpected,
					toExpected, from, to);
	}

	private static void checkSize(RingQueue rq, int expected) {
		assertEquals(rq.getSize(), expected);
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
	 * Gets the DynmaicArray via Reflection, this should not be a problem, since
	 * this part is already given.
	 */
	private static DynamicArray getDynamicArray(RingQueue rq) {
		try {
			Field f = rq.getClass().getDeclaredField("dynArr");
			f.setAccessible(true);
			return (DynamicArray) Objects.requireNonNull(f.get(rq));
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("Konnte das RingQueue Attribut 'dynArr' nicht finden. "
					+ "Bitte überprüfen, dass es nicht umbennant wurde.", e);
		} catch (NullPointerException e) {
			throw new IllegalStateException("Entweder der RingQueue ist null oder der DynamicArray 'dynArr' darinnen.",
					e);
		} catch (ClassCastException e) {
			throw new IllegalStateException("In Attribut 'dynArr' ist kein DynamicArray-Objekt O.o", e);
		} catch (IllegalAccessException | SecurityException e) {
			throw new IllegalStateException("Irgendetwas hat bei der Reflection nicht geklappt. "
					+ "Sicher, dass das ein ganz normales Java-Projekt ist?", e);
		}
	}

	private static String getNextTestId() {
		return String.format("%s.%s", testMethod, testNum.incrementAndGet());
	}

	private static void printTestHeadline(String s) {
		System.out.format("Test %s: %s%n", testMethod.incrementAndGet(), s);
		testNum.reset();
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
}

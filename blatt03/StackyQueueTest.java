import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
public class StackyQueueTest {

	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(11);

	private static final int[] EMPTY = new int[0];

	static {
		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		// Test C, D
		testBasic();
		// test E, F, G
		testMixed();
	}

	/**
	 * Test basic behaviour
	 */
	public static void testBasic() {
		printTestHeadline("TEST ENQUEUE"); // Test C
		StackyQueue sq = new StackyQueue(2, 3);
		DynamicArray a = getDynamicArray(sq, true);
		DynamicArray b = getDynamicArray(sq, false);

		// 1, 2, 3
		checkDynArray(a, EMPTY);
		checkDynArray(b, EMPTY);
		checkLength(sq, 0);

		// 4, 5, 6
		sq.enqueue(-5);
		checkDynArray(a, -5, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 1);

		// 7, 8, 9
		sq.enqueue(12);
		checkDynArray(a, -5, 12);
		checkDynArray(b, EMPTY);
		checkLength(sq, 2);

		// A, B, C
		sq.enqueue(3);
		checkDynArray(a, -5, 12, 3, 0, 0, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 3);

		// D, E, F
		sq.enqueue(4);
		checkDynArray(a, -5, 12, 3, 4, 0, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 4);

		printTestHeadline("TEST DEQUEUE"); // Test D
		int i;

		// 1, 2, 3, 4
		i = sq.dequeue();
		assertEquals(i, -5); // erwartet erstes Element, weil Warteschlange
		checkDynArray(a, EMPTY);
		checkDynArray(b, 4, 3, 12, 0, 0, 0);
		checkLength(sq, 3);

		// 5, 6, 7, 8
		i = sq.dequeue();
		assertEquals(i, 12); // nun zweites Element
		checkDynArray(a, EMPTY);
		checkDynArray(b, 4, 3, 0, 0, 0, 0);
		checkLength(sq, 2);

		// 9, A, B, C
		i = sq.dequeue();
		assertEquals(i, 3); // nun drittes Element
		checkDynArray(a, EMPTY);
		checkDynArray(b, 4, 0);
		checkLength(sq, 1);

		// E, F, G, H
		i = sq.dequeue();
		assertEquals(i, 4); // und letztes Element
		checkDynArray(a, EMPTY);
		checkDynArray(b, EMPTY);
		checkLength(sq, 0);
	}

	/**
	 * Test mixed behaviour
	 */
	public static void testMixed() {
		printTestHeadline("TEST MIXED SMALL"); // Test E
		StackyQueue sq = new StackyQueue(2, 3);
		DynamicArray a = getDynamicArray(sq, true);
		DynamicArray b = getDynamicArray(sq, false);
		int i;

		// 1, 2, 3
		checkDynArray(a, EMPTY);
		checkDynArray(b, EMPTY);
		checkLength(sq, 0);

		// 4, 5, 6
		sq.enqueue(2);
		checkDynArray(a, 2, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 1);

		// 7, 8, 9, A
		i = sq.dequeue();
		assertEquals(i, 2);
		checkDynArray(a, EMPTY);
		checkDynArray(b, EMPTY);
		checkLength(sq, 0);

		// B, C, D
		sq.enqueue(12);
		checkDynArray(a, 12, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 1);

		// E, F, G
		sq.enqueue(3);
		checkDynArray(a, 12, 3);
		checkDynArray(b, EMPTY);
		checkLength(sq, 2);

		// H, I, J, K
		i = sq.dequeue();
		assertEquals(i, 12);
		checkDynArray(a, EMPTY);
		checkDynArray(b, 3, 0);
		checkLength(sq, 1);

		// L, M, N
		sq.enqueue(4);
		checkDynArray(a, 4, 0);
		checkDynArray(b, 3, 0);
		checkLength(sq, 2);

		// O, P, Q, R
		i = sq.dequeue();
		assertEquals(i, 3);
		checkDynArray(a, 4, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 1);

		// S, T, U, V
		i = sq.dequeue();
		assertEquals(i, 4);
		checkDynArray(a, EMPTY);
		checkDynArray(b, EMPTY);
		checkLength(sq, 0);

		printTestHeadline("TEST MIXED MEDIUM"); // Test F

		// 1, 2, 3
		sq.enqueue(1);
		sq.enqueue(2);
		sq.enqueue(3);
		checkDynArray(a, 1, 2, 3, 0, 0, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 3);

		// 4, 5, 6, 7
		i = sq.dequeue();
		assertEquals(i, 1);
		checkDynArray(a, EMPTY);
		checkDynArray(b, 3, 2, 0, 0, 0, 0);
		checkLength(sq, 2);

		// 8, 9, A
		sq.enqueue(-1);
		sq.enqueue(-8);
		checkDynArray(a, -1, -8);
		checkDynArray(b, 3, 2, 0, 0, 0, 0);
		checkLength(sq, 4);

		// B, C, D, E
		i = sq.dequeue();
		assertEquals(i, 2);
		checkDynArray(a, -1, -8);
		checkDynArray(b, 3, 0);
		checkLength(sq, 3);

		// F, G, H, I
		i = sq.dequeue();
		assertEquals(i, 3);
		checkDynArray(a, -1, -8);
		checkDynArray(b, EMPTY);
		checkLength(sq, 2);

		// J, K, L
		sq.enqueue(9);
		sq.enqueue(6);
		checkDynArray(a, -1, -8, 9, 6, 0, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 4);

		// M, N, O, P
		i = sq.dequeue();
		assertEquals(i, -1);
		checkDynArray(a, EMPTY);
		checkDynArray(b, 6, 9, -8, 0, 0, 0);
		checkLength(sq, 3);

		// Q, R, S, T, U
		sq.enqueue(7);
		i = sq.dequeue();
		assertEquals(i, -8);
		i = sq.dequeue();
		assertEquals(i, 9);
		checkDynArray(a, 7, 0);
		checkDynArray(b, 6, 0);
		checkLength(sq, 2);

		// V, W, X, Y, Z
		i = sq.dequeue();
		assertEquals(i, 6);
		checkDynArray(a, 7, 0);
		checkDynArray(b, EMPTY);
		i = sq.dequeue();
		assertEquals(i, 7);
		checkDynArray(b, EMPTY);

		printTestHeadline("TEST MIXED LARGE"); // Test G

		// 1, 2, 3
		for (int j = 1; j <= 12; j++)
			sq.enqueue(j);
		checkDynArray(a, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 0);
		checkDynArray(b, EMPTY);
		checkLength(sq, 12);

		// 4, 5, 6, 7
		i = sq.dequeue();
		assertEquals(i, 1);
		checkDynArray(a, EMPTY);
		checkDynArray(b, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 0, 0, 0);
		checkLength(sq, 11);

		// 8, 9, A, B, C, D, E, F, G, H, I, J, K
		for (int j = 2; j <= 11; j++)
			assertEquals(j, sq.dequeue());
		checkDynArray(a, EMPTY);
		checkDynArray(b, 12, 0);
		checkLength(sq, 1);

		// L, M, N
		for (int j = -9; j <= -5; j++)
			sq.enqueue(j);
		checkDynArray(a, -9, -8, -7, -6, -5, 0);
		checkDynArray(b, 12, 0);
		checkLength(sq, 6);

		// O, P, Q
		i = sq.dequeue();
		assertEquals(i, 12);
		checkDynArray(a, -9, -8, -7, -6, -5, 0);
		checkDynArray(b, EMPTY);

		// R, S, T
		i = sq.dequeue();
		assertEquals(i, -9);
		checkDynArray(a, EMPTY);
		checkDynArray(b, -5, -6, -7, -8, 0, 0);

		// U, V, W, X, Y, Z
		for (int j = -8; j <= -5; j++)
			assertEquals(j, sq.dequeue());
		checkDynArray(a, EMPTY);
		checkDynArray(b, EMPTY);
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

	private static void checkLength(StackyQueue sq, int expected) {
		assertEquals(sq.getLength(), expected);
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
	private static DynamicArray getDynamicArray(StackyQueue sq, boolean first) {
		String name = first ? "first" : "second";
		try {
			Field f = sq.getClass().getDeclaredField(name);
			f.setAccessible(true);
			return getDynamicArray((DynamicStack) f.get(sq));
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("Konnte das StackyQueue Attribut '" + name + "' nicht finden. "
					+ "Bitte überprüfen, dass es nicht umbennant wurde.", e);
		} catch (NullPointerException e) {
			throw new IllegalStateException(
					"Entweder der DynamicStack ist null oder der StackyQueue '" + name + "' darinnen.", e);
		} catch (ClassCastException e) {
			throw new IllegalStateException("In Attribut '" + name + "' ist kein StackyQueue-Objekt O.o", e);
		} catch (IllegalAccessException | SecurityException e) {
			throw new IllegalStateException("Irgendetwas hat bei der Reflection nicht geklappt. "
					+ "Sicher, dass das ein ganz normales Java-Projekt ist?", e);
		}
	}

	/**
	 * Gets the DynmaicArray via Reflection, this should not be a problem, since
	 * this part is already given.
	 */
	private static DynamicArray getDynamicArray(DynamicStack ds) {
		try {
			Field f = ds.getClass().getDeclaredField("dynArr");
			f.setAccessible(true);
			return (DynamicArray) Objects.requireNonNull(f.get(ds));
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("Konnte das DynamicArray Attribut 'dynArr' nicht finden. "
					+ "Bitte überprüfen, dass es nicht umbennant wurde.", e);
		} catch (NullPointerException e) {
			throw new IllegalStateException(
					"Entweder der DynamicStack ist null oder der DynamicArray 'dynArr' darinnen.", e);
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

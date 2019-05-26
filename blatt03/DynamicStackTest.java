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
 * @version 1.6
 * 
 * @since 1.4 small style improvements [added StackyQueueTest]
 * @since 1.5 [added RingQueueTest]
 * @since 1.6 added version check
 * 
 * @author Christian Femers (IN.TUM)
 *
 */
public class DynamicStackTest {

	private static final String VERSION = "1.4";

	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(5);

	private static final int[] EMPTY = new int[0];

	static {
		tryVersionCheck();

		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		// Test 6, 7, 8
		test24();

		// Test 9, A, B
		test12();
	}

	/**
	 * Test length increase/decrease with growth 2, overhead 4
	 */
	public static void test24() {
		printTestHeadline("TEST PUSH 2,4"); // Test 6
		DynamicStack ds = new DynamicStack(2, 4);
		DynamicArray da = getDynamicArray(ds);

		// 1, 2
		checkDynArray(da, EMPTY); // sollte zuerst noch leer sein
		assertEquals(0, ds.getLength());

		// 3, 4
		ds.pushBack(1);
		checkDynArray(da, 1, 0); // 1 angefügt, also bei index 0; Größe 1 * 2 = 2
		assertEquals(1, ds.getLength());

		// 5
		ds.pushBack(2);
		checkDynArray(da, 1, 2); // 2 angefügt, also bei index 1; Größe reicht noch

		// 6
		ds.pushBack(3);
		checkDynArray(da, 1, 2, 3, 0, 0, 0); // 3 angefügt, also bei index 2; Größe 3 * 2 = 6

		// 7
		ds.pushBack(4);
		checkDynArray(da, 1, 2, 3, 4, 0, 0); // 4 angefügt, also bei index 3; Größe reicht noch

		// 8
		ds.pushBack(5);
		checkDynArray(da, 1, 2, 3, 4, 5, 0); // 5 angefügt, also bei index 4; Größe reicht noch

		// 9
		ds.pushBack(6);
		checkDynArray(da, 1, 2, 3, 4, 5, 6); // 6 angefügt, also bei index 5; Größe reicht noch

		// A, B
		ds.pushBack(7);
		checkDynArray(da, 1, 2, 3, 4, 5, 6, 7, 0, 0, 0, 0, 0, 0, 0); // 7 angefügt, also bei index 6; Größe 7 * 2 = 14
		assertEquals(7, ds.getLength());

		printTestHeadline("TEST POP 2,4"); // Test 7
		int i;

		// 1, 2, 3
		i = ds.popBack();
		assertEquals(i, 7); // "auf" dem Stack lag "zuoberst" die 7
		checkDynArray(da, 1, 2, 3, 4, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0); // pop schreibt an die Stelle eine 0 (Vorgabe!)
		assertEquals(6, ds.getLength());

		// 4, 5
		i = ds.popBack();
		assertEquals(i, 6); // "auf" dem Stack lag "zuoberst" die 6
		checkDynArray(da, 1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0); // pop schreibt an die Stelle eine 0 (Vorgabe!)

		// 6, 7
		i = ds.popBack();
		assertEquals(i, 5); // "auf" dem Stack lag "zuoberst" die 5
		checkDynArray(da, 1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0); // pop schreibt an die Stelle eine 0 (Vorgabe!)

		// 8, 9, A
		i = ds.popBack();
		assertEquals(i, 4); // "auf" dem Stack lag "zuoberst" die 4
		checkDynArray(da, 1, 2, 3, 0, 0, 0); // Größenänderung, weil 3 * 4 < 14
		assertEquals(3, ds.getLength());

		// B, C
		i = ds.popBack();
		assertEquals(i, 3); // "auf" dem Stack lag "zuoberst" die 3
		checkDynArray(da, 1, 2, 0, 0, 0, 0); // pop schreibt an die Stelle eine 0 (Vorgabe!)

		// D, E, F
		i = ds.popBack();
		assertEquals(i, 2); // "auf" dem Stack lag "zuoberst" die 2
		checkDynArray(da, 1, 0); // Größenänderung, weil 1 * 4 < 6
		assertEquals(1, ds.getLength());

		// G, H, I
		i = ds.popBack();
		assertEquals(i, 1); // "auf" dem Stack lag "zuoberst" die 1
		checkDynArray(da, EMPTY); // Größenänderung, weil 0 * 4 < 2
		assertEquals(0, ds.getLength());

		printTestHeadline("TEST MIXED 2,4"); // Test 8

		// 1
		ds.pushBack(2); // Größenänderung passiert hier: 1 * 2 = 2
		checkDynArray(da, 2, 0);

		// 2, 3
		i = ds.popBack(); // Größenänderung passiert hier: 0 * 2 < 2
		assertEquals(i, 2);
		checkDynArray(da, EMPTY);

		// 4, 5
		ds.pushBack(4); // Größenänderung passiert hier: 1 * 2 = 2
		ds.pushBack(-5);
		checkDynArray(da, 4, -5);
		assertEquals(2, ds.getLength());

		// 6
		ds.pushBack(3); // Größenänderung passiert hier: 3 * 2 = 6
		ds.pushBack(8);
		checkDynArray(da, 4, -5, 3, 8, 0, 0);

		// 7, 8, 9, A
		i = ds.popBack();
		assertEquals(i, 8);
		i = ds.popBack();
		assertEquals(i, 3);
		i = ds.popBack(); // Größenänderung passiert hier: 1 * 4 < 6
		assertEquals(i, -5);
		checkDynArray(da, 4, 0);

		// B, C
		ds.pushBack(4);
		checkDynArray(da, 4, 4);
		assertEquals(2, ds.getLength());

		// D, E, F, G
		i = ds.popBack();
		assertEquals(i, 4);
		i = ds.popBack(); // Größenänderung passiert hier: 0 * 2 < 2
		assertEquals(i, 4);
		checkDynArray(da, EMPTY);
		assertEquals(0, ds.getLength());
	}

	/**
	 * Test length increase/decrease with growth 1, overhead 2
	 */
	public static void test12() {
		printTestHeadline("TEST PUSH 1,2"); // Test 9
		DynamicStack ds = new DynamicStack(1, 2);
		DynamicArray da = getDynamicArray(ds);

		// 1, 2
		checkDynArray(da, EMPTY); // sollte zuerst noch leer sein
		assertEquals(0, ds.getLength());

		// 3, 4
		ds.pushBack(9);
		checkDynArray(da, 9); // Größe 1 * 1 = 1
		assertEquals(1, ds.getLength());

		// 5
		ds.pushBack(8);
		checkDynArray(da, 9, 8); // Größe 2 * 1 = 1

		// 6
		ds.pushBack(7);
		checkDynArray(da, 9, 8, 7); // Größe 3 * 1 = 1

		// 7, 8
		ds.pushBack(6);
		checkDynArray(da, 9, 8, 7, 6); // Größe 4 * 1 = 1
		assertEquals(4, ds.getLength());

		printTestHeadline("TEST POP 1,2"); // Test A
		int i;

		// 1, 2, 3
		i = ds.popBack();
		assertEquals(i, 6); // "auf" dem Stack lag "zuoberst" die 4
		checkDynArray(da, 9, 8, 7, 0); // pop schreibt an die Stelle eine 0 (Vorgabe!)
		assertEquals(3, ds.getLength());

		// 4, 5, 6
		i = ds.popBack();
		assertEquals(i, 7); // "auf" dem Stack lag "zuoberst" die 3
		checkDynArray(da, 9, 8, 0, 0); // keine Änderung, da nicht 2 * 2 < 4
		assertEquals(2, ds.getLength());

		// 7, 8, 9
		i = ds.popBack();
		assertEquals(i, 8); // "auf" dem Stack lag "zuoberst" die 2
		checkDynArray(da, 9); // Größenänderung, weil 1 * 2 < 4
		assertEquals(1, ds.getLength());

		// A, B, C
		i = ds.popBack();
		assertEquals(i, 9); // "auf" dem Stack lag "zuoberst" die 1
		checkDynArray(da, EMPTY); // Größenänderung, weil 0 * 2 < 1
		assertEquals(0, ds.getLength());

		printTestHeadline("TEST MIXED 1,2"); // Test B

		// 1
		ds.pushBack(2); // Größenänderung passiert hier: 1 * 1 = 1
		checkDynArray(da, 2);

		// 2, 3
		i = ds.popBack(); // Größenänderung passiert hier: 0 * 1 < 1
		assertEquals(i, 2);
		checkDynArray(da, EMPTY);

		// 4, 5
		ds.pushBack(4); // Größenänderung passiert hier: 1 * 1 = 1
		ds.pushBack(-5); // Größenänderung passiert hier: 2 * 1 = 2
		checkDynArray(da, 4, -5);
		assertEquals(2, ds.getLength());

		// 6
		ds.pushBack(3); // Größenänderung passiert hier: 3 * 1 = 3
		ds.pushBack(8); // Größenänderung passiert hier: 4 * 1 = 4
		checkDynArray(da, 4, -5, 3, 8);

		// 7, 8, 9, A
		i = ds.popBack();
		assertEquals(i, 8);
		i = ds.popBack();
		assertEquals(i, 3);
		i = ds.popBack(); // Größenänderung passiert hier: 1 * 2 < 4
		assertEquals(i, -5);
		checkDynArray(da, 4);

		// B, C
		ds.pushBack(4); // Größenänderung passiert hier: 2 * 1 = 2
		checkDynArray(da, 4, 4);
		assertEquals(2, ds.getLength());

		// D, E, F, G
		i = ds.popBack();
		assertEquals(i, 4);
		i = ds.popBack(); // Größenänderung passiert hier: 0 * 1 < 2
		assertEquals(i, 4);
		checkDynArray(da, EMPTY);
		assertEquals(0, ds.getLength());
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

	private static void tryVersionCheck() {
		try {
			Class.forName("CFUpdate").getDeclaredMethod("checkForNewVersion", String.class, Class.class, String.class)
					.invoke(null, "blatt03", DynamicStackTest.class, VERSION);
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
public class RadixSortTest {

	private static final String VERSION = "1.0";

	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(0);

	static {
		tryVersionCheck();

		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		// Test 1
		testKey();
		// Test 2
		testMaxDecimalPlaces();
		// Test 3
		testConcatenate();
		// Test 4
		testKSort();
		// Test 5
		testSort();
	}

	/**
	 * Test key method
	 */
	public static void testKey() {
		printTestHeadline("TEST KEY");

		// 1
		assertEquals(invokeKey(0, 0), 0);

		// 2
		assertEquals(invokeKey(1, 0), 1);

		// 3
		assertEquals(invokeKey(9, 0), 9);

		// 4
		assertEquals(invokeKey(10, 0), 0);

		// 5
		assertEquals(invokeKey(12, 0), 2);

		// 6
		assertEquals(invokeKey(12, 1), 1);

		// 7
		assertEquals(invokeKey(123_456, 5), 1);

		// 8
		assertEquals(invokeKey(123_456, 0), 6);

		// 9
		assertEquals(invokeKey(123_456, 3), 3);

		// A
		assertThrows(() -> invokeKey(-1, 0), IllegalArgumentException.class);

		// B
		assertEquals(invokeKey(8, 1), 0);

		// C
		assertEquals(invokeKey(8, 2), 0);
	}

	/**
	 * Test getMaxDecimalPlaces method
	 */
	public static void testMaxDecimalPlaces() {
		printTestHeadline("TEST MAX_DECIMAl_PLACES");

		// 1
		assertEquals(invokeGetMaxDecimalPlaces(0), 1);

		// 2
		assertEquals(invokeGetMaxDecimalPlaces(1, 0), 1);

		// 3
		assertEquals(invokeGetMaxDecimalPlaces(1, 5, 8), 1);

		// 4
		assertEquals(invokeGetMaxDecimalPlaces(10, 0), 2);

		// 5
		assertEquals(invokeGetMaxDecimalPlaces(2, 10, 12), 2);

		// 6
		assertEquals(invokeGetMaxDecimalPlaces(12, 123, 1234), 4);

		// 7
		assertEquals(invokeGetMaxDecimalPlaces(123_456, 123, 5), 6);

		// 8
		assertEquals(invokeGetMaxDecimalPlaces(123_456), 6);

		// 9
		assertEquals(invokeGetMaxDecimalPlaces(23_456, 3), 5);
	}

	/**
	 * Test concatenate method
	 */
	public static void testConcatenate() {
		printTestHeadline("TEST CONCATENATE");

		BucketArray ba;

		// 1
		ba = BucketArray.of();
		checkArray(invokeConcatenate(ba.build(), ints(ba.count())));

		// 2
		ba = BucketArray.of(0);
		checkArray(invokeConcatenate(ba.build(), ints(ba.count())), 0);

		// 3
		ba = BucketArray.of(0, 1);
		checkArray(invokeConcatenate(ba.build(), ints(ba.count())), 0, 1);

		// 4
		ba = BucketArray.of(0)
				.and(1);
		checkArray(invokeConcatenate(ba.build(), ints(ba.count())), 0, 1);

		// 5
		ba = BucketArray.of()
				.and();
		checkArray(invokeConcatenate(ba.build(), ints(ba.count())));

		// 6
		ba = BucketArray.of(1)
				.and()
				.and(2);
		checkArray(invokeConcatenate(ba.build(), ints(ba.count())), 1, 2);

		// 7
		ba = BucketArray.of(1)
				.and(8, 4)
				.and(2);
		checkArray(invokeConcatenate(ba.build(), ints(ba.count())), 1, 8, 4, 2);

		// 8
		ba = BucketArray.of(0)
				.and(1, 4, 5)
				.and()
				.and(6, 4, 12)
				.and(20, 1, 0);
		checkArray(invokeConcatenate(ba.build(), ints(ba.count())), 0, 1, 4, 5, 6, 4, 12, 20, 1, 0);
	}

	/**
	 * Test kSort method
	 */
	public static void testKSort() {
		printTestHeadline("TEST K_SORT");

		// 1 - Einerstelle
		checkArray(invokeKSort(0, 1, 3, 2), 1, 2, 3);

		// 2 - Einerstelle 2
		checkArray(invokeKSort(0, 7, 1, 5, 6, 5, 8, 4, 0, 2, 9, 2), 0, 1, 2, 2, 4, 5, 5, 6, 7, 8, 9);

		// 3 - Zehnerstelle
		checkArray(invokeKSort(1, 10, 50, 60, 50, 80, 40, 0), 0, 10, 40, 50, 50, 60, 80);

		// 4 - Nur Zehnerstelle betrachten, folglich muss 58 vor 53 bestehen bleiben
		checkArray(invokeKSort(1, 74, 11, 58, 64, 53, 80, 41, 9), 9, 11, 41, 58, 53, 64, 74, 80);

		// 5 - Nur Zehnerstelle betrachten, keine Änderung erlaubt
		checkArray(invokeKSort(1, 7, 1, 5, 6, 5, 8, 4, 0, 2, 9, 2), 7, 1, 5, 6, 5, 8, 4, 0, 2, 9, 2);
	}

	/**
	 * Test sort method
	 */
	public static void testSort() {
		printTestHeadline("TEST SORT");

		// 1
		checkArray(invokeSort());

		// 2
		checkArray(invokeSort(0), 0);

		// 3
		checkArray(invokeSort(2, 1, 3), 1, 2, 3);

		// 4
		checkArray(invokeSort(7, 1, 5, 6, 5, 8, 4, 0, 2, 9, 2), 0, 1, 2, 2, 4, 5, 5, 6, 7, 8, 9);

		// 5 - Jetzt muss auch 53 vor 58 sein
		checkArray(invokeSort(74, 11, 58, 64, 53, 80, 41, 9), 9, 11, 41, 53, 58, 64, 74, 80);

		// 6
		checkArray(invokeSort(123, 0, 42, 12, 1024, 83, 2, 200), 0, 2, 12, 42, 83, 123, 200, 1024);

		Random r = new Random(1);

		Integer[] a = r.ints(100, 0, 1_000_000).boxed().toArray(Integer[]::new);
		Integer[] expected = a.clone();

		RadixSort.sort(a, false);
		Arrays.sort(expected);

		// 7
		checkArray(a, expected);
	}

	static Integer[] ints(int length) {
		return new Integer[length];
	}

	static int invokeKey(Integer i, int dec) {
		try {
			Method m = RadixSort.class.getDeclaredMethod("key", Integer.class, Integer.TYPE);
			m.setAccessible(true);
			return (Integer) m.invoke(null, i, dec);
		} catch (InvocationTargetException e) {
			throw (RuntimeException) e.getCause();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("RadixSort key method error", e);
		}
	}

	static Integer[] invokeConcatenate(ArrayList<Integer>[] buckets, Integer[] elements) {
		try {
			Method m = RadixSort.class.getDeclaredMethod("concatenate", ArrayList[].class, Integer[].class);
			m.setAccessible(true);
			m.invoke(null, buckets, elements);
			return elements;
		} catch (InvocationTargetException e) {
			throw (RuntimeException) e.getCause();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("RadixSort concatenate method error", e);
		}
	}

	static int invokeGetMaxDecimalPlaces(Integer... elements) {
		try {
			Method m = RadixSort.class.getDeclaredMethod("getMaxDecimalPlaces", Integer[].class);
			m.setAccessible(true);
			return (Integer) m.invoke(null, new Object[] { elements });
		} catch (InvocationTargetException e) {
			throw (RuntimeException) e.getCause();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("RadixSort getMaxDecimalPlaces method error", e);
		}
	}

	static Integer[] invokeKSort(int decimal, Integer... vals) {
		try {
			Method m = RadixSort.class.getDeclaredMethod("kSort", Integer[].class, Integer.TYPE);
			m.setAccessible(true);
			m.invoke(null, vals, decimal);
			return vals;
		} catch (InvocationTargetException e) {
			throw (RuntimeException) e.getCause();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("RadixSort kSort method error", e);
		}
	}

	static Integer[] invokeSort(Integer... vals) {
		RadixSort.sort(vals, false);
		return vals;
	}

	private static void assertThrows(Runnable r, Class<IllegalArgumentException> class1) {
		String testId = getNextTestId();
		try {
			r.run();
			System.err.format("  test %s failed: expected %s but nothing was thrown%n", testId, class1.getName());
		} catch (Exception e) {
			if (class1.isInstance(e))
				System.out.format("  passed %s, result: %s%n", testId, e);
			else
				System.err.format("  test %s failed: expected %s but was %s%n", testId, class1.getName(), e);
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
	 * Compares all of the arrays content with the given sequence
	 * 
	 * @param array    the int array to check
	 * @param contents the array (varargs) that is expected
	 */
	private static void checkArray(Integer[] array, Integer... contents) {
		String testId = getNextTestId();
		if (array.length != contents.length) {
			System.err.format("  test %s failed: expected length %d array but was %d%n", testId, contents.length,
					array.length);
			return;
		}
		List<Integer> differences = new ArrayList<>();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i].intValue() != array[i].intValue())
				differences.add(i);
		}
		if (differences.isEmpty())
			System.out.format("  passed %s, result: %s%n", testId, Arrays.toString(array));
		else {
			System.err.format("  test %s failed: array content differs at indices %s%n", testId, differences);
			System.err.format("     expected: %s%n", Arrays.toString(contents));
			System.err.format("     actual:   %s%n", Arrays.toString(array));
		}
	}

	private static String getNextTestId() {
		return String.format("%s.%s", testMethod, testNum.incrementAndGet());
	}

	private static void printTestHeadline(String s) {
		System.out.format("Test %s: %s%n", testMethod.incrementAndGet(), s);
		testNum.reset();
	}

	static class BucketArray {
		private final ArrayList<ArrayList<Integer>> bucketList;

		private BucketArray() {
			bucketList = new ArrayList<>();
		}

		public static BucketArray of(Integer... vals) {
			return new BucketArray().and(vals);
		}

		public BucketArray and(Integer... vals) {
			bucketList.add(new ArrayList<>(Arrays.asList(vals)));
			return this;
		}

		public ArrayList<Integer>[] build() {
			return bucketList.toArray(new ArrayList[0]);
		}

		public int count() {
			return bucketList.stream().mapToInt(List::size).sum();
		}
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
					.invoke(null, "blatt07", RadixSortTest.class, VERSION);
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Öffentliche Testfälle von Christian Femers. Siehe
 * https://github.com/MaisiKoleni/GAD-Extras
 * 
 * Bitte nicht mit abgeben. (geht sonst nicht)
 * 
 * @version 1.4
 * 
 * @since 1.1 fixed test
 * @since 1.2 added test case id to identify single tests better
 * @since 1.3 some style improvements
 * 
 * @author Christian Femers (IN.TUM)
 *
 */
public class DynamicArrayTest {

	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(0);

	private static final int[] EMPTY = new int[0];

	static {
		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		// Test 1, 2
		testGrowthMinSize();

		// Test 3, 4
		testGrowthInterval();

		// Test 5
		testSetAndReorderElements();
	}

	/**
	 * Test length increase/decrease using the minSize
	 */
	public static void testGrowthMinSize() {
		printTestHeadline("TEST INCREASE minSize"); // Test 1
		DynamicArray da = new DynamicArray(2, 4);
		Interval i;

		// 1
		checkDynArray(da, EMPTY);

		// 2, 3
		i = da.reportUsage(of(), 1); // minSize * growth factor = 1 * 2
		checkDynArray(da, new int[2]);
		assertIntervalEquals(i, of());

		// 4, 5
		i = da.reportUsage(of(), 2);
		checkDynArray(da, new int[2]);
		assertIntervalEquals(i, of());

		// 6, 7
		i = da.reportUsage(of(), 3);
		checkDynArray(da, new int[6]); // minSize * growth factor = 3 * 2
		assertIntervalEquals(i, of());

		// 6, 9
		i = da.reportUsage(of(), 4);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of());

		// A, B
		i = da.reportUsage(of(), 5);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of());

		// C, D
		i = da.reportUsage(of(), 6);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of());

		// E, F
		i = da.reportUsage(of(), 7);
		checkDynArray(da, new int[14]); // minSize * growth factor = 7 * 2
		assertIntervalEquals(i, of());

		printTestHeadline("TEST DECREASE minSize"); // Test 2

		// 1, 2
		i = da.reportUsage(of(), 3);
		checkDynArray(da, new int[6]); // minSize * growth factor = 3 * 2
		assertIntervalEquals(i, of());

		// 3, 4
		i = da.reportUsage(of(), 1);
		checkDynArray(da, new int[2]); // minSize * growth factor = 1 * 2
		assertIntervalEquals(i, of());

		// 5, 6
		i = da.reportUsage(of(), 0);
		checkDynArray(da, EMPTY); // minSize * growth factor = 0 * 2
		assertIntervalEquals(i, of());
	}

	/**
	 * Test length increase/decrease using the intervals (and minSize)
	 */
	public static void testGrowthInterval() {
		printTestHeadline("TEST INCREASE with Interval"); // Test 3
		DynamicArray da = new DynamicArray(2, 4);
		Interval i;

		// 1
		checkDynArray(da, EMPTY);

		// 2, 3
		i = da.reportUsage(of(), 1); // minSize * growth factor = 1 * 2
		checkDynArray(da, new int[2]);
		assertIntervalEquals(i, of());

		// 4, 5
		i = da.reportUsage(of(0, 1), 0);
		checkDynArray(da, new int[2]);
		assertIntervalEquals(i, of(0, 1)); // keeping Interval, no size change

		// 6, 7
		i = da.reportUsage(of(1, 0), 3);
		checkDynArray(da, new int[6]); // minSize * growth factor = 3 * 2
		assertIntervalEquals(i, of(0, 1)); // Reordering..

		// 8, 9
		i = da.reportUsage(of(0, 3), 0);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of(0, 3)); // keeping Interval, no size change

		// A, B
		i = da.reportUsage(of(0, 4), 1);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of(0, 4)); // keeping Interval, no size change

		// C, D
		i = da.reportUsage(of(3, 2), 6);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of(3, 2)); // keeping Interval, no size change

		// E, F
		i = da.reportUsage(of(5, 4), 7);
		checkDynArray(da, new int[14]); // minSize * growth factor = 7 * 2
		assertIntervalEquals(i, of(0, 5)); // Reordering..

		printTestHeadline("TEST DECREASE with Interval"); // Test 4

		// 1, 2
		i = da.reportUsage(of(2, 4), 0);
		checkDynArray(da, new int[6]); // intervalLength * growth factor = 3 * 2
		assertIntervalEquals(i, of(0, 2)); // Reordering..

		// 3, 4
		i = da.reportUsage(of(1, 1), 0);
		checkDynArray(da, new int[2]); // intervalLength * growth factor = 1 * 2
		assertIntervalEquals(i, of(0, 0)); // Reordering..

		// 5, 6
		i = da.reportUsage(of(-1, -1), 0);
		checkDynArray(da, EMPTY); // intervalLength * growth factor = 0 * 2
		assertIntervalEquals(i, of());
	}

	/**
	 * Tests if elements get properly reordered when the size changes
	 */
	public static void testSetAndReorderElements() {
		printTestHeadline("TEST SET/REORDER ELEMENTS"); // Test 5
		DynamicArray da = new DynamicArray(2, 4);
		Interval i;

		// 1
		da.reportUsage(of(), 2);
		checkDynArray(da, new int[4]); // 2 * 2

		// 2, 3
		da.set(0, 1);
		da.set(1, 2);
		da.set(2, 3);
		checkDynArray(da, 1, 2, 3, 0);
		da.set(3, 4);
		checkDynArray(da, 1, 2, 3, 4);

		// 4, 5
		i = da.reportUsage(of(0, 2), 3);
		checkDynArray(da, 1, 2, 3, 4); // no change
		assertIntervalEquals(i, of(0, 2));

		// 6, 7
		i = da.reportUsage(of(0, 3), 5);
		checkDynArray(da, 1, 2, 3, 4, 0, 0, 0, 0, 0, 0); // size increase
		assertIntervalEquals(i, of(0, 3));

		// 8, 9
		i = da.reportUsage(of(1, 2), 0);
		checkDynArray(da, 2, 3, 0, 0); // size decrease, elements reordered
		assertIntervalEquals(i, of(0, 1));

		// A, B
		i = da.reportUsage(of(0, 0), 0);
		checkDynArray(da, 2, 3, 0, 0); // no change
		assertIntervalEquals(i, of(0, 0));

		// C, D
		i = da.reportUsage(of(1, 0), 5); // index order here: 1-2-3-0
		checkDynArray(da, 3, 0, 0, 2, 0, 0, 0, 0, 0, 0); // size increase and reorder
		assertIntervalEquals(i, of(0, 3));

		// E, F
		i = da.reportUsage(of(3, 3), 1);
		checkDynArray(da, 2, 0); // size decrease and reorder
		assertIntervalEquals(i, of(0, 0));

		// G, H
		i = da.reportUsage(of(), 0);
		checkDynArray(da, EMPTY); // make empty
		assertIntervalEquals(i, of());
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
	 * Checks if two Intervals, actual and expected, are equal
	 */
	private static void assertIntervalEquals(Interval actual, Interval expected) {
		String testId = getNextTestId();
		if (actual.isEmpty() != expected.isEmpty())
			System.err.format("  test %s failed: Interval: expected %s but was %s%n", testId, expected, actual);
		else if (actual.isEmpty() || (actual.getTo() == expected.getTo() && actual.getFrom() == expected.getFrom()))
			System.out.format("  passed %s, result Interval: %s%n", testId, actual);
		else
			System.err.format("  test %s failed: Interval: expected %s but was %s%n", testId, expected, actual);
	}

	/**
	 * Creates a new Interval, possibly empty if one of the arguments is negative
	 */
	private static Interval of(int from, int to) {
		if (from < 0 || to < 0)
			return new EmptyInterval();
		return new NonEmptyInterval(from, to);
	}

	/**
	 * Creates a new empty interval
	 */
	private static EmptyInterval of() {
		return new EmptyInterval();
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Öffentliche Testfälle von Christian Femers. Siehe
 * https://github.com/MaisiKoleni/GAD-Extras
 * 
 * Bitte nicht mit abgeben. (geht sonst nicht)
 * 
 * @version 1.1
 * @author Christian Femers (IN.TUM)
 *
 */
public class DynamicArrayTest {

	static {
		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		testGrowthMinSize();
		testGrowthInterval();
		testSetAndReorderElements();
	}

	/**
	 * Test length increase/decrease using the minSize
	 */
	public static void testGrowthMinSize() {
		System.out.println("TEST INCREASE minSize");

		DynamicArray da = new DynamicArray(2, 4);
		Interval i;

		checkDynArray(da, new int[0]);

		i = da.reportUsage(of(), 1); // minSize * growth factor = 1 * 2
		checkDynArray(da, new int[2]);
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(), 2);
		checkDynArray(da, new int[2]);
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(), 3);
		checkDynArray(da, new int[6]); // minSize * growth factor = 3 * 2
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(), 4);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(), 5);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(), 6);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(), 7);
		checkDynArray(da, new int[14]); // minSize * growth factor = 7 * 2
		assertIntervalEquals(i, of());

		System.out.println("TEST DECREASE minSize");
		i = da.reportUsage(of(), 3);
		checkDynArray(da, new int[6]); // minSize * growth factor = 3 * 2
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(), 1);
		checkDynArray(da, new int[2]); // minSize * growth factor = 1 * 2
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(), 0);
		checkDynArray(da, new int[0]); // minSize * growth factor = 0 * 2
		assertIntervalEquals(i, of());
	}

	/**
	 * Test length increase/decrease using the intervals (and minSize)
	 */
	public static void testGrowthInterval() {
		System.out.println("TEST INCREASE with Interval");

		DynamicArray da = new DynamicArray(2, 4);
		Interval i;

		checkDynArray(da, new int[0]);

		i = da.reportUsage(of(), 1); // minSize * growth factor = 1 * 2
		checkDynArray(da, new int[2]);
		assertIntervalEquals(i, of());

		i = da.reportUsage(of(0, 1), 0);
		checkDynArray(da, new int[2]);
		assertIntervalEquals(i, of(0, 1)); // keeping Interval, no size change

		i = da.reportUsage(of(1, 0), 3);
		checkDynArray(da, new int[6]); // minSize * growth factor = 3 * 2
		assertIntervalEquals(i, of(0, 1)); // Reordering..

		i = da.reportUsage(of(0, 3), 0);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of(0, 3)); // keeping Interval, no size change

		i = da.reportUsage(of(0, 4), 1);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of(0, 4)); // keeping Interval, no size change

		i = da.reportUsage(of(3, 2), 6);
		checkDynArray(da, new int[6]);
		assertIntervalEquals(i, of(3, 2)); // keeping Interval, no size change

		i = da.reportUsage(of(5, 4), 7);
		checkDynArray(da, new int[14]); // minSize * growth factor = 7 * 2
		assertIntervalEquals(i, of(0, 5)); // Reordering..

		System.out.println("TEST DECREASE with Interval");
		i = da.reportUsage(of(2, 4), 0);
		checkDynArray(da, new int[6]); // intervalLength * growth factor = 3 * 2
		assertIntervalEquals(i, of(0, 2)); // Reordering..

		i = da.reportUsage(of(1, 1), 0);
		checkDynArray(da, new int[2]); // intervalLength * growth factor = 1 * 2
		assertIntervalEquals(i, of(0, 0)); // Reordering..

		i = da.reportUsage(of(-1, -1), 0);
		checkDynArray(da, new int[0]); // intervalLength * growth factor = 0 * 2
		assertIntervalEquals(i, of());
	}

	/**
	 * Tests if elements get properly reordered when the size changes
	 */
	public static void testSetAndReorderElements() {
		System.out.println("TEST SET/REORDER ELEMENTS");

		DynamicArray da = new DynamicArray(2, 4);
		Interval i;

		da.reportUsage(of(), 2);
		checkDynArray(da, new int[4]); // 2 * 2

		da.set(0, 1);
		da.set(1, 2);
		da.set(2, 3);
		checkDynArray(da, 1, 2, 3, 0);
		da.set(3, 4);
		checkDynArray(da, 1, 2, 3, 4);

		i = da.reportUsage(of(0, 2), 3);
		checkDynArray(da, 1, 2, 3, 4); // no change
		assertIntervalEquals(i, of(0, 2));

		i = da.reportUsage(of(0, 3), 5);
		checkDynArray(da, 1, 2, 3, 4, 0, 0, 0, 0, 0, 0); // size increase
		assertIntervalEquals(i, of(0, 3));

		i = da.reportUsage(of(1, 2), 0);
		checkDynArray(da, 2, 3, 0, 0); // size decrease, elements reordered
		assertIntervalEquals(i, of(0, 1));

		i = da.reportUsage(of(0, 0), 0);
		checkDynArray(da, 2, 3, 0, 0); // no change
		assertIntervalEquals(i, of(0, 0));

		i = da.reportUsage(of(1, 0), 5); // index order here: 1-2-3-0
		checkDynArray(da, 3, 0, 0, 2, 0, 0, 0, 0, 0, 0); // size increase and reorder
		assertIntervalEquals(i, of(0, 3));

		i = da.reportUsage(of(3, 3), 1);
		checkDynArray(da, 2, 0); // size decrease and reorder
		assertIntervalEquals(i, of(0, 0));

		i = da.reportUsage(of(), 0);
		checkDynArray(da, new int[0]); // make empty
		assertIntervalEquals(i, of());
	}

	/**
	 * Compares all of the DynamicArrays inner content with the given sequence
	 * 
	 * @param da       the DynamicArray to check
	 * @param contents the array (varargs) that is expected
	 */
	private static void checkDynArray(DynamicArray da, int... contents) {
		if (da.getInnerLength() != contents.length) {
			System.err.format("  test failed: expected length %d array but was %d%n", contents.length,
					da.getInnerLength());
			return;
		}
		List<Integer> differences = new ArrayList<>();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != da.get(i))
				differences.add(i);
		}
		if (differences.isEmpty())
			System.out.format("  passed, result: %s%n", da);
		else {
			System.err.format("  test failed: array content differs at indices %s%n", differences);
			System.err.format("     expected: %s%n", Arrays.toString(contents));
			System.err.format("     actual:   %s%n", da);
		}
	}

	/**
	 * Checks if two Intervals, actual and expected, are equal
	 */
	private static void assertIntervalEquals(Interval actual, Interval expected) {
		if (actual.isEmpty() != expected.isEmpty())
			System.err.format("  test failed: Interval: expected %s but was %s%n", expected, actual);
		else if (actual.isEmpty() || (actual.getTo() == expected.getTo() && actual.getFrom() == expected.getFrom()))
			System.out.format("  passed, result Interval: %s%n", actual);
		else
			System.err.format("  test failed: Interval: expected %s but was %s%n", expected, actual);
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
}
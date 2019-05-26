import java.util.Arrays;

/**
 * Öffentliche Testfälle von Christian Femers. Siehe
 * https://github.com/MaisiKoleni/GAD-Extras
 * 
 * Bitte nicht mit abgeben. (geht sonst nicht)
 * 
 * @version 1.2
 * 
 * @since 1.1 fixed higher/lower mistake
 * @since 1.2 added test counter and version check
 * 
 * @author Christian Femers (IN.TUM)
 *
 */
public class BinSeaTest {

	private static final String VERSION = "1.2";
	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(0);

	static {
		tryVersionCheck();

		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		test();
	}

	/**
	 * Hilfsmethode, die fast wie ein Aufruf von
	 * BinSea.search(sortedData,value,true) funktioniert.
	 */
	private static int searchLower(int[] sortedData, int value) {
		Interval i = BinSea.search(sortedData, Interval.fromArrayIndices(value, Integer.MAX_VALUE));
		return i instanceof EmptyInterval ? -1 : i.getFrom();
	}

	/**
	 * Hilfsmethode, die fast wie ein Aufruf von
	 * BinSea.search(sortedData,value,false) funktioniert.
	 */
	private static int searchHigher(int[] sortedData, int value) {
		Interval i = BinSea.search(sortedData, Interval.fromArrayIndices(0, value));
		return i instanceof EmptyInterval ? -1 : i.getTo();
	}

	/**
	 * Haupt-Testmethode
	 */
	public static void test() {
		printTestHeadline("EMPTY");
		assertEquals(searchLower(new int[] {}, 5), -1);
		assertEquals(searchHigher(new int[] {}, 5), -1);

		printTestHeadline("LOWER - ONE");
		assertEquals(searchLower(new int[] { 1 }, 5), -1);
		assertEquals(searchLower(new int[] { 5 }, 5), 0);
		assertEquals(searchLower(new int[] { 8 }, 5), 0);

		printTestHeadline("LOWER - TWO");
		assertEquals(searchLower(new int[] { 1, 2 }, 5), -1);
		assertEquals(searchLower(new int[] { 7, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 8 }, 5), 1);

		assertEquals(searchLower(new int[] { 1, 5 }, 5), 1);
		assertEquals(searchLower(new int[] { 5, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 5, 5 }, 5), 0);

		printTestHeadline("LOWER - THREE");
		assertEquals(searchLower(new int[] { 1, 2, 3 }, 5), -1);
		assertEquals(searchLower(new int[] { 6, 7, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 6, 7 }, 5), 1);
		assertEquals(searchLower(new int[] { 1, 3, 7 }, 5), 2);

		assertEquals(searchLower(new int[] { 1, 2, 5 }, 5), 2);
		assertEquals(searchLower(new int[] { 5, 7, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 5, 8 }, 5), 1);
		assertEquals(searchLower(new int[] { 1, 5, 5 }, 5), 1);
		assertEquals(searchLower(new int[] { 5, 5, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 5, 5, 5 }, 5), 0);

		printTestHeadline("LOWER - SEVEN");
		assertEquals(searchLower(new int[] { 1, 2, 2, 3, 4, 4, 4 }, 5), -1);
		assertEquals(searchLower(new int[] { 6, 6, 7, 8, 8, 9, 9 }, 5), 0);

		assertEquals(searchLower(new int[] { 5, 5, 5, 6, 7, 8, 9 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 5, 5, 5 }, 5), 4);
		assertEquals(searchLower(new int[] { 5, 5, 5, 5, 7, 8, 9 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 5, 5, 5 }, 5), 3);

		assertEquals(searchLower(new int[] { 1, 5, 5, 5, 7, 8, 9 }, 5), 1);
		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 5, 5, 9 }, 5), 3);

		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 6, 7, 8 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 2, 5, 5, 5, 7, 8 }, 5), 2);
		assertEquals(searchLower(new int[] { 1, 5, 5, 5, 5, 5, 8 }, 5), 1);
		assertEquals(searchLower(new int[] { 5, 5, 5, 5, 5, 5, 5 }, 5), 0);

		printTestHeadline("LOWER - EIGHT");
		assertEquals(searchLower(new int[] { 1, 2, 2, 3, 3, 4, 4, 4 }, 5), -1);
		assertEquals(searchLower(new int[] { 6, 6, 7, 7, 8, 8, 9, 9 }, 5), 0);

		assertEquals(searchLower(new int[] { 5, 5, 5, 5, 6, 7, 8, 9 }, 5), 0);
		assertEquals(searchLower(new int[] { 5, 5, 5, 6, 6, 7, 8, 9 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 5, 5, 5, 5 }, 5), 4);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 4, 5, 5, 5 }, 5), 5);

		assertEquals(searchLower(new int[] { 1, 5, 5, 5, 6, 7, 8, 9 }, 5), 1);
		assertEquals(searchLower(new int[] { 1, 5, 5, 6, 6, 7, 8, 9 }, 5), 1);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 5, 5, 5, 9 }, 5), 4);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 4, 5, 5, 9 }, 5), 5);

		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 5, 5, 8, 9 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, 5), 4);
		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 6, 6, 7, 8 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 5, 6, 7, 8 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 2, 5, 5, 5, 5, 7, 8 }, 5), 2);
		assertEquals(searchLower(new int[] { 1, 5, 5, 5, 5, 5, 5, 8 }, 5), 1);
		assertEquals(searchLower(new int[] { 5, 5, 5, 5, 5, 5, 5, 5 }, 5), 0);

		printTestHeadline("HIGHER - ONE");
		assertEquals(searchHigher(new int[] { 1 }, 5), 0);
		assertEquals(searchHigher(new int[] { 5 }, 5), 0);
		assertEquals(searchHigher(new int[] { 8 }, 5), -1);

		printTestHeadline("HIGHER - TWO");
		assertEquals(searchHigher(new int[] { 1, 2 }, 5), 1);
		assertEquals(searchHigher(new int[] { 7, 8 }, 5), -1);
		assertEquals(searchHigher(new int[] { 1, 8 }, 5), 0);

		assertEquals(searchHigher(new int[] { 1, 5 }, 5), 1);
		assertEquals(searchHigher(new int[] { 5, 8 }, 5), 0);
		assertEquals(searchHigher(new int[] { 5, 5 }, 5), 1);

		printTestHeadline("HIGHER - THREE");
		assertEquals(searchHigher(new int[] { 1, 2, 3 }, 5), 2);
		assertEquals(searchHigher(new int[] { 6, 7, 8 }, 5), -1);
		assertEquals(searchHigher(new int[] { 1, 6, 7 }, 5), 0);
		assertEquals(searchHigher(new int[] { 1, 3, 7 }, 5), 1);

		assertEquals(searchHigher(new int[] { 1, 2, 5 }, 5), 2);
		assertEquals(searchHigher(new int[] { 5, 7, 8 }, 5), 0);
		assertEquals(searchHigher(new int[] { 1, 5, 8 }, 5), 1);
		assertEquals(searchHigher(new int[] { 1, 5, 5 }, 5), 2);
		assertEquals(searchHigher(new int[] { 5, 5, 8 }, 5), 1);
		assertEquals(searchHigher(new int[] { 5, 5, 5 }, 5), 2);

		printTestHeadline("HIGHER - SEVEN");
		assertEquals(searchHigher(new int[] { 1, 2, 2, 3, 4, 4, 4 }, 5), 6);
		assertEquals(searchHigher(new int[] { 6, 6, 7, 8, 8, 9, 9 }, 5), -1);

		assertEquals(searchHigher(new int[] { 5, 5, 5, 6, 7, 8, 9 }, 5), 2);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 5, 5, 5 }, 5), 6);
		assertEquals(searchHigher(new int[] { 5, 5, 5, 5, 7, 8, 9 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 5, 5, 5 }, 5), 6);

		assertEquals(searchHigher(new int[] { 1, 5, 5, 5, 7, 8, 9 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 5, 5, 9 }, 5), 5);

		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 6, 7, 8 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 2, 5, 5, 5, 7, 8 }, 5), 4);
		assertEquals(searchHigher(new int[] { 1, 5, 5, 5, 5, 5, 8 }, 5), 5);
		assertEquals(searchHigher(new int[] { 5, 5, 5, 5, 5, 5, 5 }, 5), 6);

		printTestHeadline("HIGHER - EIGHT");
		assertEquals(searchHigher(new int[] { 1, 2, 2, 3, 3, 4, 4, 4 }, 5), 7);
		assertEquals(searchHigher(new int[] { 6, 6, 7, 7, 8, 8, 9, 9 }, 5), -1);

		assertEquals(searchHigher(new int[] { 5, 5, 5, 5, 6, 7, 8, 9 }, 5), 3);
		assertEquals(searchHigher(new int[] { 5, 5, 5, 6, 6, 7, 8, 9 }, 5), 2);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 5, 5, 5, 5 }, 5), 7);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 4, 5, 5, 5 }, 5), 7);

		assertEquals(searchHigher(new int[] { 1, 5, 5, 5, 6, 7, 8, 9 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 5, 5, 6, 6, 7, 8, 9 }, 5), 2);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 5, 5, 5, 9 }, 5), 6);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 4, 5, 5, 9 }, 5), 6);

		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 5, 5, 8, 9 }, 5), 5);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, 5), 4);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 6, 6, 7, 8 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 5, 6, 7, 8 }, 5), 4);
		assertEquals(searchHigher(new int[] { 1, 2, 5, 5, 5, 5, 7, 8 }, 5), 5);
		assertEquals(searchHigher(new int[] { 1, 5, 5, 5, 5, 5, 5, 8 }, 5), 6);
		assertEquals(searchHigher(new int[] { 5, 5, 5, 5, 5, 5, 5, 5 }, 5), 7);

		printTestHeadline("HIGHER/LOWER - 10.000.000");
		int[] a = gen(10_000_000);
		assertEquals(searchHigher(a, 1_314_155), Arrays.binarySearch(a, 1_314_155));
		assertEquals(searchLower(a, 1_314_155), Arrays.binarySearch(a, 1_314_155));
		assertEquals(searchHigher(a, 485_002_327), Arrays.binarySearch(a, 485_002_327));
		assertEquals(searchLower(a, 485_002_327), Arrays.binarySearch(a, 485_002_327));
		assertEquals(searchHigher(a, 968_686_425), Arrays.binarySearch(a, 968_686_425));
		assertEquals(searchLower(a, 968_686_425), Arrays.binarySearch(a, 968_686_425));
	}

	/**
	 * Generiert irgendeine erfundene Sequenz, die streng monoton steigt. Bei size
	 * >= 22_139_005 entstehen Overflows, d.h. Zahlen werden plötzlich negativ.
	 */
	private static int[] gen(int size) {
		int[] a = new int[size];
		a[0] = 1;
		for (int i = 1; i < a.length; i++) {
			int prev = a[i - 1];
			a[i] = prev++ + prev % (i % (prev + 1) + 1);
		}
		return a;
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
					.invoke(null, "blatt02", BinSeaTest.class, VERSION);
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

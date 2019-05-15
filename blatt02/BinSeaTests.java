import java.util.Arrays;

/**
 * Öffentliche Testfälle von Christian Femers. 
 * Siehe https://github.com/MaisiKoleni/GAD-Extras
 * 
 * Bitte nicht mit abgeben. (geht sonst nicht)
 * 
 * @author Christian Femers (IN.TUM)
 *
 */
public class BinSeaTests {

	static {
		System.err.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		test();
	}

	/**
	 * Hilfsmethode, die fast wie ein Aufruf von BinSea.search(sortedData,value,true) funktioniert.
	 */
	private static int searchLower(int[] sortedData, int value) {
		Interval i = BinSea.search(sortedData, Interval.fromArrayIndices(value, Integer.MAX_VALUE));
		return i instanceof EmptyInterval ? -1 : i.getFrom();
	}

	/**
	 * Hilfsmethode, die fast wie ein Aufruf von BinSea.search(sortedData,value,false) funktioniert.
	 */
	private static int searchHigher(int[] sortedData, int value) {
		Interval i = BinSea.search(sortedData, Interval.fromArrayIndices(0, value));
		return i instanceof EmptyInterval ? -1 : i.getTo();
	}

	/**
	 * Haupt-Testmethode
	 */
	public static void test() {
		System.out.println("EMPTY");
		assertEquals(searchLower(new int[] {}, 5), -1);
		assertEquals(searchHigher(new int[] {}, 5), -1);

		System.out.println("LOWER - ONE");
		assertEquals(searchLower(new int[] { 1 }, 5), -1);
		assertEquals(searchLower(new int[] { 5 }, 5), 0);
		assertEquals(searchLower(new int[] { 8 }, 5), 0);

		System.out.println("LOWER - TWO");
		assertEquals(searchLower(new int[] { 1, 2 }, 5), -1);
		assertEquals(searchLower(new int[] { 7, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 8 }, 5), 1);

		assertEquals(searchLower(new int[] { 1, 5 }, 5), 1);
		assertEquals(searchLower(new int[] { 5, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 5, 5 }, 5), 1);

		System.out.println("LOWER - THREE");
		assertEquals(searchLower(new int[] { 1, 2, 3 }, 5), -1);
		assertEquals(searchLower(new int[] { 6, 7, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 6, 7 }, 5), 1);
		assertEquals(searchLower(new int[] { 1, 3, 7 }, 5), 2);

		assertEquals(searchLower(new int[] { 1, 2, 5 }, 5), 2);
		assertEquals(searchLower(new int[] { 5, 7, 8 }, 5), 0);
		assertEquals(searchLower(new int[] { 1, 5, 8 }, 5), 1);
		assertEquals(searchLower(new int[] { 1, 5, 5 }, 5), 2);
		assertEquals(searchLower(new int[] { 5, 5, 8 }, 5), 1);
		assertEquals(searchLower(new int[] { 5, 5, 5 }, 5), 2);

		System.out.println("LOWER - SEVEN");
		assertEquals(searchLower(new int[] { 1, 2, 2, 3, 4, 4, 4 }, 5), -1);
		assertEquals(searchLower(new int[] { 6, 6, 7, 8, 8, 9, 9 }, 5), 0);

		assertEquals(searchLower(new int[] { 5, 5, 5, 6, 7, 8, 9 }, 5), 2);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 5, 5, 5 }, 5), 6);
		assertEquals(searchLower(new int[] { 5, 5, 5, 5, 7, 8, 9 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 5, 5, 5 }, 5), 6);

		assertEquals(searchLower(new int[] { 1, 5, 5, 5, 7, 8, 9 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 5, 5, 9 }, 5), 5);

		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 6, 7, 8 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 2, 5, 5, 5, 7, 8 }, 5), 4);
		assertEquals(searchLower(new int[] { 1, 5, 5, 5, 5, 5, 8 }, 5), 5);
		assertEquals(searchLower(new int[] { 5, 5, 5, 5, 5, 5, 5 }, 5), 6);

		System.out.println("LOWER - EIGHT");
		assertEquals(searchLower(new int[] { 1, 2, 2, 3, 3, 4, 4, 4 }, 5), -1);
		assertEquals(searchLower(new int[] { 6, 6, 7, 7, 8, 8, 9, 9 }, 5), 0);

		assertEquals(searchLower(new int[] { 5, 5, 5, 5, 6, 7, 8, 9 }, 5), 3);
		assertEquals(searchLower(new int[] { 5, 5, 5, 6, 6, 7, 8, 9 }, 5), 2);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 5, 5, 5, 5 }, 5), 7);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 4, 5, 5, 5 }, 5), 7);

		assertEquals(searchLower(new int[] { 1, 5, 5, 5, 6, 7, 8, 9 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 5, 5, 6, 6, 7, 8, 9 }, 5), 2);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 5, 5, 5, 9 }, 5), 6);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 4, 5, 5, 9 }, 5), 6);

		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 5, 5, 8, 9 }, 5), 5);
		assertEquals(searchLower(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, 5), 4);
		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 6, 6, 7, 8 }, 5), 3);
		assertEquals(searchLower(new int[] { 1, 2, 3, 5, 5, 6, 7, 8 }, 5), 4);
		assertEquals(searchLower(new int[] { 1, 2, 5, 5, 5, 5, 7, 8 }, 5), 5);
		assertEquals(searchLower(new int[] { 1, 5, 5, 5, 5, 5, 5, 8 }, 5), 6);
		assertEquals(searchLower(new int[] { 5, 5, 5, 5, 5, 5, 5, 5 }, 5), 7);

		System.out.println("HIGHER - ONE");
		assertEquals(searchHigher(new int[] { 1 }, 5), 0);
		assertEquals(searchHigher(new int[] { 5 }, 5), 0);
		assertEquals(searchHigher(new int[] { 8 }, 5), -1);

		System.out.println("HIGHER - TWO");
		assertEquals(searchHigher(new int[] { 1, 2 }, 5), 1);
		assertEquals(searchHigher(new int[] { 7, 8 }, 5), -1);
		assertEquals(searchHigher(new int[] { 1, 8 }, 5), 0);

		assertEquals(searchHigher(new int[] { 1, 5 }, 5), 1);
		assertEquals(searchHigher(new int[] { 5, 8 }, 5), 0);
		assertEquals(searchHigher(new int[] { 5, 5 }, 5), 0);

		System.out.println("HIGHER - THREE");
		assertEquals(searchHigher(new int[] { 1, 2, 3 }, 5), 2);
		assertEquals(searchHigher(new int[] { 6, 7, 8 }, 5), -1);
		assertEquals(searchHigher(new int[] { 1, 6, 7 }, 5), 0);
		assertEquals(searchHigher(new int[] { 1, 3, 7 }, 5), 1);

		assertEquals(searchHigher(new int[] { 1, 2, 5 }, 5), 2);
		assertEquals(searchHigher(new int[] { 5, 7, 8 }, 5), 0);
		assertEquals(searchHigher(new int[] { 1, 5, 8 }, 5), 1);
		assertEquals(searchHigher(new int[] { 1, 5, 5 }, 5), 1);
		assertEquals(searchHigher(new int[] { 5, 5, 8 }, 5), 0);
		assertEquals(searchHigher(new int[] { 5, 5, 5 }, 5), 0);

		System.out.println("HIGHER - SEVEN");
		assertEquals(searchHigher(new int[] { 1, 2, 2, 3, 4, 4, 4 }, 5), 6);
		assertEquals(searchHigher(new int[] { 6, 6, 7, 8, 8, 9, 9 }, 5), -1);

		assertEquals(searchHigher(new int[] { 5, 5, 5, 6, 7, 8, 9 }, 5), 0);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 5, 5, 5 }, 5), 4);
		assertEquals(searchHigher(new int[] { 5, 5, 5, 5, 7, 8, 9 }, 5), 0);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 5, 5, 5 }, 5), 3);

		assertEquals(searchHigher(new int[] { 1, 5, 5, 5, 7, 8, 9 }, 5), 1);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 5, 5, 9 }, 5), 3);

		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 6, 7, 8 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 2, 5, 5, 5, 7, 8 }, 5), 2);
		assertEquals(searchHigher(new int[] { 1, 5, 5, 5, 5, 5, 8 }, 5), 1);
		assertEquals(searchHigher(new int[] { 5, 5, 5, 5, 5, 5, 5 }, 5), 0);

		System.out.println("HIGHER - EIGHT");
		assertEquals(searchHigher(new int[] { 1, 2, 2, 3, 3, 4, 4, 4 }, 5), 7);
		assertEquals(searchHigher(new int[] { 6, 6, 7, 7, 8, 8, 9, 9 }, 5), -1);

		assertEquals(searchHigher(new int[] { 5, 5, 5, 5, 6, 7, 8, 9 }, 5), 0);
		assertEquals(searchHigher(new int[] { 5, 5, 5, 6, 6, 7, 8, 9 }, 5), 0);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 5, 5, 5, 5 }, 5), 4);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 4, 5, 5, 5 }, 5), 5);

		assertEquals(searchHigher(new int[] { 1, 5, 5, 5, 6, 7, 8, 9 }, 5), 1);
		assertEquals(searchHigher(new int[] { 1, 5, 5, 6, 6, 7, 8, 9 }, 5), 1);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 5, 5, 5, 9 }, 5), 4);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 4, 5, 5, 9 }, 5), 5);

		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 5, 5, 8, 9 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, 5), 4);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 6, 6, 7, 8 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 2, 3, 5, 5, 6, 7, 8 }, 5), 3);
		assertEquals(searchHigher(new int[] { 1, 2, 5, 5, 5, 5, 7, 8 }, 5), 2);
		assertEquals(searchHigher(new int[] { 1, 5, 5, 5, 5, 5, 5, 8 }, 5), 1);
		assertEquals(searchHigher(new int[] { 5, 5, 5, 5, 5, 5, 5, 5 }, 5), 0);

		System.out.println("HIGHER/LOWER - 10.000.000");
		int[] a = gen(10_000_000);
		assertEquals(searchHigher(a, 1_314_155), Arrays.binarySearch(a, 1_314_155));
		assertEquals(searchLower(a, 1_314_155), Arrays.binarySearch(a, 1_314_155));
		assertEquals(searchHigher(a, 485_002_327), Arrays.binarySearch(a, 485_002_327));
		assertEquals(searchLower(a, 485_002_327), Arrays.binarySearch(a, 485_002_327));
		assertEquals(searchHigher(a, 968_686_425), Arrays.binarySearch(a, 968_686_425));
		assertEquals(searchLower(a, 968_686_425), Arrays.binarySearch(a, 968_686_425));
	}

	/**
	 * Generiert irgendeine erfundene Sequenz, die streng monoton steigt. 
	 * Bei size >= 22_139_005 entstehen Overflows, d.h. Zahlen werden plötzlich negativ.
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
	 * Simple assert-Methode zum Vergleichen von ints.
	 */
	private static void assertEquals(int actual, int expected) {
		if (actual == expected)
			System.out.format("  passed, result: %d\n", actual);
		else
			System.err.format("  test failed: expected %d but was %d\n", expected, actual);
	}
}
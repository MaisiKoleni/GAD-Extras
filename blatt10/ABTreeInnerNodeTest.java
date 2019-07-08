import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
public class ABTreeInnerNodeTest {

	private static final String VERSION = "1.0";

	private static Counter testNum = new Counter(0);
	private static Counter testMethod = new Counter(0);

	static {
		tryVersionCheck();

		System.out.println("CF's TESTS ACTIVE\n");
	}

	public static void main(String[] args) {
		// Test 1, 2
		testValidTrees();
		// Test 3, 4
		testInvalidTrees();
	}

	/**
	 * Test some non-restructuring methods first
	 */
	public static void testValidTrees() {
		printTestHeadline("VALID TREE TEST 1"); // Test 1

		ABTreeNode root = TreeBuilder.ofAB(2, 3).root(Sub.of(
				Sub.of(1, 2), 
				5, 
				Sub.of(6, 8))).build();

		// 1
		assertEquals(root.height(), 2);

		// 2, 3, 4, 5, 6
		assertTrue(root.find(5));
		assertTrue(root.find(1));
		assertTrue(root.find(2));
		assertTrue(root.find(6));
		assertTrue(root.find(8));

		// 7, 8, 9, A
		assertFalse(root.find(7));
		assertFalse(root.find(0));
		assertFalse(root.find(-1));
		assertFalse(root.find(10));

		// B
		assertTrue(root.validAB(true));

		// C
		assertFalse(root.canSteal());

		// D, E
		assertEquals(root.min(), 1);
		assertEquals(root.max(), 8);


		printTestHeadline("VALID TREE TEST 2"); // Test 2

		root = TreeBuilder.ofAB(2, 4).root(Sub.of(
				Sub.of(
						Sub.of(0), 
						1, 
						Sub.of(3, 4)), 
				5,
				Sub.of(
						Sub.of(6, 7), 
						8, 
						Sub.of(9)), 
				10, 
				Sub.of(
						Sub.of(11, 13), 
						14, 
						Sub.of(15, 18))
				)).build();

		// 1
		assertEquals(root.height(), 3);

		// 2, 3, 4, 5, 6
		assertTrue(root.find(10));
		assertTrue(root.find(0));
		assertTrue(root.find(1));
		assertTrue(root.find(8));
		assertTrue(root.find(18));

		// 7, 8, 9, A
		assertFalse(root.find(2));
		assertFalse(root.find(16));
		assertFalse(root.find(-1));
		assertFalse(root.find(12));

		// B
		assertTrue(root.validAB(true));

		// C
		assertTrue(root.canSteal());

		// D, E
		assertEquals(root.min(), 0);
		assertEquals(root.max(), 18);
	}

	/**
	 * Test the validAB method against invalid trees.
	 */
	public static void testInvalidTrees() {
		printTestHeadline("INVALID TREE TEST 1"); // Test 3

		ABTreeNode root = TreeBuilder.ofAB(2, 3).root(Sub.of(
				Sub.of(1, 2), 
				5, 
				Sub.of(8, 6))).build();

		// 1
		assertFalse(root.validAB(true));


		printTestHeadline("INVALID TREE TEST 2"); // Test 4

		root = TreeBuilder.ofAB(2, 4).root(Sub.of(
				Sub.of(
						Sub.of(0), 
						1, 
						Sub.of(3, 4)), 
				5,
				Sub.of(
						Sub.of(6, 7), 
						8, 
						Sub.of(9)), 
				10, 
				Sub.of(
						12, 
						14, 
						15)
				)).build();

		// 1
		assertFalse(root.validAB(true));
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
	 * Simple assert-method to evaluate booleans.
	 */
	private static void assertTrue(boolean test) {
		String testId = getNextTestId();
		if (test)
			System.out.format("  passed %s, result: true%n", testId);
		else
			System.err.format("  test %s failed: expected true but was false%n", testId);
	}

	/**
	 * Simple assert-method to evaluate booleans.
	 */
	private static void assertFalse(boolean test) {
		String testId = getNextTestId();
		if (!test)
			System.out.format("  passed %s, result: false%n", testId);
		else
			System.err.format("  test %s failed: expected false but was true%n", testId);
	}

	private static String getNextTestId() {
		return String.format("%s.%s", testMethod, testNum.incrementAndGet());
	}

	private static void printTestHeadline(String s) {
		System.out.format("Test %s: %s%n", testMethod.incrementAndGet(), s);
		testNum.reset();
	}

	public static class TreeBuilder {
		int a;
		int b;
		private Sub root;

		public TreeBuilder(int a, int b) {
			if (b < 2 * a - 1)
				throw new IllegalArgumentException("b must be less than 2a-1");
			this.a = a;
			this.b = b;
		}

		public TreeBuilder root(Sub root) {
			if (this.root != null)
				throw new IllegalStateException("cannot have two roots");
			this.root = Objects.requireNonNull(root, "root must not be null");
			return this;
		}

		public ABTreeInnerNode build() {
			return root.build(this);
		}

		static TreeBuilder ofAB(int a, int b) {
			return new TreeBuilder(a, b);
		}
	}

	public static class Sub {
		private final ArrayList<Integer> keys;
		private final List<Sub> subs;

		Sub(List<Integer> keys, List<Sub> subs) {
			this.keys = new ArrayList<>(keys);
			this.subs = subs;
		}

		ABTreeInnerNode build(TreeBuilder tb) {
			if (keys.size() + 1 < tb.a || keys.size() >= tb.b)
				throw new IllegalStateException("childern/keys out of (a,b)-bounds");
			if (subs.isEmpty())
				return new ABTreeInnerNode(keys, createLeaves(keys.size() + 1, tb.a, tb.b), tb.a, tb.b);
			return new ABTreeInnerNode(keys,
					subs.stream().map(sub -> sub.build(tb)).collect(Collectors.toCollection(ArrayList::new)), tb.a,
					tb.b);
		}

		public static Sub of(Integer... keys) {
			return new Sub(List.of(keys), List.of());
		}

		public static Sub of(Sub a, Integer key1, Sub b) {
			return new Sub(List.of(key1), List.of(a, b));
		}

		public static Sub of(Sub a, Integer key1, Sub b, Integer key2, Sub c) {
			return new Sub(List.of(key1, key2), List.of(a, b, c));
		}

		public static Sub of(Sub a, Integer key1, Sub b, Integer key2, Sub c, Integer key3, Sub d) {
			return new Sub(List.of(key1, key2, key3), List.of(a, b, c, d));
		}

		static ArrayList<ABTreeNode> createLeaves(int num, int a, int b) {
			return IntStream.range(0, num).mapToObj(i -> new ABTreeLeaf(a, b))
					.collect(Collectors.toCollection(ArrayList::new));
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
					.invoke(null, "blatt10", ABTreeInnerNodeTest.class, VERSION);
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

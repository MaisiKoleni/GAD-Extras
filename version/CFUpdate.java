import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.prefs.Preferences;

/**
 * Öffentliche Testfälle von Christian Femers. Siehe
 * https://github.com/MaisiKoleni/GAD-Extras
 * <p>
 * This class is for test update checks only. DO NOT SUBMIT THIS CLASS.
 * <p>
 * Place this class next to the test classes to enable automatic version checks
 * 
 * @version 1.0
 *
 * @author Christian Femers
 */
public class CFUpdate {

	/**
	 * Automatically checks for new versions. This should help to communicate
	 * problems with the tests themselves and when new or extended tests are
	 * available. Checks are done only every 30 minutes (if run, of course).
	 */
	static void checkForNewVersion(String blatt, Class<?> testClass, String currentVersion) {
		String testName = testClass.getSimpleName();
		if (!testName.endsWith("Test"))
			throw new IllegalArgumentException("Please pass a valid test class. Found: " + testName);
		String key = blatt + "." + testName + "_last_update_check";
		String url = "https://raw.githubusercontent.com/MaisiKoleni/GAD-Extras/master/" + blatt + "/" + testName
				+ ".java";
		// prevent checking every single run [user-registry > Software > JavaSoft > ..]
		String in = Preferences.userRoot().node("christian_femers_tum").node("gad_extras").get(key, "0");
		if (in.matches("\\d{1,19}") && Long.parseLong(in) < System.currentTimeMillis() / 1_800_000) {
			new Thread(() -> {
				// get the raw code from GitHub
				try (InputStream is = new URL(url).openStream()) {
					String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
					// search for version in the code and compare both
					String v = content.replaceFirst("(?s).* @version (\\S+).*", "$1");
					if (v.compareTo(currentVersion) > 0) {
						// print that a newer version is there and wait 10 seconds for input
						System.err.format("%n>>> There is a newer Version (%s) available under <<<%n%s%n%n", v, url);
						try (Scanner sc = new Scanner(System.in)) {
							System.out.println("Print the new source code now? y/n (10 sec)");
							for (int i = 0; i < 200 && System.in.available() == 0; i++) {
								try {
									Thread.sleep(50);
								} catch (@SuppressWarnings("unused") InterruptedException e) {
									// should not happen
									Thread.currentThread().interrupt();
								}
							}
							// print code if typed "y" or "Y"
							if (System.in.available() != 0 && sc.nextLine().equalsIgnoreCase("y"))
								System.out.println(content);
							else
								System.out.println("Exiting...");
						}
					}
				} catch (IOException e) {
					System.err.println("Cannot reach update URL (no internet connection? renamed something?)");
					e.printStackTrace();
				}
				// store the current time again
				Preferences.userRoot().node("christian_femers_tum").node("gad_extras").putLong(key,
						System.currentTimeMillis() / 1_800_000);
			}).start();
		}
	}

	public static void main(String[] args) {
		System.out.println("Please do not upload CFUpdate.java");
	}
}

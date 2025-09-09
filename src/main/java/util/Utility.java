package util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v128.network.Network;
import org.openqa.selenium.devtools.v128.network.model.ConnectionType;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Utility {

    static String loginMail;
    static String loginPassword;
    private static int callCount = 0; // track calls
    public static List<String> menuItems;
    private static Map<String, String> dataMap = new HashMap<>();

    public static String getLoginMail() {
        return loginMail;
    }
    public static String getLoginPassword() {
        return loginPassword;
    }

    /**
     * This method generate name (firstName and lastName)
     * with a criteria in general name should be from 20 to 30 capital characters
     * @return name
     */
    public static String generateRandomName() {
        Random random = new Random();
        int length = 20 + random.nextInt(11); // Random length between 20 and 30
        StringBuilder name = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char randomChar = (char) ('A' + random.nextInt(26)); // 'A' to 'Z'
            name.append(randomChar);
        }
        return name.toString();
    }

    /**
     * This method generate email in gmail domain
     *
     * @return email
     */
    public static String generateRandomGmail() {
        Random random = new Random();
        int length = 10 + random.nextInt(6); // Length between 10 and 15
        StringBuilder localPart = new StringBuilder(length);

        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            localPart.append(randomChar);
        }
        loginMail = localPart.toString() + "@gmail.com";
        return localPart.toString() + "@gmail.com";
    }

    /**
     * Here’s a Java method that generates a secure random password with:
     * Uppercase letters
     * Lowercase letters
     * Digits
     * Special characters
     * A random length between 20 and 30 ex. <;)=@@5@nrs*FrPSu@@GW>
     *
     * @return password
     */
    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();

        // Character sets
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?";

        // All characters combined
        String allChars = upperCase + lowerCase + digits + specialChars;

        // Random password length between 20 and 30
        int length = 20 + random.nextInt(11);

        List<Character> passwordChars = new ArrayList<>();

        // Ensure at least one of each type
        passwordChars.add(upperCase.charAt(random.nextInt(upperCase.length())));
        passwordChars.add(lowerCase.charAt(random.nextInt(lowerCase.length())));
        passwordChars.add(digits.charAt(random.nextInt(digits.length())));
        passwordChars.add(specialChars.charAt(random.nextInt(specialChars.length())));

        // Fill the rest of the password
        for (int i = 4; i < length; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle to avoid predictable pattern
        Collections.shuffle(passwordChars, random);

        // Convert to string
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }
        loginPassword = password.toString();
        return password.toString();
    }

    public static String getRandomCurrency() {
        String[] currencies = {"€Euro", "£Pound Sterling", "$US Dollar"};
        Random random = new Random();
        int index = random.nextInt(currencies.length);
        return currencies[index];
    }
    public static String getRandomCategory(){
        String[] Category = {
                "Desktops",
                "Laptops & Notebooks",
                "Components",
                "MP3 Players"
        };

        Random random3 = new Random();
        int index_3 = random3.nextInt(Category.length);
        return Category[index_3];
    }

    public static String getRandomSupCategory(String Cat) {

        switch (Cat) {
            case "Desktops":
                String[] supCategory1 = {
                        "PC (0)",
                        "Mac (1)"
                };

                Random random1 = new Random();
                int index_1 = random1.nextInt(supCategory1.length);
                return supCategory1[index_1];
            case "Laptops & Notebooks":
                String[] supCategory2 = {
                        "Macs (0)",
                        "Windows (0)"
                };

                Random random2 = new Random();
                int index_2 = random2.nextInt(supCategory2.length);
                return supCategory2[index_2];
            case "Components":
                String[] supCategory3 = {
                        "Mice and Trackballs (0)",
                        "Monitors (2)",
                        "Scanners (0)",
                        "Web Cameras (0)"
                };

                Random random3 = new Random();
                int index_3 = random3.nextInt(supCategory3.length);
                return supCategory3[index_3];

            case "MP3 Players":
                String[] supCategory9 = {
                        "test 7 (0)",
                        "test 8 (0)",
                        "test 9 (0)",
                        "test 10 (0)"
                };

                Random random9 = new Random();
                int index_9 = random9.nextInt(supCategory9.length);
                return supCategory9[index_9];

        }
        return "Can't Find Cat !";
    }
    public static String removeParenthesesAndNumbers(String input) {
        // Remove parentheses and digits
        return input.replaceAll("[()\\d]", "").trim();
    }
    public static void openBrowserNetworkTab() throws AWTException {
        // Create Robot instance
        Robot robot = new Robot();
        // Add a delay for setup (optional)
        robot.delay(2000); // Wait for the browser window to be active
        // Step 1: Press Ctrl+Shift+I to open Developer Tools
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_I);
        // Add a delay for Developer Tools to open
        robot.delay(1000);
        // release press buttons
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_I);
        // Step 2: Navigate to the Network tab
        // Use Right Arrow key multiple times to move to the Network tab
        for (int i = 0; i < 3; i++) {
            // Press and hold Ctrl
            robot.keyPress(KeyEvent.VK_CONTROL);
            // Press and release
            robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);
            robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
            // Release Ctrl
            robot.keyRelease(KeyEvent.VK_CONTROL);
            // Add a small delay between presses
            robot.delay(200);
        }
    }
    public static synchronized void writeToCSV(ArrayList<String[]> list) {
        FileWriter writer = null;
        try {
            callCount++;

            // Generate timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            // Decide prefix based on call number
            String prefix;
            if (callCount == 1) {
                prefix = "Data_Extracted_SwagLabs_";
            } else if (callCount == 2) {
                prefix = "Data_Extracted_TutorialsNinja_";
            } else {
                prefix = "Data_Extracted_Other_"; // fallback for 3rd+ calls
            }

            String filePath = "src/test/java/OutputData/" + prefix + timestamp + ".csv";
            writer = new FileWriter(filePath, false); // new file each call

            // First row: Date and Time
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.append("Date and Time: ,\"").append(now).append("\"\n");

            // Add header row
            writer.append("Name,Description,Price\n");

            double totalPrice = 0.0;

            // Write rows
            for (String[] row : list) {
                for (int i = 0; i < row.length; i++) {
                    String value = row[i].replace("\"", "\"\""); // escape quotes
                    writer.append("\"").append(value).append("\"");

                    if (i < row.length - 1) {
                        writer.append(",");
                    }

                    // If this is the price column (last column), add to total
                    if (i == row.length - 1) {
                        try {
                            String cleanValue = row[i].replace("$", "").replace(",", "");
                            totalPrice += Double.parseDouble(cleanValue);
                        } catch (NumberFormatException e) {
                            // Ignore if not a number
                        }
                    }
                }
                writer.append("\n");
            }

            // Add total row with $ sign
            writer.append("\"Total Price\",,\"$").append(String.format("%.2f", totalPrice)).append("\"\n");

            writer.flush();
            System.out.println("CSV write successful → " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static int extractNumberAfterTo(String text) {
        // Split the text by spaces
        String[] parts = text.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("to") && i + 1 < parts.length) {
                try {
                    return Integer.parseInt(parts[i + 1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("No valid number found after 'to'");
                }
            }
        }
        throw new IllegalArgumentException("'to' not found in the text");
    }
    public static String extractFirstPrice(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // Split by spaces
        String[] parts = text.split("\\s+");

        // First part should be the first price
        if (parts.length > 0) {
            return parts[0].trim();
        }
        return "";
    }
    // Method to return item by index
    public static String getItemByIndex(int index) {
        menuItems = new ArrayList<>();
        menuItems.add("Admin");
        menuItems.add("PIM");
        menuItems.add("Leave");
        menuItems.add("Time");
        menuItems.add("Recruitment");
        menuItems.add("PIM");
        menuItems.add("Performance");
        menuItems.add("Dashboard");
        menuItems.add("Directory");
        menuItems.add("Maintenance");
        menuItems.add("Claim");
        menuItems.add("Buzz");
        if (index >= 0 && index < menuItems.size()) {
            return menuItems.get(index);
        } else {
            return "Invalid index!";
        }
    }
    //Read from Json file
// Parse once and store all keys in a map
    public static void loadJsonData(String jsonFilePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(jsonFilePath);

        Object obj = jsonParser.parse(fileReader);
        JSONObject jsonObject = (JSONObject) obj;

        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            String value = String.valueOf(jsonObject.get(keyStr));
            dataMap.put(keyStr, value);
        }
    }

    // Access value by key later
    public static String getValue(String key) {
        return dataMap.get(key);
    }

    public static String extractNumber(String text) {
        // Use regex to keep only digits
        return text.replaceAll("[^0-9]", "");
    }

/*    public static void emulateNetwork(DevTools devTools, String networkType) {
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        switch (networkType.toLowerCase()) {
            case "2g":
                devTools.send(Network.emulateNetworkConditions(
                        false,
                        300,                                // latency (ms)
                        250 * 1024 / 8,                     // ~250 kbps download
                        50 * 1024 / 8,                      // ~50 kbps upload
                        Optional.of(ConnectionType.CELLULAR2G),
                        Optional.empty(),                   // packetLoss
                        Optional.empty(),                   // packetQueueLength
                        Optional.empty()                    // packetReordering
                ));
                break;

            case "3g":
                devTools.send(Network.emulateNetworkConditions(
                        false,
                        150,
                        750 * 1024 / 8,
                        250 * 1024 / 8,
                        Optional.of(ConnectionType.CELLULAR3G),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                ));
                break;

            case "4g":
                devTools.send(Network.emulateNetworkConditions(
                        false,
                        70,
                        4 * 1024 * 1024 / 8,
                        3 * 1024 * 1024 / 8,
                        Optional.of(ConnectionType.CELLULAR4G),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                ));
                break;

            case "offline":
                devTools.send(Network.emulateNetworkConditions(
                        true,                               // offline
                        0,
                        0,
                        0,
                        Optional.of(ConnectionType.NONE),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                ));
                break;

            default:
                throw new IllegalArgumentException("Unsupported network type: " + networkType);
        }
    }*/

    public static WebDriver createMobileDriver(String deviceName) {
        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 375);
        deviceMetrics.put("height", 812);
        deviceMetrics.put("pixelRatio", 3.0);

        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 13_0 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) " +
                        "Version/13.0 Mobile/15E148 Safari/604.1");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        // Improve scaling
        options.addArguments("--force-device-scale-factor=1");
        options.addArguments("--window-size=375,812");

        return new ChromeDriver(options);
    }



    public static WebDriver createDesktopDriver() {
        return new ChromeDriver();
    }


}


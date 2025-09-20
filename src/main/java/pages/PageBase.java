package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static drivers.DriverHolder.getDriver;
//import static java.sql.DriverManager.getDriver;

public class PageBase {

    static WebDriver driver;
    static WebDriverWait wait;

    // TODO: constructor to intailize webdriver
    public PageBase(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public static void hoverWebElement(WebDriver driver, WebElement element) {
        //Creating object of an Actions class
        Actions action = new Actions(getDriver());

//Performing the mouse hover action on the target element.
        action.moveToElement(element).perform();
    }

    //Waits
    public static void explicitWait(WebDriver driver, By webElementXPATH) {
        // explicit wait - to wait for the compose button to be click-able
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        wait.until(ExpectedConditions.visibilityOfElementLocated(webElementXPATH));
    }

    // TODO: Types of Waits
    //
    public static void fluentWaitHandling(WebDriver driver, By webElementXPATH) {
        FluentWait wait = new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(50))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(Exception.class);
        wait.until(ExpectedConditions.visibilityOfElementLocated(webElementXPATH));
    }

    // TODO: clear all browser data after each test
    public static void quitBrowser(WebDriver driver) {
        // clear browser localStorage , sessionStorage and delete All Cookies
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
        driver.manage().deleteAllCookies();
        driver.quit();
        // kill browser process on background
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
//                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
            } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("pkill -f chromedriver");
                Runtime.getRuntime().exec("pkill -f chrome");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // short wait
    public <T> T shortWait(ExpectedCondition<T> condition) {
        int attempts = 0;
        final int MAX_RETRIES = 5;
        final Duration TIMEOUT = Duration.ofSeconds(10);
        while (attempts < MAX_RETRIES) {
            try {
                wait = new WebDriverWait(this.driver, TIMEOUT);
                return wait.until(condition);
            } catch (StaleElementReferenceException e) {
                System.out.println("Attempt " + (attempts + 1) + ": StaleElementReferenceException - " + e.getMessage());
            } catch (TimeoutException e) {
                System.out.println("Attempt " + (attempts + 1) + ": TimeoutException - " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("Attempt " + (attempts + 1) + ": NoSuchElementException - " + e.getMessage());
            } catch (ElementClickInterceptedException e) {
                System.out.println("Attempt " + (attempts + 1) + ": ElementClickInterceptedException - " + e.getMessage());
            } catch (ElementNotInteractableException e) {
                System.out.println("Attempt " + (attempts + 1) + ": ElementNotInteractableException - " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Attempt " + (attempts + 1) + ": Unexpected exception - " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
            attempts++;
        }
        throw new RuntimeException("Condition failed after " + MAX_RETRIES + " attempts.");
    }

    public static <T> T longWait(ExpectedCondition<T> condition) {
        int attempts = 0;
        final int MAX_RETRIES = 5;
        final Duration TIMEOUT = Duration.ofSeconds(25);
        while (attempts < MAX_RETRIES) {
            try {
                wait = new WebDriverWait(driver, TIMEOUT);
                return wait.until(condition);
            } catch (StaleElementReferenceException e) {
                System.out.println("Attempt " + (attempts + 1) + ": StaleElementReferenceException - " + e.getMessage());
            } catch (TimeoutException e) {
                System.out.println("Attempt " + (attempts + 1) + ": TimeoutException - " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("Attempt " + (attempts + 1) + ": NoSuchElementException - " + e.getMessage());
            } catch (ElementClickInterceptedException e) {
                System.out.println("Attempt " + (attempts + 1) + ": ElementClickInterceptedException - " + e.getMessage());
            } catch (ElementNotInteractableException e) {
                System.out.println("Attempt " + (attempts + 1) + ": ElementNotInteractableException - " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Attempt " + (attempts + 1) + ": Unexpected exception - " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
            attempts++;
        }
        throw new RuntimeException("Condition failed after " + MAX_RETRIES + " attempts.");
    }

    // TODO: Capture Screenshot
    public static void captureScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        try {
            FileHandler.copy(takesScreenshot.getScreenshotAs(OutputType.FILE), new File(System.getProperty("user.dir")
                    + "/src/test/resources/Screenshots/" + screenshotName + System.currentTimeMillis() + ".png"));
        } catch (WebDriverException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void selectWithRobot(By locator, int downCount) throws Exception {
        // Focus the dropdown
        WebElement dropdown = driver.findElement(locator);
        dropdown.click();

        Robot robot = new Robot();

        // Navigate with DOWN arrow
        for (int i = 0; i < downCount; i++) {
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            Thread.sleep(200); // short delay to let UI update
        }

        // Confirm with ENTER
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void setImplicitWait(long timeInSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeInSeconds));
    }



}

package pages;

import org.openqa.selenium.*;
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

public class BasePage {
    //driver declaration
    static WebDriver driver;
    WebDriverWait wait;

    //Create Constructor for driver initialization
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }

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
                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
            } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("pkill -f chromedriver");
                Runtime.getRuntime().exec("pkill -f chrome");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // explicit wait
    public static void explicitWait(WebDriver driver, By element) {
        // explicit wait - to wait for the compose button to be click-able
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    //Fluent Wait
    public static void fluentWaitHandling(WebDriver driver, By element) {
        FluentWait wait = new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(50))
                .pollingEvery(Duration.ofSeconds(5)).ignoring(Exception.class);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    //Wait for Page loading
    public static void waitForPageLoad(WebDriver driver) {
        (new WebDriverWait(driver, Duration.ofSeconds(50))).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                JavascriptExecutor js = (JavascriptExecutor) d;
                String readyState = js.executeScript("return document.readyState").toString();
                System.out.println("Ready State: " + readyState);
                return (Boolean) readyState.equals("complete");
            }
        });
    }

    //Wait using lambda Expresion
    public boolean typeToElement(By locator, String text) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                int finalAttempts = attempts;
                return wait.until(driver -> {
                    try {
                        WebElement element = driver.findElement(locator);
                        if (element.isDisplayed() && element.isEnabled()) {
                            element.clear();
                            element.sendKeys(text);
                            return true;
                        } else {
                            return false;
                        }
                    } catch (NoSuchElementException e) {
                        return false;
                    }
                });
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        return false;
    }

    public boolean clickOnElement(By locator) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                int finalAttempts = attempts;
                return wait.until(driver -> {
                    try {
                        WebElement element = driver.findElement(locator);
                        if (element.isDisplayed() && element.isEnabled()) {
                            element.click();
                            return true;
                        }
                        return false;
                    } catch (NoSuchElementException e) {
                        return false;
                    }
                });
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        return false;
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
}

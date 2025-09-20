package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Utility;

import static util.Utility.extractNumber;


public class P02_HomePage extends PageBase {

    private static int count = 1;
    Robot robot;

    public P02_HomePage(WebDriver driver) {
        super(driver);
    }

    //Locator for first Test Case
    private By admin = By.xpath("(//ul/li/a)[1]");
    private By PIM = By.xpath("(//ul/li/a)[2]");

    private By messageDashboard = By.xpath("//span/h6");
    private By maintenanceLogin = By.xpath("//div/input[@name=\"password\"]");
    private By confirmLoginMaintenance = By.xpath("//div/button/following-sibling::button");

    //Locators for second Test Case
    private By addPIMbutton = By.xpath("//div/button[@class=\"oxd-button oxd-button--medium oxd-button--secondary\"]");
    private By uploadPhoto = By.xpath("(//button/i)[3]");
    private By userNameEmpolyee = By.xpath("//div/input[@name=\"firstName\"]");
    private By middleNameEmpolyee = By.xpath("//div/input[@name=\"middleName\"]");
    private By lastNameEmpolyee = By.xpath("//div/input[@name=\"lastName\"]");
    private By empolyeeID = By.xpath("(//div/input[@class=\"oxd-input oxd-input--active\"])[2]");
    private By saveButton = By.xpath("(//div/button)[5]");
    private By successMessage = By.id("oxd-toaster_1");
    private By numRecords = By.xpath("(//div/span)[2]");
    //Locators for Assertion
    private By PIMButton = By.xpath("(//li/a)[2]");
    private By namePIM = By.xpath("//div/div/following-sibling::input");
    private By searchButton = By.xpath("//div/button/following-sibling::button[@type=\"submit\"]");

    //Locator For Third Test Case ِAdmin
    private By userName = By.xpath("(//div/input[@class=\"oxd-input oxd-input--active\"])[2]");
    private By userRole = By.xpath("(//div/div[@class=\"oxd-select-text oxd-select-text--active\"])[1]");
    private By employeeName = By.xpath("//div/div/following-sibling::input[@placeholder=\"Type for hints...\"]");
    private By status = By.xpath("(//div/div[@class=\"oxd-select-text oxd-select-text--active\"])[2]");
    //Add new User
    private By password = By.xpath("(//div/input[@type=\"password\"])[1]");
    private By confirmPassword = By.xpath("(//div/input[@type=\"password\"])[2]");
    private By adminSaveButton = By.xpath("//button[@type=\"submit\"]");

    //Action Methods
    public P02_HomePage clickLeftList() throws InterruptedException {
        longWait(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//ul/li/a)[" + count + "]")));
        driver.findElement(By.xpath("(//ul/li/a)[" + count + "]")).click();
        count++;
        return this;
    }

    public boolean successMesssage(String subMain) {
        longWait(ExpectedConditions.visibilityOfElementLocated(this.messageDashboard));
        return subMain.equals(driver.findElement(messageDashboard).getText());
    }

    public P02_HomePage maintenancePage(String pass) {
        driver.findElement(maintenanceLogin).sendKeys(pass);
        longWait(ExpectedConditions.elementToBeClickable(this.confirmLoginMaintenance)).click();
        return this;
    }

    public P02_HomePage createNewPIM(String firstName, String middleName, String lastName, String empolyeeID, String filePath) throws InterruptedException {
        longWait(ExpectedConditions.elementToBeClickable(this.PIM)).click();
        longWait(ExpectedConditions.elementToBeClickable(this.addPIMbutton)).click();
        longWait(ExpectedConditions.elementToBeClickable(this.uploadPhoto)).click();
        StringSelection fPath = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(fPath, null);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        robot.delay(1000);

        // Press Ctrl+V
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        // Press Enter
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        //Thread.sleep(4000);
        longWait(ExpectedConditions.visibilityOfElementLocated(this.userNameEmpolyee));
        driver.findElement(userNameEmpolyee).sendKeys(firstName);
        longWait(ExpectedConditions.visibilityOfElementLocated(this.middleNameEmpolyee));
        driver.findElement(middleNameEmpolyee).sendKeys(middleName);
        longWait(ExpectedConditions.visibilityOfElementLocated(this.lastNameEmpolyee));
        driver.findElement(lastNameEmpolyee).sendKeys(lastName);
        longWait(ExpectedConditions.visibilityOfElementLocated(this.empolyeeID));
        driver.findElement(this.empolyeeID).clear();
        longWait(ExpectedConditions.visibilityOfElementLocated(this.empolyeeID));
        driver.findElement(this.empolyeeID).sendKeys(empolyeeID);
        longWait(ExpectedConditions.visibilityOfElementLocated(this.saveButton));
        Thread.sleep(5000);
        longWait(ExpectedConditions.elementToBeClickable(this.saveButton)).click();
        Thread.sleep(5000);

        setImplicitWait(500);
        return this;
    }

    public boolean successMesssagePIM(String message) {
        return message.equals(driver.findElement(successMessage).getText());
    }

    public P02_HomePage confirmPIMsavedSuccessfully(String PIMname) throws InterruptedException {
        longWait(ExpectedConditions.elementToBeClickable(this.PIMButton)).click();
        longWait(ExpectedConditions.visibilityOfElementLocated(this.namePIM));
        WebElement input = driver.findElement(namePIM);
        // type the text
        input.sendKeys(PIMname);
        //Thread.sleep(2000);
        //
        longWait(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='listbox']//div[normalize-space()='Ahmed1234 1234Ali 1234Shady']")));

        // press arrow down to highlight the first suggestion
        input.sendKeys(Keys.ARROW_DOWN);
        // press enter to select
        input.sendKeys(Keys.ENTER);
        longWait(ExpectedConditions.visibilityOfElementLocated(this.searchButton));
        longWait(ExpectedConditions.elementToBeClickable(this.searchButton)).click();
        return this;
    }

    public boolean PIMaddingSuccessfully() throws InterruptedException {
        longWait(ExpectedConditions.visibilityOfElementLocated(numRecords));
        return extractNumber(driver.findElement(numRecords).getText()).equals("1");
    }

    public P02_HomePage createAdminPIM(String employName, String userName, String password) throws Exception {
        longWait(ExpectedConditions.visibilityOfElementLocated(this.admin));
        longWait(ExpectedConditions.elementToBeClickable(this.admin)).click();
        longWait(ExpectedConditions.visibilityOfElementLocated(this.addPIMbutton));
        longWait(ExpectedConditions.elementToBeClickable(this.addPIMbutton)).click();
        longWait(ExpectedConditions.visibilityOfElementLocated(this.userRole));
        selectWithRobot(userRole,1);
        longWait(ExpectedConditions.visibilityOfElementLocated(this.employeeName));
        WebElement input = driver.findElement(this.employeeName);
        // type the text
        input.sendKeys(employName);
        // wait for suggestions to load (better with WebDriverWait)
        // press arrow down to highlight the first suggestion
        input.sendKeys(Keys.ARROW_DOWN);
        // press enter to select
        input.sendKeys(Keys.ENTER);
        longWait(ExpectedConditions.visibilityOfElementLocated(this.status));
        selectWithRobot(status,1);

        driver.findElement(this.userName).sendKeys(userName);
        driver.findElement(this.password).sendKeys(password);
        driver.findElement(confirmPassword).sendKeys(password);
        longWait(ExpectedConditions.elementToBeClickable(this.adminSaveButton)).click();
        return this;
    }

    public P02_HomePage confirmAdminsavedSuccessfully(String employName) throws InterruptedException {
        // Wait for Admin tab and click
        longWait(ExpectedConditions.elementToBeClickable(this.admin)).click();
        longWait(ExpectedConditions.visibilityOfElementLocated(this.namePIM));
        // Wait for input field
        WebElement input = longWait(ExpectedConditions.visibilityOfElementLocated(this.namePIM));
        // Type the employee name
        input.sendKeys(employName);
        // Wait for suggestions dropdown to appear (instead of Thread.sleep)
        longWait(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='listbox']//div[1]")));
        // Use Selenium's Keys (no Robot)
        input.sendKeys(Keys.ARROW_DOWN); // highlight first suggestion
        input.sendKeys(Keys.ENTER);      // select it
        // Wait for search button and click
        longWait(ExpectedConditions.elementToBeClickable(this.searchButton)).click();
        return this;
    }


    public boolean adminAddingSuccessfully() throws InterruptedException {
        longWait(ExpectedConditions.visibilityOfElementLocated(this.numRecords));
        return extractNumber(driver.findElement(this.numRecords).getText()).equals("1");
    }



}

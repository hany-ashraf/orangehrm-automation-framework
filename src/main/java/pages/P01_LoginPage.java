package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class P01_LoginPage extends PageBase {

    public P01_LoginPage(WebDriver driver) {
        super(driver);
    }

    //Locators
    private final By userName = By.name("username");
    private final By password = By.name("password");
    private final By loginButton = By.xpath("//button");


    //Action Methods
    public P01_LoginPage fillUserName(String userName) {
        shortWait(ExpectedConditions.visibilityOfElementLocated(this.userName));
        driver.findElement(this.userName).sendKeys(userName);
        return this;
    }

    public P01_LoginPage fillPassword(String password) {
        driver.findElement(this.password).sendKeys(password);
        return this;
    }

    public P01_LoginPage clickLoginButton() {
        longWait(ExpectedConditions.elementToBeClickable(this.loginButton)).click();
        return this;
    }

    public boolean isLoginSuccessURL(String url) {
        return url.equals(driver.getCurrentUrl());
    }

}

package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class P01_LoginPage extends BasePage {

    public P01_LoginPage(WebDriver driver) {
        super(driver);
    }

    //Locators
    private final By userName = By.name("username");
    private final By password = By.name("password");
    private final By loginButton = By.xpath("//button");


    //Action Methods
    public P01_LoginPage fillUserName(String userName) {
        //typeToElement(this.userName,userName);
        driver.findElement(this.userName).sendKeys(userName);
        return this;
    }

    public P01_LoginPage fillPassword(String password) {
        //typeToElement(this.password,password);

        driver.findElement(this.password).sendKeys(password);
        return this;
    }

    public P01_LoginPage clickLoginButton() {
        //clickOnElement(this.loginButton);
        driver.findElement(this.loginButton).click();
        return this;
    }

    public boolean isLoginSuccessURL(String url) {
        return url.equals(driver.getCurrentUrl());
    }


}

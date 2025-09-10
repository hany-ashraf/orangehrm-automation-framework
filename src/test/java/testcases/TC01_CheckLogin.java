package testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.P01_LoginPage;

import static drivers.DriverHolder.getDriver;
import static pages.BasePage.captureScreenshot;

public class TC01_CheckLogin extends TestBase {
    String user = "Admin";
    String password = "admin123";
    String url = "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index";
    @Test(priority = 1, description = "Login With Valid Data")
    public void validateWithLoginData_P() throws InterruptedException {
        Thread.sleep(20000);
        new P01_LoginPage(getDriver()).fillUserName(user).fillPassword(password);
//        Thread.sleep(2000);
//        new P01_LoginPage(getDriver()).clickLoginButton();
//
//        //Capture ScreenShot
//        Thread.sleep(4000);
//        captureScreenshot(getDriver(), "login_screenShot");
//
//        //assertion
//        Assert.assertTrue(new P01_LoginPage(getDriver()).isLoginSuccessURL(url));

    }
}

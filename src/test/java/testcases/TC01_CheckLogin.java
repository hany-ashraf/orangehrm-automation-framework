package testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.P01_LoginPage;
import retryTest.MyRetry;

import static drivers.DriverHolder.getDriver;
import static pages.PageBase.captureScreenshot;

public class TC01_CheckLogin extends TestBase {
    String user = "Admin";
    String password = "admin123";
    String url = "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index";
    @Test(priority = 1, description = "Login With Valid Data", retryAnalyzer = MyRetry.class)
    public void validateWithLoginData_P(){
        new P01_LoginPage(getDriver()).fillUserName(user).fillPassword(password);
        new P01_LoginPage(getDriver()).clickLoginButton();
        //Capture ScreenShot
        captureScreenshot(getDriver(), "login_screenShot");
        //Hard assertion
        Assert.assertTrue(new P01_LoginPage(getDriver()).isLoginSuccessURL(url));
    }
}

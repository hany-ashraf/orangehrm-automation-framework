package testcases;

import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.P01_LoginPage;
import pages.P02_HomePage;

import java.io.IOException;

import static drivers.DriverHolder.getDriver;
import static util.Utility.*;

public class TC02_HomePage extends TestBase{
    String user = "Admin";
    String password = "admin123";

    @Test(priority = 1, description = "loop in all Taps")
    public void validateOnAllTaps_P() throws InterruptedException {
        new P01_LoginPage(getDriver()).fillUserName(user).fillPassword(password);
        new P01_LoginPage(getDriver()).clickLoginButton();
        for(int i = 0; i<12; i++) {
            new P02_HomePage(getDriver()).clickLeftList();
            if(i==9){
                Thread.sleep(2000);
                new P02_HomePage(getDriver()).maintenancePage("admin123");
            }
            Assert.assertTrue(new P02_HomePage(getDriver()).successMesssage(getItemByIndex(i)));
        }
    }
    @Test(priority = 2, description = "Create New PIM empolyee")
    public void validateCreateNewPIM_P() throws IOException, ParseException, InterruptedException {
        new P01_LoginPage(getDriver()).fillUserName(user).fillPassword(password);
        new P01_LoginPage(getDriver()).clickLoginButton();
        loadJsonData("src\\test\\resources\\TestData\\testData.json");
        new P02_HomePage(getDriver()).createNewPIM(getValue("firstName"),getValue("middleName"),getValue("lastName"),getValue("employeeID"),getValue("filePath"));
        new P02_HomePage(getDriver()).confirmPIMsavedSuccessfully(getValue("firstName"));
        Assert.assertTrue(new P02_HomePage(getDriver()).PIMaddingSuccessfully());
    }

    @Test(priority = 3, description = "Make New PIM as admin")
    public void validateCreateAdmin_P() throws Exception {
        loadJsonData("src\\test\\resources\\TestData\\testData.json");
        new P02_HomePage(getDriver()).createAdminPIM(getValue("firstName"),getValue("firstName"),getValue("password"));
        new P02_HomePage(getDriver()).confirmAdminsavedSuccessfully(getValue("firstName"));
        Assert.assertTrue(new P02_HomePage(getDriver()).adminAddingSuccessfully());
    }

}

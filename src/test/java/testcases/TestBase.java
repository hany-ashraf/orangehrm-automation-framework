package testcases;

import common.MyScreenRecorder;
import org.testng.annotations.*;
import java.time.Duration;

import static drivers.DriverFactory.getNewInstance;
import static drivers.DriverHolder.getDriver;
import static drivers.DriverHolder.setDriver;
import static util.Utility.openBrowserNetworkTab;

public class TestBase {

    @BeforeSuite
    public void startRecording() throws Exception {
        //Start recording
        MyScreenRecorder.startRecording("Login_Test");
    }
    @Parameters({"browser","URL"})
    @BeforeTest
    public void setupDriver(@Optional String browser, String URL) throws Exception {
        if(browser == null) browser = "";
        setDriver(getNewInstance(browser));
        // implicit wait
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        getDriver().get(URL);
        //TODO: Open network tap to monitor APIs
        //openBrowserNetworkTab();
    }

    //TODO: Back again to the home page
    public void clickBack(){
        getDriver().navigate().back();
    }

    //TODO: Refresh the page
    public void clickRefreshTwice(){
        getDriver().navigate().refresh();
        getDriver().navigate().refresh();
    }


    @AfterTest
    public void tearDown(){
       getDriver().quit();
    }

    @AfterSuite
    public void stopRecording() throws Exception {
        //Stop recording
        MyScreenRecorder.stopRecording();
    }
}

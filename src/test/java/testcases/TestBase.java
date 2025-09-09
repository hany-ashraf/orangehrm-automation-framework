package testcases;

import common.MyScreenRecorder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.*;
import util.Utility;

import java.time.Duration;

import static drivers.DriverFactory.getNewInstance;
import static drivers.DriverHolder.getDriver;
import static drivers.DriverHolder.setDriver;
import static pages.BasePage.*;
//import static util.Utility.emulateNetwork;
import static util.Utility.openBrowserNetworkTab;

public class TestBase {

    private DevTools devTools;

    @BeforeSuite
    public void startRecording() throws Exception {
        //Start recording
        MyScreenRecorder.startRecording("Login_Test");
    }

    @Parameters({"browser", "URL","device"})
    @BeforeTest
    public void setupDriver(@Optional String browser, String URL,@Optional String device) throws Exception {

        if (browser == null) browser = "";


        if (device != null && !device.equalsIgnoreCase("desktop") && !device.isEmpty()) {
            // Use mobile emulation with device from XML
            setDriver(Utility.createMobileDriver(device));
        } else {
            // Use normal browser
            setDriver(getNewInstance(browser));
        }

        // 2. Create DevTools session
        //devTools = ((HasDevTools) getDriver()).getDevTools();
        //devTools.createSession();

        //emulateNetwork(devTools, networkType);


        // implicit wait
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        getDriver().get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        //TODO: Open network tap to monitor APIs
        //openBrowserNetworkTab();
    }

    //TODO: Back again to the home page
    public void clickBack() {
        getDriver().navigate().back();
    }

    //TODO: Refresh the page
    public void clickRefreshTwice() {
        getDriver().navigate().refresh();
        getDriver().navigate().refresh();
    }


    @AfterTest
    public void tearDown() {
        getDriver().quit();
    }

    @AfterSuite
    public void stopRecording() throws Exception {
        //Stop recording
        MyScreenRecorder.stopRecording();
    }
}

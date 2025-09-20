package testcases;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.github.javafaker.Faker;
import drivers.DriverFactory;
import listeners.TestExecustionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static drivers.DriverHolder.getDriver;
import static drivers.DriverHolder.setDriver;
import static pages.PageBase.quitBrowser;
import static util.Utility.openBrowserNetworkTab;

@Listeners({ChainTestListener.class, TestExecustionListener.class})
public class TestBase {
    SoftAssert softAssert = new SoftAssert();
    Faker faker = new Faker();

    // logger
    private static final Logger logger = LogManager.getLogger(TestBase.class);

    // extend report
    protected static ExtentSparkReporter htmlReporter;
    protected static ExtentReports extent;
    protected static ExtentTest test;
    private static String PROJECT_NAME = null;

    private static String PROJECT_URL = null;
    static Properties prop;
    static FileInputStream readProperty;
    protected String lang;

    @BeforeSuite
    public void beforeSuite() throws Exception {

        // initialize the HtmlReporter
        htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/testReport.html");

        //initialize ExtentReports and attach the HtmlReporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        setProjectDetails();

        // initialize test
        test = extent.createTest(PROJECT_NAME + " Test Automation Project");

        //configuration items to change the look and fee add content, manage tests etc
        htmlReporter.config().setDocumentTitle(PROJECT_NAME + " Test Automation Report");
        htmlReporter.config().setReportName(PROJECT_NAME + " Test Report");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    private void setProjectDetails() throws IOException {
        // TODO: Step1: define object of properties file
        readProperty = new FileInputStream(
                System.getProperty("user.dir") + "/src/test/resources/properties/environment.properties");
        prop = new Properties();
        prop.load(readProperty);

        // define project name from properties file
        PROJECT_NAME = prop.getProperty("projectName");
        PROJECT_URL = prop.getProperty("url");
    }


    @Parameters("browsername")
    @BeforeTest
    public void OpenBrower(@Optional String browsername) throws AWTException {
        lang = browsername;
        // setDriver(DriverFactory.getNewInstance(browsername));
        setDriver(DriverFactory.getNewInstance(browsername));


        logger.info("Starting Browser");
        getDriver().get(PROJECT_URL);

        // open browser network
        //openBrowserNetworkTab();
    }

    @AfterTest
    public void TearDown() {

        logger.info("close browser");
        quitBrowser(getDriver());
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, result.getName() + " failed with the following error: " + result.getThrowable());
            Reporter.log("Failed to perform " + result.getName(), 10, true);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, result.getName());
            Reporter.log("Successfully perform " + result.getName(), 10, true);
        } else {
            test.log(Status.SKIP, result.getName());
            Reporter.log("Skip " + result.getName(), 10, true);
        }
    }

    @AfterSuite
    public void afterSuite() throws Exception {
        extent.flush();
    }
}

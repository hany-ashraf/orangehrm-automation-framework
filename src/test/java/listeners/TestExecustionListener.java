package listeners;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class TestExecustionListener implements ITestListener, ISuiteListener, IInvokedMethodListener {
    private static final Logger logger = LogManager.getLogger(TestExecustionListener.class);
    private static final Logger failureLogger = LogManager.getLogger("TestFailureLogger");
    private static final Logger screenshotLogger = LogManager.getLogger("ScreenshotLogger");

    private static final String SCREENSHOT_DIR = "screenshots";
    private static final String REPORTS_DIR = "test-reports";

    // Suite level methods
    @Override
    public void onStart(ISuite suite) {
        logger.info("=== SUITE STARTED: {} ===", suite.getName());
        logger.info("Suite XML file: {}", suite.getXmlSuite().getFileName());

        // Create directories for screenshots and reports
        createDirectories();

        // Log suite configuration
        logSuiteConfiguration(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("=== SUITE FINISHED: {} ===", suite.getName());
        logger.info("Suite execution time: {} ms",
                suite.getResults().values().stream()
                        .mapToLong(result -> result.getTestContext().getEndDate().getTime() -
                                result.getTestContext().getStartDate().getTime())
                        .sum());
    }

    // Test level methods
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("TEST STARTED: {}.{}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());

        // Log test parameters if any
        Object[] parameters = result.getParameters();
        if (parameters.length > 0) {
            logger.info("Test parameters: {}", Arrays.toString(parameters));
        }

        // Log test groups
        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            logger.info("Test groups: {}", Arrays.toString(groups));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        logger.info("✅ TEST PASSED: {}.{} (Duration: {} ms)",
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();

        // Log failure details
        failureLogger.error("❌ TEST FAILED: {} (Duration: {} ms)", testName, duration);
        failureLogger.error("Failure reason: {}", result.getThrowable().getMessage());
        failureLogger.error("Stack trace:", result.getThrowable());

        // Log test context information
        logTestContext(result);

        // Take screenshot if WebDriver is available
        takeScreenshotOnFailure(result);

        // Log browser information if available
        logBrowserInformation(result);

        // Log test data if available
        logTestData(result);

        // Generate failure report
        generateFailureReport(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⚠️ TEST SKIPPED: {}.{}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());

        if (result.getThrowable() != null) {
            logger.warn("Skip reason: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("⚡ TEST FAILED BUT WITHIN SUCCESS PERCENTAGE: {}.{}",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
    }

    // Method level methods (for @BeforeMethod, @AfterMethod, etc.)
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            logger.debug("About to invoke test method: {}", method.getTestMethod().getMethodName());
        } else {
            logger.debug("About to invoke configuration method: {} ({})",
                    method.getTestMethod().getMethodName(),
                    getConfigurationType(method.getTestMethod()));
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            logger.debug("Finished invoking test method: {}", method.getTestMethod().getMethodName());
        } else {
            logger.debug("Finished invoking configuration method: {} ({})",
                    method.getTestMethod().getMethodName(),
                    getConfigurationType(method.getTestMethod()));

            // Log if configuration method failed
            if (testResult.getStatus() == ITestResult.FAILURE) {
                failureLogger.error("Configuration method failed: {} - {}",
                        method.getTestMethod().getMethodName(),
                        testResult.getThrowable().getMessage());
            }
        }
    }

    // Helper methods
    private void createDirectories() {
        try {
            File screenshotDir = new File(SCREENSHOT_DIR);
            File reportsDir = new File(REPORTS_DIR);

            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
                logger.info("Created screenshot directory: {}", screenshotDir.getAbsolutePath());
            }

            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
                logger.info("Created reports directory: {}", reportsDir.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("Failed to create directories", e);
        }
    }

    private void logSuiteConfiguration(ISuite suite) {
        logger.info("Suite parameters: {}", suite.getXmlSuite().getParameters());
        logger.info("Parallel mode: {}", suite.getXmlSuite().getParallel());
        logger.info("Thread count: {}", suite.getXmlSuite().getThreadCount());
    }

    private void logTestContext(ITestResult result) {
        ITestContext context = result.getTestContext();

        failureLogger.error("Test context information:");
        failureLogger.error("  - Test name: {}", context.getName());
        failureLogger.error("  - Output directory: {}", context.getOutputDirectory());
        failureLogger.error("  - Start date: {}", context.getStartDate());
        failureLogger.error("  - End date: {}", context.getEndDate());
        failureLogger.error("  - Failed tests count: {}", context.getFailedTests().size());
        failureLogger.error("  - Passed tests count: {}", context.getPassedTests().size());
        failureLogger.error("  - Skipped tests count: {}", context.getSkippedTests().size());
    }

    private void takeScreenshotOnFailure(ITestResult result) {
        try {
            // Try to get WebDriver from test instance or test context
            WebDriver driver = getWebDriverFromTest(result);

            if (driver != null && driver instanceof TakesScreenshot) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String testName = result.getTestClass().getName() + "_" + result.getMethod().getMethodName();
                String fileName = String.format("%s_%s_FAILED.png", testName, timestamp);

                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File destination = new File(SCREENSHOT_DIR, fileName);

                FileUtils.copyFile(screenshot, destination);

                screenshotLogger.info("Screenshot captured for failed test: {}", destination.getAbsolutePath());
                failureLogger.error("Screenshot saved: {}", destination.getAbsolutePath());

                // Add screenshot path to test result attributes
                System.setProperty("screenshot.path", destination.getAbsolutePath());

            } else {
                failureLogger.warn("WebDriver not available for screenshot capture");
            }
        } catch (Exception e) {
            failureLogger.error("Failed to capture screenshot", e);
        }
    }

    private WebDriver getWebDriverFromTest(ITestResult result) {
        try {
            Object testInstance = result.getInstance();

            // Try common field names for WebDriver
            String[] possibleDriverFields = {"driver", "webDriver", "webdriver"};

            for (String fieldName : possibleDriverFields) {
                try {
                    java.lang.reflect.Field driverField = testInstance.getClass().getDeclaredField(fieldName);
                    driverField.setAccessible(true);
                    Object driver = driverField.get(testInstance);

                    if (driver instanceof WebDriver) {
                        return (WebDriver) driver;
                    }
                } catch (NoSuchFieldException e) {
                    // Try next field name
                } catch (IllegalAccessException e) {
                    logger.warn("Cannot access driver field: {}", fieldName);
                }
            }

            // Try to get from superclass
            Class<?> superClass = testInstance.getClass().getSuperclass();
            while (superClass != null) {
                for (String fieldName : possibleDriverFields) {
                    try {
                        java.lang.reflect.Field driverField = superClass.getDeclaredField(fieldName);
                        driverField.setAccessible(true);
                        Object driver = driverField.get(testInstance);

                        if (driver instanceof WebDriver) {
                            return (WebDriver) driver;
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        // Continue searching
                    }
                }
                superClass = superClass.getSuperclass();
            }

        } catch (Exception e) {
            logger.debug("Could not extract WebDriver from test instance", e);
        }

        return null;
    }

    private void logBrowserInformation(ITestResult result) {
        try {
            WebDriver driver = getWebDriverFromTest(result);
            if (driver != null) {
                failureLogger.error("Browser information:");
                failureLogger.error("  - Current URL: {}", driver.getCurrentUrl());
                failureLogger.error("  - Page title: {}", driver.getTitle());
                failureLogger.error("  - Window handles count: {}", driver.getWindowHandles().size());

                // Log browser capabilities if available
                if (driver instanceof org.openqa.selenium.remote.RemoteWebDriver) {
                    org.openqa.selenium.remote.RemoteWebDriver remoteDriver = (org.openqa.selenium.remote.RemoteWebDriver) driver;
                    failureLogger.error("  - Browser: {}",
                            remoteDriver.getCapabilities().getBrowserName());
                    failureLogger.error("  - Browser version: {}",
                            remoteDriver.getCapabilities().getBrowserVersion());
                    failureLogger.error("  - Platform: {}",
                            remoteDriver.getCapabilities().getPlatformName());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to log browser information", e);
        }
    }

    private void logTestData(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters.length > 0) {
            failureLogger.error("Test data parameters:");
            for (int i = 0; i < parameters.length; i++) {
                failureLogger.error("  - Parameter {}: {} ({})",
                        i + 1,
                        parameters[i],
                        parameters[i] != null ? parameters[i].getClass().getSimpleName() : "null");
            }
        }

        // Log test attributes if any
        if (!result.getAttributeNames().isEmpty()) {
            failureLogger.error("Test attributes:");
            for (String attrName : result.getAttributeNames()) {
                failureLogger.error("  - {}: {}", attrName, result.getAttribute(attrName));
            }
        }
    }

    private void generateFailureReport(ITestResult result) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String testName = result.getTestClass().getName() + "_" + result.getMethod().getMethodName();
            String fileName = String.format("%s_%s_failure_report.html", testName, timestamp);

            StringBuilder reportContent = new StringBuilder();
            reportContent.append("<html><head><title>Test Failure Report</title></head><body>");
            reportContent.append("<h1>Test Failure Report</h1>");
            reportContent.append("<h2>Test Information</h2>");
            reportContent.append("<p><strong>Test Class:</strong> ").append(result.getTestClass().getName()).append("</p>");
            reportContent.append("<p><strong>Test Method:</strong> ").append(result.getMethod().getMethodName()).append("</p>");
            reportContent.append("<p><strong>Failure Time:</strong> ").append(LocalDateTime.now()).append("</p>");
            reportContent.append("<p><strong>Duration:</strong> ").append(result.getEndMillis() - result.getStartMillis()).append(" ms</p>");

            reportContent.append("<h2>Failure Details</h2>");
            reportContent.append("<p><strong>Error Message:</strong> ").append(result.getThrowable().getMessage()).append("</p>");
            reportContent.append("<h3>Stack Trace</h3>");
            reportContent.append("<pre>").append(getStackTrace(result.getThrowable())).append("</pre>");

            if (result.getParameters().length > 0) {
                reportContent.append("<h2>Test Parameters</h2>");
                reportContent.append("<ul>");
                for (Object param : result.getParameters()) {
                    reportContent.append("<li>").append(param).append("</li>");
                }
                reportContent.append("</ul>");
            }

            reportContent.append("</body></html>");

            File reportFile = new File(REPORTS_DIR, fileName);
            FileUtils.writeStringToFile(reportFile, reportContent.toString(), "UTF-8");

            failureLogger.error("Failure report generated: {}", reportFile.getAbsolutePath());

        } catch (IOException e) {
            failureLogger.error("Failed to generate failure report", e);
        }
    }

    private String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    private String getConfigurationType(ITestNGMethod method) {
        if (method.isBeforeMethodConfiguration()) return "BeforeMethod";
        if (method.isAfterMethodConfiguration()) return "AfterMethod";
        if (method.isBeforeClassConfiguration()) return "BeforeClass";
        if (method.isAfterClassConfiguration()) return "AfterClass";
        if (method.isBeforeTestConfiguration()) return "BeforeTest";
        if (method.isAfterTestConfiguration()) return "AfterTest";
        if (method.isBeforeSuiteConfiguration()) return "BeforeSuite";

        if (method.isAfterSuiteConfiguration()) return "AfterSuite";
        if (method.isBeforeGroupsConfiguration()) return "BeforeGroups";
        if (method.isAfterGroupsConfiguration()) return "AfterGroups";
        return "Unknown";
    }
}
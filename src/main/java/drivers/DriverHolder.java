package drivers;

import org.openqa.selenium.WebDriver;

public class DriverHolder {

    // webdriver defined as thread local
    private static final ThreadLocal<WebDriver> driver=new ThreadLocal<>();

    // getter method to return thread local driver
    public static synchronized WebDriver getDriver(){
        return driver.get();
    }

    // setter to set driver
    public static void setDriver(WebDriver driver){
        DriverHolder.driver.set(driver);
    }

}

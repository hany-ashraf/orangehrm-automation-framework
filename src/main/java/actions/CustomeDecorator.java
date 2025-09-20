package actions;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;

import java.util.Arrays;
import java.util.List;

public class CustomeDecorator implements WebElement {

    private WebDriver driver;
    private By element;
    private int wait;

    public CustomeDecorator(WebDriver driver, By element, int wait) {
        this.driver = driver;
        this.element = element;
        this.wait=wait;
    }

    @Override
    public void click() {
        System.out.println("Click on element " + element);
//        longWait(driver).until(ExpectedConditions.elementToBeClickable(element));
        driver.findElement(element).click();
    }

    @Override
    public void submit() {

    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        System.out.println("Fill element " + element + " With this text " + Arrays.toString(keysToSend));
        driver.findElement(element).sendKeys(keysToSend);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public String getTagName() {
        return "";
    }

    @Override
    public @Nullable String getDomProperty(String name) {
        return WebElement.super.getDomProperty(name);
    }

    @Override
    public @Nullable String getDomAttribute(String name) {
        return WebElement.super.getDomAttribute(name);
    }

    @Override
    public @Nullable String getAttribute(String name) {
        return "";
    }

    @Override
    public @Nullable String getAriaRole() {
        return WebElement.super.getAriaRole();
    }

    @Override
    public @Nullable String getAccessibleName() {
        return WebElement.super.getAccessibleName();
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public List<WebElement> findElements(By by) {
        return List.of();
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public SearchContext getShadowRoot() {
        return WebElement.super.getShadowRoot();
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public String getCssValue(String propertyName) {
        return "";
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }
}

package com.automation.pages;

import com.automation.driver.DriverFactory;
import com.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Shared page actions using explicit waits.
 */
public class BasePage {

    protected final WebDriver driver;

    public BasePage() {
        this.driver = DriverFactory.getDriver();
    }

    protected void click(By locator) {
        WebElement element = WaitUtils.waitForClickable(driver, locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void type(By locator, String text) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        element.click();
        // React controlled inputs: set the native value and dispatch input/change events.
        ((JavascriptExecutor) driver).executeScript(
                "const input = arguments[0];"
                        + "const value = arguments[1];"
                        + "const setter = Object.getOwnPropertyDescriptor("
                        + "window.HTMLInputElement.prototype, 'value').set;"
                        + "setter.call(input, value);"
                        + "input.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "input.dispatchEvent(new Event('change', { bubbles: true }));",
                element,
                text);
    }

    protected String getText(By locator) {
        return WaitUtils.waitForVisible(driver, locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        return WaitUtils.waitForVisible(driver, locator).isDisplayed();
    }

    protected WebElement findVisible(By locator) {
        return WaitUtils.waitForVisible(driver, locator);
    }
}

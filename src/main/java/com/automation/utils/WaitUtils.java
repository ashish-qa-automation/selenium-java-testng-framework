package com.automation.utils;

import com.automation.config.ConfigReader;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Explicit wait helpers. Prefer these over Thread.sleep and heavy implicit waits.
 */
public final class WaitUtils {

    private WaitUtils() {
    }

    private static WebDriverWait newWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return newWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return newWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForUrlContains(WebDriver driver, String fraction) {
        return newWait(driver).until(ExpectedConditions.urlContains(fraction));
    }

    public static boolean waitForTitleContains(WebDriver driver, String title) {
        return newWait(driver).until(ExpectedConditions.titleContains(title));
    }

    public static boolean waitForAttributeToBe(WebDriver driver, By locator, String attribute, String value) {
        return newWait(driver).until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }
}

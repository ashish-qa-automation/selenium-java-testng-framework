package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.driver.DriverFactory;
import com.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Phase 1C verification: driver starts, Sauce Demo loads, browser quits cleanly.
 */
public class FrameworkSmokeTest extends BaseTest {

    @Test(groups = "smoke", description = "Open Sauce Demo and verify login page is ready")
    public void shouldOpenSauceDemoLoginPage() {
        Assert.assertTrue(
                WaitUtils.waitForUrlContains(DriverFactory.getDriver(), "saucedemo.com"),
                "URL should contain saucedemo.com");

        Assert.assertTrue(
                WaitUtils.waitForTitleContains(DriverFactory.getDriver(), "Swag Labs"),
                "Page title should contain Swag Labs");

        Assert.assertTrue(
                WaitUtils.waitForVisible(DriverFactory.getDriver(), By.id("user-name")).isDisplayed(),
                "Username field should be visible on login page");

        Assert.assertTrue(
                WaitUtils.waitForVisible(DriverFactory.getDriver(), By.id("password")).isDisplayed(),
                "Password field should be visible on login page");
    }
}

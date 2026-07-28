package com.automation.base;

import com.automation.config.ConfigReader;
import com.automation.driver.DriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Shared TestNG setup/teardown for WebDriver lifecycle.
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
        DriverFactory.getDriver().get(ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}

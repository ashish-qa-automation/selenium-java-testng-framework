package com.automation.listeners;

import com.automation.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Captures a screenshot when a test fails.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String path = ScreenshotUtils.capture(result.getName());
            System.out.println("Screenshot saved: " + path);
        } catch (RuntimeException e) {
            System.err.println("Could not capture screenshot: " + e.getMessage());
        }
    }
}

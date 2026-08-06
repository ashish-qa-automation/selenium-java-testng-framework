package com.automation.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test once to reduce flake noise. Limited by design.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY = 1;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            System.out.println(
                    "Retrying failed test: " + result.getName() + " (attempt " + (retryCount + 1) + ")");
            return true;
        }
        return false;
    }
}

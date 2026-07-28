package com.automation.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Optional flake retry policy.
 * Implementation planned in Phase 1F.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    @Override
    public boolean retry(ITestResult result) {
        return false;
    }
}

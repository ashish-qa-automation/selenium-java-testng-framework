package com.automation.listeners;

import com.automation.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Captures a screenshot on failure and attaches it to the Allure report.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String path = ScreenshotUtils.capture(result.getName());
            System.out.println("Screenshot saved: " + path);

            byte[] bytes = Files.readAllBytes(Path.of(path));
            Allure.addAttachment(
                    "Failure screenshot",
                    "image/png",
                    new ByteArrayInputStream(bytes),
                    ".png");
        } catch (Exception e) {
            System.err.println("Could not capture screenshot: " + e.getMessage());
        }
    }
}

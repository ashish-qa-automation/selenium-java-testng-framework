package com.automation.utils;

import com.automation.driver.DriverFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Captures screenshots for failure triage.
 */
public final class ScreenshotUtils {

    private static final Path SCREENSHOT_DIR = Paths.get("screenshots");

    private ScreenshotUtils() {
    }

    public static String capture(String testName) {
        WebDriver driver = DriverFactory.getDriver();
        return capture(driver, testName);
    }

    public static String capture(WebDriver driver, String testName) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);

            String safeName = testName == null ? "screenshot" : testName.replaceAll("[^a-zA-Z0-9-_]", "_");
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path destination = SCREENSHOT_DIR.resolve(safeName + "_" + timestamp + ".png");

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toAbsolutePath().toString();
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Failed to capture screenshot for: " + testName, e);
        }
    }
}

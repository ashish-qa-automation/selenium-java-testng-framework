package com.automation.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads framework configuration from {@code config.properties} on the test classpath.
 * Full driver/page usage is wired in Phase 1C+.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfigReader.
            class.getClassLoader().getResourceAsStream("config.properties")) 
        {
            if (inputStream == null) {
                throw new RuntimeException("config.properties file not found in classpath");
            }

            PROPERTIES.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getBaseUrl() {
        return getProperty("baseUrl");
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicitWait"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicitWait"));
    }
}

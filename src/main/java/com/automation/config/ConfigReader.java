package com.automation.config;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Loads framework configuration from {@code config.properties} on the test classpath.
 * Resolution order: system property → environment variable → file.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream =
                ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
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
        String systemValue = System.getProperty(key);
        if (isPresent(systemValue)) {
            return systemValue;
        }

        String envValue = System.getenv(toEnvName(key));
        if (isPresent(envValue)) {
            return envValue;
        }

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

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Converts a properties key such as {@code baseUrl} to {@code BASE_URL}.
     */
    private static String toEnvName(String key) {
        return key.replaceAll("([a-z])([A-Z])", "$1_$2")
                .replace('.', '_')
                .replace('-', '_')
                .toUpperCase(Locale.ROOT);
    }
}

package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        try {
            properties = new Properties();
            InputStream inputStream = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            if (inputStream == null) {
                throw new RuntimeException("config.properties file not found in classpath");
            }

            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}

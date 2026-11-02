# ConfigReader.java -- Complete Learning Notes

## Purpose

`ConfigReader` is the single source of truth for framework
configuration.

Its responsibilities are:

-   Load `config.properties` only once.
-   Store all configuration values in memory.
-   Provide controlled access to configuration values.
-   Hide the implementation of how configuration is loaded.

It does **not**:

-   Launch browsers
-   Execute tests
-   Read test data
-   Manage WebDriver

------------------------------------------------------------------------

# Complete Source

``` java
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

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
```

------------------------------------------------------------------------

# Part 1 -- Class Declaration

``` java
public final class ConfigReader
```

## What?

Defines a utility class responsible for configuration management.

## Why `final`?

Configuration loading should have a single implementation.

Preventing inheritance protects the framework design.

------------------------------------------------------------------------

# Part 2 -- Properties Storage

``` java
private static final Properties PROPERTIES = new Properties();
```

## What?

Creates an empty `Properties` object.

`Properties` is a Java collection designed to store key-value pairs.

Initially:

    PROPERTIES

    (empty)

After loading:

    browser        -> chrome
    baseUrl        -> https://demo.com
    explicitWait   -> 20
    implicitWait   -> 5

## Why private?

Only `ConfigReader` should directly access or modify the properties
object.

Every other class should use getter methods.

## Why static?

Only one configuration is needed for the entire framework execution.

## Why final?

The reference should never point to another `Properties` object.

The object contents can still be modified internally.

------------------------------------------------------------------------

# Part 3 -- Static Initialization Block

``` java
static {
    ...
}
```

## What?

Executes automatically once when the JVM loads the `ConfigReader` class.

Execution order:

    Class Loading
          │
          ▼
    Create empty Properties object
          │
          ▼
    Execute static block
          │
          ▼
    Load config.properties
          │
          ▼
    Store values in memory

The file is loaded only once.

------------------------------------------------------------------------

# Part 4 -- Locate config.properties

``` java
ConfigReader.class
        .getClassLoader()
        .getResourceAsStream("config.properties")
```

## What?

Uses the ClassLoader that loaded `ConfigReader` to find the resource
named `config.properties` from the application's classpath.

## Breakdown

### ConfigReader.class

Returns the `Class` object representing `ConfigReader`.

No object is created.

### getClassLoader()

Returns the ClassLoader responsible for loading this class.

### getResourceAsStream()

Searches the classpath for `config.properties` and returns an
`InputStream`.

Think of it as:

    ConfigReader.class
          │
          ▼
    ClassLoader
          │
          ▼
    Find config.properties
          │
          ▼
    InputStream

------------------------------------------------------------------------

# Part 5 -- Validate Resource

``` java
if (inputStream == null)
```

## What?

Checks whether the file could be found.

**Important**

`null` does **not** mean the file is empty.

It means the file could not be located on the classpath.

If missing:

    RuntimeException

    config.properties file not found

------------------------------------------------------------------------

# Part 6 -- Load Properties

``` java
PROPERTIES.load(inputStream);
```

Reads every key-value pair from the file into memory.

Example:

``` properties
browser=chrome
baseUrl=https://demo.com
explicitWait=20
implicitWait=5
```

Memory becomes:

    browser        -> chrome
    baseUrl        -> https://demo.com
    explicitWait   -> 20
    implicitWait   -> 5

After this step the framework no longer needs to read the file again.

------------------------------------------------------------------------

# Part 7 -- Exception Handling

``` java
catch (Exception e)
```

If loading fails:

``` java
throw new RuntimeException(
    "Failed to load config.properties",
    e
);
```

Passing `e` preserves the original stack trace.

------------------------------------------------------------------------

# Part 8 -- Private Constructor

``` java
private ConfigReader() {
}
```

## Why?

No object is ever required.

Everything is accessed statically:

``` java
ConfigReader.getBrowser();
ConfigReader.getBaseUrl();
```

Preventing object creation makes the intent clear.

------------------------------------------------------------------------

# Part 9 -- Generic Getter

``` java
public static String getProperty(String key)
```

Purpose:

Return any property by its key.

Example:

``` java
ConfigReader.getProperty("browser");
```

Execution:

    browser
       │
       ▼
    PROPERTIES
       │
       ▼
    chrome

This method is the central access point.

------------------------------------------------------------------------

# Part 10 -- Specialized Getters

``` java
public static String getBrowser()
```

Instead of

``` java
ConfigReader.getProperty("browser");
```

we can write

``` java
ConfigReader.getBrowser();
```

The same pattern is used for:

-   getBaseUrl()
-   getExplicitWait()
-   getImplicitWait()

These improve readability.

------------------------------------------------------------------------

# Part 11 -- Integer Conversion

Properties files always store values as text.

Example:

``` properties
explicitWait=20
```

Java reads:

    "20"

Selenium APIs require integers.

Therefore:

``` java
Integer.parseInt(getProperty("explicitWait"));
```

converts

    "20"

    ↓

    20

Now it can be passed directly into:

``` java
Duration.ofSeconds(...)
```

------------------------------------------------------------------------

# Complete Execution Flow

    First call

    ConfigReader.getBrowser()

            │
            ▼

    JVM loads ConfigReader

            │
            ▼

    Create Properties object

            │
            ▼

    Execute static block

            │
            ▼

    Locate config.properties

            │
            ▼

    Load key-value pairs

            │
            ▼

    Store in memory

            │
            ▼

    Return requested property

            │
            ▼

    Subsequent calls use memory only

------------------------------------------------------------------------

# Design Decisions

  Decision              Reason
  --------------------- -------------------------------
  final class           Prevent inheritance
  private constructor   Prevent object creation
  static Properties     One shared configuration
  static block          Load only once
  private PROPERTIES    Encapsulation
  Generic getter        Centralized access
  Specialized getters   Readability
  Integer parsing       Selenium APIs require numbers

------------------------------------------------------------------------

# Interview Takeaways

1.  `ConfigReader` follows the Single Responsibility Principle.
2.  Configuration is loaded only once using a static block.
3.  `Properties` stores key-value pairs in memory.
4.  `ConfigReader.class.getClassLoader()` locates resources on the
    classpath.
5.  `inputStream == null` means the file was not found, not that it is
    empty.
6.  The class is a utility class with a private constructor.
7.  Static methods allow access without creating objects.
8.  Specialized getters improve readability over repeated string keys.
9.  Configuration is cached in memory, avoiding repeated file access.
10. Every framework component retrieves configuration through
    `ConfigReader`, making it the single source of truth.

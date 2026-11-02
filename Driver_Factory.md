# DriverFactory.java -- Complete Learning Notes

## Purpose

`DriverFactory` is responsible for the complete lifecycle of Selenium
WebDriver instances.

Its responsibilities are:

-   Create a browser instance.
-   Configure the browser.
-   Store the browser for the current execution thread.
-   Return the correct browser whenever required.
-   Close the browser after execution.
-   Clean up thread-local storage.

It does **not**: - Read test data - Execute test cases - Interact with
page objects - Generate reports

------------------------------------------------------------------------

# Complete Source

``` java
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        if (DRIVER.get() != null) {
            return;
        }

        String browser = ConfigReader.getBrowser();
        if (browser == null || browser.isBlank()) {
            browser = "chrome";
        }

        WebDriver driver;
        switch (browser.trim().toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();

        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                "WebDriver is not initialized. Call DriverFactory.initDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
```

------------------------------------------------------------------------

# Part 1 -- Class Declaration

``` java
public final class DriverFactory
```

## What?

Defines a utility class responsible for driver management.

## Why final?

Prevents inheritance.

The framework should have only one implementation of driver management.

------------------------------------------------------------------------

# Part 2 -- ThreadLocal Driver Storage

``` java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
```

## What?

Creates a thread-local storage capable of storing one WebDriver per
thread.

**Important:**

It does **not** create Chrome or Firefox.

It only creates storage.

Initially:

    Thread 1 -> null
    Thread 2 -> null
    Thread 3 -> null

## Why not a normal variable?

Bad approach:

``` java
private static WebDriver DRIVER;
```

With parallel execution:

    Thread 1
    ↓

    DRIVER = Chrome #1

    Thread 2
    ↓

    DRIVER = Chrome #2

    Thread 1 continues
    ↓

    Uses Chrome #2

Threads overwrite the shared variable.

This creates race conditions.

ThreadLocal solves this.

Internally think of it as:

    Thread 1 -> Chrome #1
    Thread 2 -> Chrome #2
    Thread 3 -> Chrome #3

Each thread owns its own browser.

------------------------------------------------------------------------

# Part 3 -- Private Constructor

``` java
private DriverFactory() {}
```

## Why?

No object is ever required.

We always use:

``` java
DriverFactory.initDriver();
DriverFactory.getDriver();
DriverFactory.quitDriver();
```

Therefore object creation is prevented.

------------------------------------------------------------------------

# Part 4 -- initDriver()

Purpose:

Create a browser only if the current thread does not already own one.

## Stage 1 -- Duplicate Check

``` java
if (DRIVER.get() != null) {
    return;
}
```

Checks whether the current thread already has a browser.

Prevents opening multiple browsers for the same thread.

------------------------------------------------------------------------

## Stage 2 -- Read Browser Configuration

``` java
String browser = ConfigReader.getBrowser();
```

Reads the browser value from `config.properties`.

Example:

``` properties
browser=chrome
```

Fallback:

``` java
if(browser == null || browser.isBlank()){
    browser = "chrome";
}
```

If configuration is missing, Chrome is used.

------------------------------------------------------------------------

## Stage 3 -- Browser Creation

``` java
switch(browser.trim().toLowerCase())
```

### trim()

Removes accidental spaces.

    " chrome "
    ↓

    "chrome"

### toLowerCase()

Allows:

    Chrome
    CHROME
    chrome

to become:

    chrome

### WebDriverManager

``` java
WebDriverManager.chromedriver().setup();
```

Automatically downloads and configures the correct browser driver.

### Browser Options

``` java
ChromeOptions chromeOptions = new ChromeOptions();
```

Configuration applied **before** browser launch.

Example:

``` java
chromeOptions.addArguments("--start-maximized");
chromeOptions.addArguments("--disable-notifications");
```

Finally:

``` java
driver = new ChromeDriver(chromeOptions);
```

Browser starts.

------------------------------------------------------------------------

## Stage 4 -- Driver Configuration

``` java
driver.manage().timeouts().implicitlyWait(...)
```

How long Selenium searches for elements.

------------------------------------------------------------------------

``` java
driver.manage().timeouts().pageLoadTimeout(...)
```

Maximum page loading time.

------------------------------------------------------------------------

``` java
driver.manage().window().maximize();
```

Ensures browser starts maximized.

------------------------------------------------------------------------

## Stage 5 -- Store Driver

``` java
DRIVER.set(driver);
```

Stores the browser for the current thread.

Conceptually:

    Thread 1 -> Chrome #1
    Thread 2 -> Chrome #2
    Thread 3 -> Chrome #3

------------------------------------------------------------------------

# Part 5 -- getDriver()

``` java
public static WebDriver getDriver()
```

Returns the browser belonging to the current thread.

``` java
WebDriver driver = DRIVER.get();
```

If driver is missing:

``` java
throw new IllegalStateException(...);
```

Provides a meaningful error instead of a NullPointerException.

------------------------------------------------------------------------

# Part 6 -- quitDriver()

Purpose:

Safely destroy the browser.

``` java
driver.quit();
```

Closes:

-   Browser window
-   WebDriver session
-   Driver process

------------------------------------------------------------------------

## Why DRIVER.remove()?

``` java
DRIVER.remove();
```

Even after closing the browser, the ThreadLocal storage still remembers
the reference.

If TestNG reuses the same thread for another test:

    Thread 2

    ↓

    Old WebDriver reference

Calling `remove()` clears the storage:

    Thread 2

    ↓

    null

The next test starts with a clean thread.

------------------------------------------------------------------------

# Complete Execution Flow

    Test Starts
        │
        ▼
    DriverFactory.initDriver()
        │
        ▼
    Read browser from ConfigReader
        │
        ▼
    Create browser
        │
        ▼
    Configure browser
        │
        ▼
    Store inside ThreadLocal
        │
        ▼
    Tests execute
        │
        ▼
    DriverFactory.getDriver()
        │
        ▼
    Returns current thread's browser
        │
        ▼
    DriverFactory.quitDriver()
        │
        ▼
    driver.quit()
        │
        ▼
    DRIVER.remove()
        │
        ▼
    Thread becomes clean

# Key Interview Takeaways

1.  DriverFactory has a single responsibility: manage WebDriver
    lifecycle.
2.  `ThreadLocal<WebDriver>` provides one browser per execution thread.
3.  `initDriver()` prevents duplicate browser creation.
4.  Browser selection comes from `ConfigReader`.
5.  Browser options are configured before launching the browser.
6.  `getDriver()` safely returns the current thread's browser.
7.  `quitDriver()` closes the browser and clears ThreadLocal storage.
8.  `DRIVER.remove()` is essential for thread reuse during parallel
    execution.

# WaitUtils.java -- Complete Learning Notes

## Purpose

`WaitUtils` centralizes all explicit wait operations used by the
automation framework.

Its responsibilities are:

-   Create a configured `WebDriverWait`.
-   Wait for common browser and element conditions.
-   Eliminate duplicate waiting logic.
-   Encourage explicit waits instead of `Thread.sleep()`.

It does **not**:

-   Locate drivers.
-   Read test data.
-   Execute tests.
-   Interact with page business logic.

------------------------------------------------------------------------

# Complete Source

``` java
public final class WaitUtils {

    private WaitUtils() {
    }

    private static WebDriverWait newWait(WebDriver driver) {
        return new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return newWait(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return newWait(driver)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForUrlContains(WebDriver driver, String fraction) {
        return newWait(driver)
                .until(ExpectedConditions.urlContains(fraction));
    }

    public static boolean waitForTitleContains(WebDriver driver, String title) {
        return newWait(driver)
                .until(ExpectedConditions.titleContains(title));
    }

    public static boolean waitForAttributeToBe(
            WebDriver driver,
            By locator,
            String attribute,
            String value) {

        return newWait(driver)
                .until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }
}
```

------------------------------------------------------------------------

# Overall Design

    Page Object

    ↓

    WaitUtils

    ↓

    WebDriverWait

    ↓

    ExpectedConditions

    ↓

    Browser

Every page uses the same waiting strategy.

------------------------------------------------------------------------

# Part 1 -- Class Declaration

``` java
public final class WaitUtils
```

## Why `final`?

This is a utility class that provides reusable helper methods.

Inheritance is unnecessary.

------------------------------------------------------------------------

# Part 2 -- Private Constructor

``` java
private WaitUtils() {
}
```

## Why?

Objects are never created.

Usage:

``` java
WaitUtils.waitForVisible(driver, locator);
```

This is a classic static utility class.

------------------------------------------------------------------------

# Part 3 -- newWait()

``` java
private static WebDriverWait newWait(WebDriver driver)
```

## Purpose

Create a fresh `WebDriverWait` configured with the framework timeout.

``` java
new WebDriverWait(
    driver,
    Duration.ofSeconds(ConfigReader.getExplicitWait())
);
```

## Why use ConfigReader?

The timeout is controlled from:

``` properties
explicitWait=20
```

Changing the configuration changes every wait automatically.

No code modifications are required.

## Why a helper method?

Without it, every wait method would duplicate:

``` java
new WebDriverWait(driver,
    Duration.ofSeconds(ConfigReader.getExplicitWait()));
```

Centralizing this improves maintainability.

------------------------------------------------------------------------

# Part 4 -- waitForVisible()

``` java
waitForVisible(driver, locator)
```

Internally:

    Create WebDriverWait

    ↓

    Repeatedly check

    ↓

    visibilityOfElementLocated(locator)

    ↓

    Return WebElement

## What does "visible" mean?

-   Element exists in the DOM.
-   Element is displayed.
-   Element has non-zero size.

Useful before typing or reading text.

------------------------------------------------------------------------

# Part 5 -- waitForClickable()

``` java
waitForClickable(driver, locator)
```

Waits until Selenium considers the element clickable.

Typically means:

-   Visible
-   Enabled

Useful before clicking buttons and links.

------------------------------------------------------------------------

# Part 6 -- waitForUrlContains()

``` java
waitForUrlContains(driver, "dashboard")
```

Waits until the current URL contains the supplied text.

Example:

Current URL:

    https://site/app/dashboard

Condition:

    dashboard

Result:

    true

Useful after login or navigation.

------------------------------------------------------------------------

# Part 7 -- waitForTitleContains()

``` java
waitForTitleContains(driver, "Dashboard")
```

Waits until the browser title contains the expected text.

Useful for verifying page transitions.

------------------------------------------------------------------------

# Part 8 -- waitForAttributeToBe()

``` java
waitForAttributeToBe(driver,
                     locator,
                     "aria-disabled",
                     "false");
```

Waits until a specific attribute has the expected value.

Useful for dynamic UIs where controls become enabled after loading.

------------------------------------------------------------------------

# Why Explicit Waits?

Instead of:

``` java
Thread.sleep(5000);
```

the framework does:

    Check condition

    ↓

    Satisfied?

    ↓

    Yes → Continue immediately

    No → Keep polling until timeout

Benefits:

-   Faster execution.
-   Less flaky tests.
-   No unnecessary waiting.

------------------------------------------------------------------------

# Why not rely on Implicit Wait?

Implicit wait only helps locate elements.

Explicit waits support rich conditions such as:

-   Visibility
-   Clickability
-   URL changes
-   Title changes
-   Attribute values

This makes them much more flexible.

------------------------------------------------------------------------

# Complete Execution Flow

    Page calls WaitUtils

    ↓

    newWait()

    ↓

    Read explicitWait from ConfigReader

    ↓

    Create WebDriverWait

    ↓

    Poll browser

    ↓

    Condition satisfied

    ↓

    Return WebElement / Boolean

------------------------------------------------------------------------

# Design Decisions

  Decision               Reason
  ---------------------- -----------------------------------------
  final class            Utility helper
  private constructor    Prevent object creation
  static methods         Framework-wide access
  newWait() helper       Avoid duplicated WebDriverWait creation
  ConfigReader timeout   Centralized configuration
  ExpectedConditions     Reliable synchronization
  Explicit waits         Replace Thread.sleep()

------------------------------------------------------------------------

# Interview Takeaways

1.  `WaitUtils` centralizes synchronization logic.
2.  `WebDriverWait` is created using the framework-configured timeout.
3.  `ConfigReader` controls explicit wait duration.
4.  Explicit waits poll until a condition is met or times out.
5.  `visibilityOfElementLocated()` returns a visible `WebElement`.
6.  `elementToBeClickable()` waits for visibility and enabled state.
7.  Explicit waits are preferred over `Thread.sleep()` because they are
    faster and more reliable.
8.  Centralizing waits improves maintainability and reduces flaky
    automation.

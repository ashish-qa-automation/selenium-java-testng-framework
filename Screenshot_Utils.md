# ScreenshotUtils.java -- Complete Learning Notes

## Purpose

`ScreenshotUtils` is a utility class responsible for capturing
screenshots during test execution, primarily for failure analysis and
reporting.

Its responsibilities are:

-   Capture screenshots from the current browser.
-   Create the screenshots directory if needed.
-   Generate unique file names.
-   Save screenshots to disk.
-   Return the saved file path.

It does **not**:

-   Decide when screenshots should be taken.
-   Execute tests.
-   Manage WebDriver lifecycle.
-   Generate reports (it only provides screenshot files).

------------------------------------------------------------------------

# Complete Source

``` java
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

            String safeName = testName == null
                    ? "screenshot"
                    : testName.replaceAll("[^a-zA-Z0-9-_]", "_");

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            Path destination = SCREENSHOT_DIR.resolve(
                    safeName + "_" + timestamp + ".png");

            File source = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            Files.copy(source.toPath(), destination,
                    StandardCopyOption.REPLACE_EXISTING);

            return destination.toAbsolutePath().toString();

        } catch (IOException | RuntimeException e) {
            throw new RuntimeException(
                    "Failed to capture screenshot for: " + testName, e);
        }
    }
}
```

------------------------------------------------------------------------

# Overall Design

Typical execution flow:

    Test Fails
          │
          ▼
    TestListener
          │
          ▼
    ScreenshotUtils.capture()
          │
          ▼
    DriverFactory.getDriver()
          │
          ▼
    Capture Browser Screenshot
          │
          ▼
    Save PNG
          │
          ▼
    Return File Path

------------------------------------------------------------------------

# Part 1 -- Class Declaration

``` java
public final class ScreenshotUtils
```

## Why `final`?

The framework should have one implementation of screenshot capture.

This is a utility class rather than a business object.

------------------------------------------------------------------------

# Part 2 -- Screenshot Directory

``` java
private static final Path SCREENSHOT_DIR = Paths.get("screenshots");
```

## What?

Represents the folder where screenshots will be stored.

Using `Path` instead of `String` provides a modern, platform-independent
API.

Initially:

    Project

    (no screenshots folder)

After first capture:

    Project
    └── screenshots

## Why `static final`?

-   One shared directory for the framework.
-   Directory location never changes during execution.

------------------------------------------------------------------------

# Part 3 -- Private Constructor

``` java
private ScreenshotUtils() {
}
```

## Why?

No object is required.

Everything is accessed statically:

``` java
ScreenshotUtils.capture("LoginTest");
```

This clearly communicates that the class provides utility functions.

------------------------------------------------------------------------

# Part 4 -- Convenience Method

``` java
public static String capture(String testName)
```

## What?

Retrieves the current thread's WebDriver automatically.

``` java
WebDriver driver = DriverFactory.getDriver();
```

Then delegates to:

``` java
capture(driver, testName);
```

## Why?

Most callers already use the current framework driver.

This avoids repeating:

``` java
DriverFactory.getDriver()
```

everywhere.

------------------------------------------------------------------------

# Part 5 -- Main Capture Method

``` java
public static String capture(WebDriver driver, String testName)
```

This method performs the real work.

------------------------------------------------------------------------

## Step 1 -- Create Directory

``` java
Files.createDirectories(SCREENSHOT_DIR);
```

Creates the screenshots directory if it doesn't already exist.

Unlike `createDirectory()`, this method does nothing if the folder
already exists.

------------------------------------------------------------------------

## Step 2 -- Safe File Name

``` java
testName.replaceAll("[^a-zA-Z0-9-_]", "_");
```

## Why?

Many characters are illegal or problematic in filenames.

Example:

    Login Test #1

    ↓

    Login_Test__1

If `testName` is null:

    screenshot

is used.

------------------------------------------------------------------------

## Step 3 -- Timestamp

``` java
LocalDateTime.now()
```

Current date and time.

Formatted as:

``` java
yyyyMMdd_HHmmss
```

Example:

    20260806_183045

## Why?

Without timestamps:

    LoginTest.png

would be overwritten every execution.

With timestamps:

    LoginTest_20260806_183045.png
    LoginTest_20260806_183327.png

Every screenshot is unique.

------------------------------------------------------------------------

## Step 4 -- Destination Path

``` java
SCREENSHOT_DIR.resolve(...)
```

Creates the complete path.

Example:

    screenshots/LoginTest_20260806_183045.png

`resolve()` is preferred over manually concatenating file separators.

------------------------------------------------------------------------

## Step 5 -- Capture Screenshot

``` java
((TakesScreenshot) driver)
```

## Why cast?

`WebDriver` itself does not declare:

``` java
getScreenshotAs(...)
```

The `TakesScreenshot` interface does.

The browser driver implements both interfaces, so the cast exposes
screenshot functionality.

------------------------------------------------------------------------

``` java
getScreenshotAs(OutputType.FILE)
```

Creates a temporary image file.

Example:

    Temp File

    ↓

    C:\Temp\abc123.png

------------------------------------------------------------------------

# Part 6 -- Save Screenshot

``` java
Files.copy(...)
```

Copies the temporary screenshot into the framework's screenshots folder.

``` java
StandardCopyOption.REPLACE_EXISTING
```

Allows replacement if a file with the same name already exists.

Because timestamps are used, replacement rarely occurs, but including
the option makes the operation safe.

------------------------------------------------------------------------

# Part 7 -- Return Path

``` java
return destination.toAbsolutePath().toString();
```

Returns the full file path.

Example:

    C:\Automation\screenshots\LoginTest_20260806_183045.png

Why?

Reporting tools such as Allure or Extent Reports can use this path to
attach screenshots.

------------------------------------------------------------------------

# Part 8 -- Exception Handling

``` java
catch (IOException | RuntimeException e)
```

Handles:

-   Directory creation failures
-   File copy failures
-   Screenshot capture failures
-   Driver-related runtime exceptions

Wrapping the exception with:

``` java
throw new RuntimeException(..., e);
```

adds context while preserving the original stack trace.

------------------------------------------------------------------------

# Complete Execution Flow

    capture("LoginTest")
            │
            ▼
    DriverFactory.getDriver()
            │
            ▼
    Create screenshots folder
            │
            ▼
    Generate safe filename
            │
            ▼
    Generate timestamp
            │
            ▼
    Build destination path
            │
            ▼
    Capture temporary screenshot
            │
            ▼
    Copy to screenshots folder
            │
            ▼
    Return absolute path

------------------------------------------------------------------------

# Design Decisions

  Decision               Reason
  ---------------------- --------------------------------------------
  final class            Utility class, no inheritance
  private constructor    Prevent object creation
  static methods         Easy framework-wide access
  Path API               Modern, platform-independent file handling
  createDirectories()    Safe repeated execution
  Safe filename          Avoid invalid filename characters
  Timestamp              Prevent overwriting screenshots
  TakesScreenshot        Selenium screenshot capability
  Return absolute path   Easy integration with reporting tools

------------------------------------------------------------------------

# Interview Takeaways

1.  `ScreenshotUtils` has a single responsibility: capture and save
    screenshots.
2.  The class is a static utility with a private constructor.
3.  `DriverFactory.getDriver()` retrieves the current thread's browser.
4.  `Files.createDirectories()` safely creates the screenshot folder
    only when needed.
5.  Filenames are sanitized to avoid invalid operating system
    characters.
6.  Timestamps ensure every screenshot has a unique name.
7.  `WebDriver` is cast to `TakesScreenshot` because screenshot
    functionality is defined by that interface.
8.  The method returns the absolute file path so reporting frameworks
    can attach the screenshot easily.

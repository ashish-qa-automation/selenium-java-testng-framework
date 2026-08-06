# selenium-java-testng-framework

Scalable Selenium + Java + TestNG UI automation framework for [Sauce Demo](https://www.saucedemo.com/), built with Page Object Model.

## Stack

- Selenium WebDriver 4
- TestNG (parallel methods, retry analyzer)
- Maven
- Allure reporting
- ThreadLocal WebDriver for safe parallelism

## Prerequisites

- JDK 11+
- Maven 3.8+
- Google Chrome

## Run tests

```powershell
# Smoke (parallel, default suite)
mvn clean test

# Explicit smoke
mvn clean test "-DsuiteXmlFile=src/test/resources/testng-smoke.xml"

# Regression (smoke + broader coverage)
mvn clean test "-DsuiteXmlFile=src/test/resources/testng-regression.xml"
```

Suites use `parallel="methods"` with `thread-count="2"`. Failed tests retry once via `RetryAnalyzer`.

## Allure report

After a test run:

```powershell
# Generate HTML report under target/allure-report
mvn allure:report

# Or open an interactive local server
mvn allure:serve
```

Failure screenshots are saved under `screenshots/` and attached to Allure.

## Project layout

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for design decisions, page map, and roadmap.

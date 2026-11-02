# selenium-java-testng-framework

[![CI](https://github.com/ashish-qa-automation/selenium-java-testng-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/ashish-qa-automation/selenium-java-testng-framework/actions/workflows/ci.yml)

Public, recruiter-ready **Selenium + Java + TestNG** UI framework for [Sauce Demo](https://www.saucedemo.com/). It shows how I design automation from scratch — not client/NDA code.

**Portfolio:** [ashishautomation.com](https://www.ashishautomation.com)

A hiring manager should be able to clone this repo, run smoke, and open a report in about **15 minutes**.

## What this demonstrates

- Page Object Model with locators and actions in pages, assertions in tests
- ThreadLocal WebDriver for parallel TestNG (`parallel="methods"`)
- Explicit waits only (`implicitWait=0`)
- External config with system-property / env overrides (`-Dheadless=true`)
- Data-driven login via TestNG DataProvider
- Failure screenshots attached to Allure
- GitHub Actions smoke on every push/PR to `main`

Design contract: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)

## Stack

- Java 11 · Maven · Selenium 4 · TestNG
- Allure reporting · WebDriverManager · GitHub Actions

## Prerequisites

- JDK 11+
- Maven 3.8+
- Google Chrome

## Run in ~15 minutes

```powershell
git clone https://github.com/ashish-qa-automation/selenium-java-testng-framework.git
cd selenium-java-testng-framework

# Smoke (default) — login, cart, checkout, logout
mvn clean test

# Same run, headless (what CI uses)
mvn clean test "-Dheadless=true"

# Broader coverage
mvn clean test -Pregression

# HTML report
mvn allure:report
# or: mvn allure:serve
```

| Command | What runs |
|---------|-----------|
| `mvn clean test` or `-Psmoke` | Smoke suite (critical path) |
| `mvn clean test -Pregression` | Smoke + regression |
| `mvn clean test "-Dheadless=true"` | Headless Chrome |

Suites use `parallel="methods"` with `thread-count="2"`. Failed tests retry once.

Failure screenshots land in `screenshots/` and are attached to Allure (`target/allure-report`).

## Coverage

**Smoke:** valid login · add to cart · checkout happy path · logout

**Regression:** locked-out user · invalid password · data-driven login · multi-item cart/remove · sort Z–A · empty checkout-info validation

## CI

GitHub Actions runs **smoke** on every push and pull request to `main` (headless Chrome). Allure results, the HTML report, and failure screenshots are uploaded as workflow artifacts.

## Layout

```text
src/main/java/com/automation/
  config/     ConfigReader
  driver/     ThreadLocal DriverFactory
  pages/      BasePage + Sauce Demo screens
  utils/      WaitUtils, ScreenshotUtils
src/test/java/com/automation/
  base/       BaseTest lifecycle
  listeners/  Allure + screenshot on fail, retry
  data/       LoginDataProvider
  tests/      Login, Cart, Checkout
```

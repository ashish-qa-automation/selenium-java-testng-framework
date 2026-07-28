# Architecture Blueprint — Phase 1A

**Application under test:** [Sauce Demo](https://www.saucedemo.com/)  
**Framework:** Selenium WebDriver + Java + TestNG + Maven + Page Object Model  
**Goal:** Public, recruiter-ready UI automation sample that demonstrates senior framework design (not NDA client code).

---

## 1. Purpose

This repository proves the ability to:

- Design a modular, reusable UI automation framework from scratch
- Apply Page Object Model with a clean utility layer
- Support parallel execution and CI-ready test runs
- Produce readable reports and failure artifacts

Sauce Demo is used as a stable public target. Enterprise VMS work remains private/NDA and is described only as a case study on the portfolio site.

---

## 2. Scope

### In scope (Phase 1)

| Area | Coverage |
|------|----------|
| Authentication | Valid login, locked-out user, invalid credentials |
| Inventory | Product list visibility, add to cart, sort (optional) |
| Cart | Item present, proceed to checkout |
| Checkout | Customer info → overview → complete |
| Session | Logout via menu |

---

## 3. Test suites

| Suite | Purpose | Approx. tests | When to run |
|-------|---------|---------------|-------------|
| **Smoke** | Critical path confidence | 3–5 | Every PR / CI |
| **Regression** | Broader functional coverage | 8–12 | Nightly / pre-release |

### Planned smoke cases

1. Login as `standard_user` → land on inventory
2. Add one product to cart → cart count / cart page
3. Complete checkout happy path
4. Logout successfully

### Planned regression extras

5. Locked-out user shows error
6. Invalid password shows error
7. Add multiple products / remove from cart
8. Product sort (A–Z or price)
9. Checkout validation (empty required fields)
10. Data-driven login (valid + invalid sets)

---

## 4. Design decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Pattern | **Page Object Model** | Clear separation of locators/actions vs tests |
| Waits | **Explicit waits only** | Avoid `Thread.sleep` and fragile implicit-wait-only designs |
| Driver | **ThreadLocal + DriverFactory** | Safe parallel TestNG execution |
| Config | **`config.properties`** | Environment-friendly (URL, browser, timeouts) |
| Assertions | **In test classes** | Pages expose state/actions; tests own expected results |
| Reporting | **Allure** (preferred) or Extent | Pick one in Phase 1F and stick to it |
| Parallel | TestNG `parallel="methods"` | Demonstrate senior concurrency awareness |
| Flake handling | Retry analyzer (optional Phase 1F) | Classify and limit retries |

---

## 5. Target package structure

```text
selenium-java-testng-framework/
├── pom.xml
├── README.md
├── docs/
│   └── ARCHITECTURE.md          ← this file (Phase 1A)
├── src/
│   ├── main/java/com/automation/
│   │   ├── config/
│   │   │   └── ConfigReader.java
│   │   ├── driver/
│   │   │   └── DriverFactory.java
│   │   ├── pages/
│   │   │   ├── BasePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── InventoryPage.java
│   │   │   ├── CartPage.java
│   │   │   ├── CheckoutInfoPage.java
│   │   │   ├── CheckoutOverviewPage.java
│   │   │   └── CheckoutCompletePage.java
│   │   ├── utils/
│   │   │   ├── WaitUtils.java
│   │   │   └── ScreenshotUtils.java
│   └── test/
│       ├── java/com/automation/
│       │   ├── base/
│       │   │   └── BaseTest.java
│       │   ├── listeners/
│       │   │   ├── TestListener.java
│       │   │   └── RetryAnalyzer.java      (optional)
│       │   ├── tests/
│       │   │   ├── LoginTests.java
│       │   │   ├── CartTests.java
│       │   │   └── CheckoutTests.java
│       │   └── data/
│       │       └── LoginDataProvider.java
│       └── resources/
│           ├── config.properties
│           ├── testng-smoke.xml
│           └── testng-regression.xml
└── .github/workflows/
    └── ci.yml                       (Phase 1G)
```

> **Note:** Phase 1B created the `com.automation.*` skeleton. Phase 1C implemented `DriverFactory`, `WaitUtils`, `ScreenshotUtils`, `BasePage`, `BaseTest`, and `FrameworkSmokeTest` (open Sauce Demo + quit). Listeners live under `src/test/java` (TestNG is test-scoped). Page objects and business tests land in Phases 1D–1E.

---

## 6. Component responsibilities

| Component | Responsibility |
|-----------|----------------|
| `ConfigReader` | Load browser, `baseUrl`, timeouts from properties |
| `DriverFactory` | Create/quit WebDriver; store in `ThreadLocal` |
| `BasePage` | Shared click, type, getText, isDisplayed using waits |
| Page classes | Locators + user actions for one screen |
| `BaseTest` | `@BeforeMethod` / `@AfterMethod` driver lifecycle |
| `WaitUtils` | Explicit wait helpers (visible, clickable, URL contains) |
| `ScreenshotUtils` | Capture on failure |
| `TestListener` | Hook failures → screenshot / report attachment |
| Test classes | Arrange → act → assert business outcomes |
| Data providers | Parameterized inputs for login / checkout |

---

## 7. Page map (Sauce Demo)

```text
LoginPage
    │  successful login
    ▼
InventoryPage ──add to cart──► CartPage
    │                              │
    │ logout (menu)                │ checkout
    ▼                              ▼
  (LoginPage)              CheckoutInfoPage
                                   │
                                   ▼
                           CheckoutOverviewPage
                                   │
                                   ▼
                           CheckoutCompletePage
```

| Page | Key actions |
|------|-------------|
| `LoginPage` | `login(user, pass)`, `getErrorMessage()` |
| `InventoryPage` | `addProductToCart(name)`, `openCart()`, `sortBy(...)` |
| `CartPage` | `getCartItemNames()`, `clickCheckout()` |
| `CheckoutInfoPage` | `fillInfo(first, last, zip)`, `continueCheckout()` |
| `CheckoutOverviewPage` | `finish()` |
| `CheckoutCompletePage` | `getConfirmationMessage()` |

---

## 8. Configuration contract

`src/test/resources/config.properties` (target values):

```properties
browser=chrome
baseUrl=https://www.saucedemo.com/
implicitWait=0
explicitWait=15
```

| Key | Meaning |
|-----|---------|
| `browser` | `chrome` (default); Edge later |
| `baseUrl` | Sauce Demo entry URL |
| `implicitWait` | Prefer `0` when using explicit waits |
| `explicitWait` | Default timeout (seconds) for WaitUtils |

Credentials for public demo users stay in tests/data providers (not secrets):

| User | Password | Intent |
|------|----------|--------|
| `standard_user` | `secret_sauce` | Happy path |
| `locked_out_user` | `secret_sauce` | Negative login |
| `problem_user` | `secret_sauce` | Optional regression |

---

## 9. Execution model

```text
mvn clean test -DsuiteXmlFile=src/test/resources/testng-smoke.xml
mvn clean test -DsuiteXmlFile=src/test/resources/testng-regression.xml
```

CI (Phase 1G) runs **smoke** on every push/PR to `main`.

Parallel (Phase 1F):

```xml
<suite name="Smoke" parallel="methods" thread-count="2">
```

---

## 10. Quality rules (non-negotiable)

1. No locators inside test classes  
2. No `Thread.sleep`  
3. No shared static WebDriver across threads  
4. Pages do not assert business outcomes (tests do)  
5. Failures produce a screenshot  
6. README documents architecture + how to run  

---

## 11. Implementation roadmap

| Sub-phase | Focus | Exit criteria |
|-----------|--------|---------------|
| **1A** | This blueprint | Architecture agreed and documented |
| **1B** | Folder structure + `pom.xml` + config | Project compiles; packages exist |
| **1C** | DriverFactory, ConfigReader, BaseTest, waits | Browser opens Sauce Demo and quits cleanly |
| **1D** | Page objects | Login → Inventory → Cart → Checkout pages |
| **1E** | Tests + suites | 5–8+ tests green via Maven |
| **1F** | Parallel + reporting | Report generated; parallel smoke works |
| **1G** | GitHub Actions | CI badge green on README |
| **1H** | Portfolio wiring | Site/profile point at runnable repo |

---

## 12. Success definition (end of Phase 1 UI track)

A hiring manager can:

1. Open this repo  
2. Read this architecture + README  
3. Run `mvn clean test`  
4. See green smoke results and a report artifact  

within **~15 minutes**.

---

*Phase 1A complete when this document is accepted as the build contract for sub-phases 1B onward.*

# Cursor project rules

These rules teach Cursor how **this** Selenium Java TestNG framework actually works. They are engineering context and constraints, not a guarantee of correct tests. Review and execution remain mandatory.

Rules live in `.cursor/rules/*.mdc`. Each file has YAML frontmatter (`description`, optional `globs`, `alwaysApply`).

## When rules apply

| Mode | Meaning |
|------|---------|
| **Always** | Attached to every chat in this repo |
| **Glob** | Attached when matching files are in context |
| **Requestable** | Attached when the task matches `description` (review/validation) |

Always-apply is limited to project-wide constraints so specialized Selenium/TestNG guidance stays scoped.

## Rule catalog

### Always apply

| File | Purpose |
|------|---------|
| [`rules/project-context.mdc`](rules/project-context.mdc) | Stack versions, Sauce Demo AUT, Maven commands, config keys, forbidden extra libraries |
| [`rules/framework-architecture.mdc`](rules/framework-architecture.mdc) | Package map, class responsibilities, driver lifecycle, page graph, reuse catalog, missing layers |
| [`rules/ai-code-generation.mdc`](rules/ai-code-generation.mdc) | Inspect-first, reuse, no unrelated edits, debug classification, report what changed and what was verified |

### Glob-scoped (implementation)

| File | Globs | Use when |
|------|-------|----------|
| [`rules/java-coding-standards.mdc`](rules/java-coding-standards.mdc) | `**/*.java` | Naming, Java 11 limits, final utilities, no Lombok |
| [`rules/selenium-best-practices.mdc`](rules/selenium-best-practices.mdc) | `**/*.java` | `DriverFactory`, locators, `BasePage` JS click/type, browsers |
| [`rules/page-object-model.mdc`](rules/page-object-model.mdc) | `src/main/java/com/automation/pages/**/*.java` | New or changed page objects |
| [`rules/testng-execution.mdc`](rules/testng-execution.mdc) | `src/test/**/*.java`, `src/test/resources/testng-*.xml` | Suites, groups, parallel methods, retry, `BaseTest` |
| [`rules/test-data-management.mdc`](rules/test-data-management.mdc) | `com.automation.data`, `config.properties` | DataProviders, config vs credentials |
| [`rules/waits-synchronization.mdc`](rules/waits-synchronization.mdc) | `src/main/java/com/automation/**/*.java` | `WaitUtils`, `implicitWait=0` |
| [`rules/exception-handling-logging.mdc`](rules/exception-handling-logging.mdc) | `**/*.java` | Fail-fast `RuntimeException`, no SLF4J |
| [`rules/reporting-debugging.mdc`](rules/reporting-debugging.mdc) | listeners, `ScreenshotUtils` | Allure, failure PNGs, retry noise |
| [`rules/test-design.mdc`](rules/test-design.mdc) | `src/test/java/com/automation/tests/**/*.java` | Independent `should*` tests, fluent flows |

### Requestable

| File | Use when |
|------|----------|
| [`rules/code-review-validation.mdc`](rules/code-review-validation.mdc) | Reviewing a diff, validating generated tests, or checking Definition of Done |

## Typical tasks → rules

| Engineer asks | Primary rules |
|---------------|----------------|
| New automated scenario | always-apply + `test-design` + `page-object-model` + `testng-execution` |
| New page object | `page-object-model` + `selenium-best-practices` + `waits-synchronization` |
| New DataProvider | `test-data-management` |
| Flaky / failed test | `ai-code-generation` (classify first) + `reporting-debugging` + `waits-synchronization` |
| Refactor `BasePage` / driver | `framework-architecture` + `selenium-best-practices` (explain before changing globally) |
| Review my change | `code-review-validation` |

## Non-goals

Rules must not be used as a reason to:

- Rewrite existing business tests or page workflows unasked
- Edit GitHub Actions
- Add Excel, Extent, Log4j, PageFactory, or new Maven dependencies “for completeness”
- Raise retry count or implicit wait to hide failures

## Source of truth

Prefer the Java sources under `src/` over README prose if they ever drift. Architecture notes: [`docs/ARCHITECTURE.md`](../docs/ARCHITECTURE.md).

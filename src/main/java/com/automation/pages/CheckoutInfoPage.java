package com.automation.pages;

import com.automation.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * Sauce Demo checkout customer-info screen.
 */
public class CheckoutInfoPage extends BasePage {

    private static final By FIRST_NAME_INPUT = By.id("first-name");
    private static final By LAST_NAME_INPUT = By.id("last-name");
    private static final By POSTAL_CODE_INPUT = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By CANCEL_BUTTON = By.id("cancel");
    private static final By ERROR_MESSAGE = By.cssSelector("h3[data-test='error']");

    public boolean isLoaded() {
        return isDisplayed(FIRST_NAME_INPUT);
    }

    public CheckoutInfoPage fillInfo(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME_INPUT, firstName);
        type(LAST_NAME_INPUT, lastName);
        type(POSTAL_CODE_INPUT, postalCode);
        return this;
    }

    public CheckoutOverviewPage continueCheckout() {
        click(CONTINUE_BUTTON);
        return new CheckoutOverviewPage();
    }

    /** Continue when customer info is valid and overview should load. */
    public CheckoutOverviewPage continueCheckoutSuccessfully() {
        click(CONTINUE_BUTTON);
        WaitUtils.waitForUrlContains(driver, "checkout-step-two");
        return new CheckoutOverviewPage();
    }

    public CartPage cancel() {
        click(CANCEL_BUTTON);
        return new CartPage();
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}

package com.automation.pages;

import org.openqa.selenium.By;

/**
 * Sauce Demo order-confirmation screen.
 */
public class CheckoutCompletePage extends BasePage {

    private static final By COMPLETE_HEADER = By.className("complete-header");
    private static final By COMPLETE_TEXT = By.className("complete-text");
    private static final By BACK_HOME_BUTTON = By.id("back-to-products");

    public boolean isLoaded() {
        return isDisplayed(COMPLETE_HEADER);
    }

    public String getConfirmationMessage() {
        return getText(COMPLETE_HEADER);
    }

    public String getConfirmationDetails() {
        return getText(COMPLETE_TEXT);
    }

    public InventoryPage backHome() {
        click(BACK_HOME_BUTTON);
        return new InventoryPage();
    }
}

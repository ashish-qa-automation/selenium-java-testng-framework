package com.automation.pages;

import com.automation.utils.WaitUtils;
import org.openqa.selenium.By;

/**
 * Sauce Demo login screen.
 */
public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("h3[data-test='error']");

    public boolean isLoaded() {
        return isDisplayed(LOGIN_BUTTON);
    }

    public InventoryPage login(String username, String password) {
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        return new InventoryPage();
    }

    /** Login and wait for inventory when credentials are expected to succeed. */
    public InventoryPage loginSuccessfully(String username, String password) {
        login(username, password);
        WaitUtils.waitForUrlContains(driver, "inventory");
        return new InventoryPage();
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}

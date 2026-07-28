package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.data.LoginDataProvider;
import com.automation.driver.DriverFactory;
import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import com.automation.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Login-related Sauce Demo tests.
 */
public class LoginTests extends BaseTest {

    @Test(groups = "smoke", description = "Valid login lands on inventory")
    public void shouldLoginWithValidCredentials() {
        InventoryPage inventoryPage = new LoginPage()
                .loginSuccessfully(LoginDataProvider.STANDARD_USER, LoginDataProvider.PASSWORD);

        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should be visible after login");
    }

    @Test(groups = "smoke", description = "Logout returns to login page")
    public void shouldLogoutSuccessfully() {
        LoginPage loginPage = new LoginPage()
                .loginSuccessfully(LoginDataProvider.STANDARD_USER, LoginDataProvider.PASSWORD)
                .logout();

        Assert.assertTrue(loginPage.isLoaded(), "Login page should be visible after logout");
    }

    @Test(groups = "regression", description = "Locked-out user sees an error")
    public void shouldShowErrorForLockedOutUser() {
        new LoginPage().login(LoginDataProvider.LOCKED_OUT_USER, LoginDataProvider.PASSWORD);

        String error = new LoginPage().getErrorMessage();
        Assert.assertTrue(
                error.contains("Sorry, this user has been locked out"),
                "Expected locked-out error, but was: " + error);
    }

    @Test(groups = "regression", description = "Invalid password shows an error")
    public void shouldShowErrorForInvalidPassword() {
        new LoginPage().login(LoginDataProvider.STANDARD_USER, "wrong_password");

        String error = new LoginPage().getErrorMessage();
        Assert.assertTrue(
                error.contains("Username and password do not match"),
                "Expected invalid credentials error, but was: " + error);
    }

    @Test(
            groups = "regression",
            dataProvider = "loginData",
            dataProviderClass = LoginDataProvider.class,
            description = "Data-driven login success and failure cases")
    public void shouldHandleLoginScenarios(
            String username, String password, boolean expectSuccess, String errorFragment) {
        InventoryPage inventoryPage = new LoginPage().login(username, password);

        if (expectSuccess) {
            Assert.assertTrue(
                    WaitUtils.waitForUrlContains(DriverFactory.getDriver(), "inventory"),
                    "Should navigate to inventory for valid credentials");
            Assert.assertTrue(inventoryPage.isLoaded(), "Inventory should load for valid credentials");
        } else {
            String error = new LoginPage().getErrorMessage();
            Assert.assertTrue(
                    error.contains(errorFragment),
                    "Expected error containing '" + errorFragment + "', but was: " + error);
        }
    }
}

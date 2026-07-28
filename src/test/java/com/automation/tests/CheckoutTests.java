package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.data.LoginDataProvider;
import com.automation.pages.CheckoutCompletePage;
import com.automation.pages.CheckoutInfoPage;
import com.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Checkout-related Sauce Demo tests.
 */
public class CheckoutTests extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";

    @Test(groups = "smoke", description = "Complete checkout happy path")
    public void shouldCompleteCheckoutHappyPath() {
        CheckoutCompletePage completePage = new LoginPage()
                .loginSuccessfully(LoginDataProvider.STANDARD_USER, LoginDataProvider.PASSWORD)
                .addProductToCart(BACKPACK)
                .openCart()
                .clickCheckout()
                .fillInfo("Ashish", "Chauhan", "110001")
                .continueCheckoutSuccessfully()
                .finish();

        Assert.assertTrue(completePage.isLoaded(), "Checkout complete page should be visible");
        Assert.assertEquals(
                completePage.getConfirmationMessage(),
                "Thank you for your order!",
                "Confirmation message should match");
    }

    @Test(groups = "regression", description = "Checkout requires customer info fields")
    public void shouldShowErrorWhenCheckoutInfoIsEmpty() {
        CheckoutInfoPage infoPage = new LoginPage()
                .loginSuccessfully(LoginDataProvider.STANDARD_USER, LoginDataProvider.PASSWORD)
                .addProductToCart(BACKPACK)
                .openCart()
                .clickCheckout();

        infoPage.continueCheckout();

        String error = new CheckoutInfoPage().getErrorMessage();
        Assert.assertTrue(
                error.contains("First Name is required"),
                "Expected first-name required error, but was: " + error);
    }
}

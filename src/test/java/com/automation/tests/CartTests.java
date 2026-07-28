package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.data.LoginDataProvider;
import com.automation.pages.CartPage;
import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Cart and inventory Sauce Demo tests.
 */
public class CartTests extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    @Test(groups = "smoke", description = "Add one product and verify cart")
    public void shouldAddOneProductToCart() {
        InventoryPage inventoryPage = new LoginPage()
                .loginSuccessfully(LoginDataProvider.STANDARD_USER, LoginDataProvider.PASSWORD)
                .addProductToCart(BACKPACK);

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Cart badge should show 1 item");

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should be visible");
        Assert.assertTrue(
                cartPage.getCartItemNames().contains(BACKPACK),
                "Cart should contain " + BACKPACK);
    }

    @Test(groups = "regression", description = "Add multiple products and remove one from cart")
    public void shouldAddMultipleProductsAndRemoveFromCart() {
        InventoryPage inventoryPage = new LoginPage()
                .loginSuccessfully(LoginDataProvider.STANDARD_USER, LoginDataProvider.PASSWORD)
                .addProductToCart(BACKPACK)
                .addProductToCart(BIKE_LIGHT);

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart badge should show 2 items");

        CartPage cartPage = inventoryPage.openCart();
        Assert.assertEquals(cartPage.getCartItemNames().size(), 2, "Cart should list 2 products");

        cartPage.removeItem(BIKE_LIGHT);
        List<String> remaining = cartPage.getCartItemNames();
        Assert.assertEquals(remaining.size(), 1, "Cart should list 1 product after remove");
        Assert.assertTrue(remaining.contains(BACKPACK), "Backpack should remain in cart");
        Assert.assertFalse(remaining.contains(BIKE_LIGHT), "Bike Light should be removed");
    }

    @Test(groups = "regression", description = "Sort products by name Z to A")
    public void shouldSortProductsByNameZToA() {
        InventoryPage inventoryPage = new LoginPage()
                .loginSuccessfully(LoginDataProvider.STANDARD_USER, LoginDataProvider.PASSWORD);

        List<String> expected = new ArrayList<>(inventoryPage.getProductNames());
        Collections.sort(expected, Collections.reverseOrder());

        inventoryPage.sortBy(InventoryPage.SortOption.NAME_Z_TO_A);
        List<String> actual = inventoryPage.getProductNames();

        Assert.assertEquals(actual, expected, "Products should be sorted name Z to A");
    }
}

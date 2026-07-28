package com.automation.pages;

import com.automation.utils.WaitUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Sauce Demo cart screen.
 */
public class CartPage extends BasePage {

    private static final By CART_LIST = By.className("cart_list");
    private static final By CART_ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");

    public boolean isLoaded() {
        return isDisplayed(CART_LIST);
    }

    public List<String> getCartItemNames() {
        return driver.findElements(CART_ITEM_NAMES).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public CartPage removeItem(String productName) {
        click(removeButtonFor(productName));
        return this;
    }

    public CheckoutInfoPage clickCheckout() {
        click(CHECKOUT_BUTTON);
        WaitUtils.waitForUrlContains(driver, "checkout-step-one");
        return new CheckoutInfoPage();
    }

    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
        WaitUtils.waitForUrlContains(driver, "inventory");
        return new InventoryPage();
    }

    private By removeButtonFor(String productName) {
        return By.xpath(
                "//div[@class='cart_item']"
                        + "[.//*[@data-test='inventory-item-name' and normalize-space()='"
                        + productName
                        + "']]//button[starts-with(@data-test,'remove')]");
    }
}

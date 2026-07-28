package com.automation.pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import com.automation.utils.WaitUtils;

/**
 * Sauce Demo product inventory screen.
 */
public class InventoryPage extends BasePage {

    private static final By INVENTORY_LIST = By.className("inventory_list");
    private static final By PRODUCT_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By SORT_DROPDOWN = By.className("product_sort_container");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By MENU_WRAP = By.cssSelector(".bm-menu-wrap");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");

    public enum SortOption {
        NAME_A_TO_Z("az"),
        NAME_Z_TO_A("za"),
        PRICE_LOW_TO_HIGH("lohi"),
        PRICE_HIGH_TO_LOW("hilo");

        private final String value;

        SortOption(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public boolean isLoaded() {
        return isDisplayed(INVENTORY_LIST);
    }

    public InventoryPage addProductToCart(String productName) {
        click(addToCartButtonFor(productName));
        return this;
    }

    public InventoryPage removeProductFromCart(String productName) {
        click(removeFromCartButtonFor(productName));
        return this;
    }

    public CartPage openCart() {
        click(CART_LINK);
        WaitUtils.waitForUrlContains(driver, "cart.html");
        return new CartPage();
    }

    public InventoryPage sortBy(SortOption option) {
        Select sortSelect = new Select(findVisible(SORT_DROPDOWN));
        sortSelect.selectByValue(option.getValue());
        return this;
    }

    public List<String> getProductNames() {
        return driver.findElements(PRODUCT_NAMES).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public int getCartBadgeCount() {
        if (driver.findElements(CART_BADGE).isEmpty()) {
            return 0;
        }
        return Integer.parseInt(getText(CART_BADGE));
    }

    public LoginPage logout() {
        click(MENU_BUTTON);
        WaitUtils.waitForAttributeToBe(driver, MENU_WRAP, "aria-hidden", "false");
        click(LOGOUT_LINK);
        WaitUtils.waitForVisible(driver, By.id("login-button"));
        return new LoginPage();
    }

    private By addToCartButtonFor(String productName) {
        return By.xpath(
                "//div[@data-test='inventory-item']"
                        + "[.//*[@data-test='inventory-item-name' and normalize-space()='"
                        + productName
                        + "']]//button[starts-with(@data-test,'add-to-cart')]");
    }

    private By removeFromCartButtonFor(String productName) {
        return By.xpath(
                "//div[@data-test='inventory-item']"
                        + "[.//*[@data-test='inventory-item-name' and normalize-space()='"
                        + productName
                        + "']]//button[starts-with(@data-test,'remove')]");
    }
}

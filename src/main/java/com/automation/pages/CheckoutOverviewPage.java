package com.automation.pages;

import com.automation.utils.WaitUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Sauce Demo checkout overview / summary screen.
 */
public class CheckoutOverviewPage extends BasePage {

    private static final By SUMMARY_INFO = By.className("summary_info");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By FINISH_BUTTON = By.id("finish");
    private static final By CANCEL_BUTTON = By.id("cancel");

    public boolean isLoaded() {
        return isDisplayed(SUMMARY_INFO);
    }

    public List<String> getItemNames() {
        return driver.findElements(ITEM_NAMES).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public CheckoutCompletePage finish() {
        click(FINISH_BUTTON);
        WaitUtils.waitForUrlContains(driver, "checkout-complete");
        return new CheckoutCompletePage();
    }

    public InventoryPage cancel() {
        click(CANCEL_BUTTON);
        return new InventoryPage();
    }
}

package com.automation.data;

import org.testng.annotations.DataProvider;

/**
 * Parameterized login inputs for Sauce Demo.
 */
public final class LoginDataProvider {

    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_OUT_USER = "locked_out_user";
    public static final String PASSWORD = "secret_sauce";

    private LoginDataProvider() {
    }

    /**
     * Columns: username, password, expectSuccess, errorFragment (nullable when success).
     */
    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        return new Object[][] {
            {STANDARD_USER, PASSWORD, true, null},
            {LOCKED_OUT_USER, PASSWORD, false, "Sorry, this user has been locked out"},
            {STANDARD_USER, "wrong_password", false, "Username and password do not match"},
            {"", PASSWORD, false, "Username is required"},
            {STANDARD_USER, "", false, "Password is required"}
        };
    }
}

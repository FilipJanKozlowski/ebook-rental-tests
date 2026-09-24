package com.kodilla.ebook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest extends BaseTest {

    @Test
    @DisplayName("FE-LOG-01")
    void shouldLogInWithValidData() {
        String login = registerNewUser();

        logIn(login, PASSWORD);

        assertEquals("Titles catalog", getText(TITLES_HEADER));
    }

    @Test
    @DisplayName("FE-LOG-02")
    void shouldNotLogInWithWrongPassword() {
        String login = registerNewUser();

        logIn(login, "ZleHaslo123");

        assertEquals("Login failed", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-LOG-03")
    void shouldNotLogInNonExistingUser() {
        logIn("Nieistniejacy" + System.currentTimeMillis(), PASSWORD);

        assertEquals("Login failed", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-LOG-04")
    void shouldNotLogInWithEmptyFields() {
        logIn("", "");

        assertEquals("You can't leave fields empty", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-LOG-05")
    void shouldGoToRegisterFormWithSignUpButton() {
        driver.get(BASE_URL + "/login");

        click(REGISTER_BUTTON);
        waitForElement(PASSWORD_REPEAT_INPUT);

        assertTrue(driver.getCurrentUrl().contains("/register"));
        assertEquals("Sign up", getText(FORM_HEADER));
    }

    @Test
    @DisplayName("FE-LOG-06")
    void shouldRedirectToLoginFormWhenNotLoggedIn() {
        driver.get(BASE_URL + "/");

        wait.until(ExpectedConditions.urlContains("/login"));
        waitForElement(LINK_TO_REGISTER);

        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    @DisplayName("FE-LOG-07")
    void shouldNotLogInWithLoginAndPasswordInDifferentCase() {
        String login = registerNewUser();

        logIn(login.toUpperCase(), PASSWORD.toUpperCase());

        assertEquals("Login failed", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-LOG-08")
    void shouldRedirectToLoginFormAfterPageRefresh() {
        registerAndLogIn();

        driver.navigate().refresh();

        wait.until(ExpectedConditions.urlContains("/login"));
        waitForElement(LINK_TO_REGISTER);

        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
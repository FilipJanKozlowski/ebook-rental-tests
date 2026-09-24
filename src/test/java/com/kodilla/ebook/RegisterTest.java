package com.kodilla.ebook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterTest extends BaseTest {

    @Test
    @DisplayName("FE-REG-01")
    void shouldRegisterNewUserWithValidData() {
        register(uniqueLogin(), PASSWORD, PASSWORD);

        assertEquals("You have been successfully registered!", getText(SUCCESS_MESSAGE));
    }

    @Test
    @DisplayName("FE-REG-02")
    void shouldNotRegisterUserWhenPasswordsDoNotMatch() {
        String login = uniqueLogin();

        register(login, PASSWORD, "InneHaslo123");

        assertEquals("The passwords don't match", getText(ERROR_MESSAGE));

        logIn(login, PASSWORD);

        assertEquals("Login failed", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-REG-03")
    void shouldNotRegisterUserWithEmptyFields() {
        register("", "", "");

        assertEquals("You can't leave fields empty", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-REG-04")
    void shouldNotRegisterUserWithExistingLogin() {
        String login = registerNewUser();

        register(login, PASSWORD, PASSWORD);

        assertEquals("This user already exist!", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-REG-05")
    void shouldGoToLoginFormWithLogInButton() {
        driver.get(BASE_URL + "/register");

        click(LOGIN_BUTTON);
        waitForElement(LINK_TO_REGISTER);

        assertTrue(driver.getCurrentUrl().contains("/login"));
        assertEquals("Log in", getText(FORM_HEADER));
    }

    @Test
    @DisplayName("FE-REG-06")
    void shouldRegisterUserWithLoginAndPasswordMadeOfSpacesOnly() {
        String login = " ".repeat(10);
        String password = " ".repeat(5);

        register(login, password, password);
        String message = getText("//div[contains(@class, \"alert\")]/p");

        assertEquals("This user already exist!", message);

        logIn(login, password);

        assertEquals("Titles catalog", getText(TITLES_HEADER));
    }
}
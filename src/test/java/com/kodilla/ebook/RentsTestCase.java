package com.kodilla.ebook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RentsTestCase extends BaseTestCase {

    private void openRentsOfNewCopy() {
        registerAndLogIn();
        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");
        openCopies();
        addCopy();
        openRents();
    }

    @Test
    @DisplayName("FE-RNT-01")
    void shouldShowNoRentsForNewCopy() {
        openRentsOfNewCopy();

        assertEquals("No rents...", getText(INFO_MESSAGE));
    }

    @Test
    @DisplayName("FE-RNT-02")
    void shouldRentCopyWithCustomerNameAndDate() {
        openRentsOfNewCopy();

        click(ADD_RENT_BUTTON);
        type(CUSTOMER_NAME_INPUT, "Jan Kowalski");
        String rentDate = pickDay("rent-date", "15");
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForPresence(RENT_ITEM);

        assertEquals("Jan Kowalski", getText(RENT_CUSTOMER_NAME));
        assertEquals(rentDate, getText(RENT_DATE));
        assertTrue(getText(RENT_EXPIRATION_DATE).startsWith("(expiration: "));
    }

    @Test
    @DisplayName("FE-RNT-03")
    void shouldNotRentCopyWithoutCustomerName() {
        openRentsOfNewCopy();

        click(ADD_RENT_BUTTON);
        click(SUBMIT_BUTTON);

        assertEquals("\"customerName\" field shouldn't be empty...", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-RNT-04")
    void shouldNotAllowRentWithoutDate() {
        openRentsOfNewCopy();

        click(ADD_RENT_BUTTON);
        WebElement rentDateInput = waitForElement("//input[@name=\"rent-date\"]");

        assertEquals("true", rentDateInput.getAttribute("readonly"));
        assertFalse(rentDateInput.getAttribute("value").isEmpty());
    }

    @Test
    @DisplayName("FE-RNT-05")
    void shouldEditExpirationDate() {
        openRentsOfNewCopy();
        addRent("Jan Kowalski");

        click(EDIT_BUTTON);
        String expirationDate = pickDayInOtherMonth("expiration-date", "next", "15");
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForText(RENT_EXPIRATION_DATE, "(expiration: " + expirationDate + ")");

        assertEquals("(expiration: " + expirationDate + ")", getText(RENT_EXPIRATION_DATE));
    }

    @Test
    @DisplayName("FE-RNT-06")
    void shouldNotSaveExpirationDateAfterClosingEditWindowWithX() {
        openRentsOfNewCopy();
        addRent("Jan Kowalski");
        String expirationDate = getText(RENT_EXPIRATION_DATE);

        click(EDIT_BUTTON);
        pickDayInOtherMonth("expiration-date", "next", "15");
        click(CLOSE_WINDOW_BUTTON);
        waitForModalToClose();

        assertEquals(expirationDate, getText(RENT_EXPIRATION_DATE));
    }

    @Test
    @DisplayName("FE-RNT-07")
    void shouldSaveExpirationDateEarlierThanRentDate() {
        openRentsOfNewCopy();
        addRent("Jan Kowalski");
        String rentDate = getText(RENT_DATE);

        click(EDIT_BUTTON);
        String expirationDate = pickDayInOtherMonth("expiration-date", "prev", "1");
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForText(RENT_EXPIRATION_DATE, "(expiration: " + expirationDate + ")");

        assertTrue(expirationDate.compareTo(rentDate) < 0);
        assertEquals("(expiration: " + expirationDate + ")", getText(RENT_EXPIRATION_DATE));
    }

    @Test
    @DisplayName("FE-RNT-08")
    void shouldKeepCopyStatusAvailableAfterRent() {
        openRentsOfNewCopy();
        addRent("Jan Kowalski");

        click(RETURN_BUTTON);
        waitForElement(COPIES_HEADER);

        assertEquals("Available", getText(COPY_STATUS));
    }

    @Test
    @DisplayName("FE-RNT-09")
    void shouldAddAnotherRentOfAlreadyRentedCopy() {
        openRentsOfNewCopy();
        addRent("Jan Kowalski");

        addRent("Anna Nowak");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(RENT_ITEM), 2));

        assertEquals(2, count(RENT_ITEM));
    }

    @Test
    @DisplayName("FE-RNT-10")
    void shouldRemoveRent() {
        openRentsOfNewCopy();
        addRent("Jan Kowalski");

        click(REMOVE_BUTTON);

        assertEquals("No rents...", getText(INFO_MESSAGE));
        assertEquals(0, count(RENT_ITEM));
    }

    @Test
    @DisplayName("FE-RNT-11")
    void shouldGoBackToCopiesWithReturnButton() {
        openRentsOfNewCopy();

        click(RETURN_BUTTON);
        waitForElement(COPIES_HEADER);

        assertEquals("List of copies", getText(COPIES_HEADER));
    }

    @Test
    @DisplayName("FE-RNT-12")
    void shouldRentCopyWithCustomerNameMadeOfSpacesOrSpecialCharacters() {
        openRentsOfNewCopy();

        addRent("     ");
        addRent("!@#$%^&*");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(RENT_ITEM), 2));

        assertEquals(2, count(RENT_ITEM));
    }

    @Test
    @DisplayName("FE-RNT-13")
    void shouldShowServerErrorForRentDateWithNegativeYear() {
        openRentsOfNewCopy();

        click(ADD_RENT_BUTTON);
        type(CUSTOMER_NAME_INPUT, "Jan Kowalski");
        pickDateWithNegativeYear("rent-date");
        click(SUBMIT_BUTTON);

        assertEquals("Request failed with status code 400", getText(ERROR_MESSAGE));
    }
}
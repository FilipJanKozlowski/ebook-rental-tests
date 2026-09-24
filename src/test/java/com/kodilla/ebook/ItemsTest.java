package com.kodilla.ebook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemsTest extends BaseTest {

    private static final String NEGATIVE_YEAR = OPEN_CALENDAR + "//span[contains(@class, \"cell year\") and starts-with(text(), \"-\")]";

    private void openCopiesOfNewTitle() {
        registerAndLogIn();
        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");
        openCopies();
    }

    @Test
    @DisplayName("FE-ITM-01")
    void shouldShowNoCopiesForNewTitle() {
        openCopiesOfNewTitle();

        assertEquals("No copies...", getText(INFO_MESSAGE));
    }

    @Test
    @DisplayName("FE-ITM-02")
    void shouldAddCopyWithPurchaseDate() {
        openCopiesOfNewTitle();

        click(ADD_COPY_BUTTON);
        String purchaseDate = pickDay("purchase-date", "15");
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForPresence(COPY_ITEM);

        assertEquals(purchaseDate, getText(COPY_PURCHASE_DATE));
        assertEquals("Available", getText(COPY_STATUS));
    }

    @Test
    @DisplayName("FE-ITM-03")
    void shouldShowServerErrorForPurchaseDateWithNegativeYear() {
        openCopiesOfNewTitle();

        click(ADD_COPY_BUTTON);
        click("//input[@name=\"purchase-date\"]");
        click(OPEN_CALENDAR + "//span[contains(@class, \"day__month_btn\")]");
        click(OPEN_CALENDAR + "//span[contains(@class, \"month__year_btn\")]");
        while (driver.findElements(By.xpath(NEGATIVE_YEAR)).isEmpty()) {
            driver.findElement(By.xpath(OPEN_CALENDAR + "//span[@class=\"prev\"]")).click();
        }
        click(NEGATIVE_YEAR);
        click(OPEN_CALENDAR + "//span[contains(@class, \"cell month\") and text()=\"January\"]");
        click(OPEN_CALENDAR + "//span[contains(@class, \"cell day\") and text()=\"1\"]");
        click(SUBMIT_BUTTON);

        assertEquals("Request failed with status code 400", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-ITM-04")
    void shouldEditPurchaseDate() {
        openCopiesOfNewTitle();
        addCopy();

        click(EDIT_BUTTON);
        String newPurchaseDate = pickDay("purchase-date", "20");
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForText(COPY_PURCHASE_DATE, newPurchaseDate);

        assertEquals(newPurchaseDate, getText(COPY_PURCHASE_DATE));
    }

    @Test
    @DisplayName("FE-ITM-05")
    void shouldNotSavePurchaseDateAfterClosingEditWindowWithX() {
        openCopiesOfNewTitle();
        addCopy();
        String purchaseDate = getText(COPY_PURCHASE_DATE);

        click(EDIT_BUTTON);
        pickDay("purchase-date", "20");
        click(CLOSE_WINDOW_BUTTON);
        waitForModalToClose();

        assertEquals(purchaseDate, getText(COPY_PURCHASE_DATE));
    }

    @Test
    @DisplayName("FE-ITM-06")
    void shouldRemoveCopy() {
        openCopiesOfNewTitle();
        addCopy();

        click(REMOVE_BUTTON);

        assertEquals("No copies...", getText(INFO_MESSAGE));
        assertEquals(0, count(COPY_ITEM));
    }

    @Test
    @DisplayName("FE-ITM-07")
    void shouldGoBackToTitlesCatalogWithReturnButton() {
        openCopiesOfNewTitle();

        click(RETURN_BUTTON);
        waitForElement(TITLES_HEADER);

        assertEquals("Titles catalog", getText(TITLES_HEADER));
    }

    @Test
    @DisplayName("FE-ITM-08")
    void shouldGoToRentsHistoryWithShowHistoryButton() {
        openCopiesOfNewTitle();
        addCopy();

        openRents();

        assertEquals("Rents history", getText(RENTS_HEADER));
    }

    @Test
    @DisplayName("FE-ITM-09")
    void shouldRedirectToLoginFormWhenOpeningCopiesOfOtherUserTitle() {
        registerAndLogIn();
        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");
        String titleId = driver.findElement(By.xpath(TITLE_ITEM)).getAttribute("id").replace("title-", "");

        registerAndLogIn();
        driver.get(BASE_URL + "/items/" + titleId);

        wait.until(ExpectedConditions.urlContains("/login"));
        waitForElement(LINK_TO_REGISTER);

        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    @DisplayName("FE-ITM-10")
    void shouldRedirectToLoginFormWhenOpeningCopiesOfNonExistingTitle() {
        registerAndLogIn();

        driver.get(BASE_URL + "/items/999999999");

        wait.until(ExpectedConditions.urlContains("/login"));
        waitForElement(LINK_TO_REGISTER);

        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    @DisplayName("FE-ITM-11")
    void shouldNotRemoveCopyWithRentsHistory() {
        openCopiesOfNewTitle();
        addCopy();
        openRents();
        addRent("Jan Kowalski");
        click(RETURN_BUTTON);
        waitForElement(COPIES_HEADER);

        click(REMOVE_BUTTON);

        assertEquals("You can't remove copy with the rents history!", getText(ERROR_MESSAGE));
        assertEquals(1, count(COPY_ITEM));
    }
}
package com.kodilla.ebook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TitlesTest extends BaseTest {

    @Test
    @DisplayName("FE-TIT-01")
    void shouldShowNoTitlesForNewUser() {
        registerAndLogIn();

        assertEquals("No titles", getText(INFO_MESSAGE));
    }

    @Test
    @DisplayName("FE-TIT-02")
    void shouldAddTitleWithValidData() {
        registerAndLogIn();

        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");

        assertEquals("Pan Tadeusz", getText(TITLE_NAME));
        assertEquals("by Adam Mickiewicz", getText(TITLE_AUTHOR));
        assertEquals("(1834)", getText(TITLE_YEAR));
    }

    @Test
    @DisplayName("FE-TIT-03")
    void shouldAddTitleWithAbout40Characters() {
        registerAndLogIn();
        String title = "Przygody testera automatyzujacego w banku";

        addTitle(title, "Filip Kozlowski", "2026");

        assertEquals(title, getText(TITLE_NAME));
    }

    @Test
    @DisplayName("FE-TIT-04")
    void shouldNotAddTitleWithEmptyTitle() {
        registerAndLogIn();

        fillTitleForm("", "Adam Mickiewicz", "1834");
        click(SUBMIT_BUTTON);

        assertEquals("\"title\" field shouldn't be empty...", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-TIT-05")
    void shouldNotAddTitleWithEmptyAuthor() {
        registerAndLogIn();

        fillTitleForm("Pan Tadeusz", "", "1834");
        click(SUBMIT_BUTTON);

        assertEquals("\"author\" field shouldn't be empty...", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-TIT-06")
    void shouldNotAddTitleWithEmptyYear() {
        registerAndLogIn();

        fillTitleForm("Pan Tadeusz", "Adam Mickiewicz", "");
        click(SUBMIT_BUTTON);

        assertEquals("\"year\" field shouldn't be empty...", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-TIT-07")
    void shouldAddTitleWithNegativeYear() {
        registerAndLogIn();

        addTitle("Pan Tadeusz", "Adam Mickiewicz", "-500");

        assertEquals("(-500)", getText(TITLE_YEAR));
    }

    @Test
    @DisplayName("FE-TIT-08")
    void shouldShowServerErrorForVeryLongTitle() {
        registerAndLogIn();

        fillTitleForm("A".repeat(500), "Adam Mickiewicz", "1834");
        click(SUBMIT_BUTTON);

        assertEquals("Request failed with status code 500", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-TIT-09")
    void shouldNotAddTitleAfterClosingWindowWithX() {
        registerAndLogIn();

        fillTitleForm("Pan Tadeusz", "Adam Mickiewicz", "1834");
        click(CLOSE_WINDOW_BUTTON);
        waitForModalToClose();

        assertEquals("No titles", getText(INFO_MESSAGE));
        assertEquals(0, count(TITLE_ITEM));
    }

    @Test
    @DisplayName("FE-TIT-10")
    void shouldEditTitle() {
        registerAndLogIn();
        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");

        click(EDIT_BUTTON);
        replaceText(TITLE_INPUT, "Kordian");
        replaceText(AUTHOR_INPUT, "Juliusz Slowacki");
        replaceText(YEAR_INPUT, "1833");
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForText(TITLE_NAME, "Kordian");

        assertEquals("Kordian", getText(TITLE_NAME));
        assertEquals("by Juliusz Slowacki", getText(TITLE_AUTHOR));
        assertEquals("(1833)", getText(TITLE_YEAR));
    }

    @Test
    @DisplayName("FE-TIT-11")
    void shouldNotSaveTitleChangesAfterClosingEditWindowWithX() {
        registerAndLogIn();
        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");

        click(EDIT_BUTTON);
        replaceText(TITLE_INPUT, "Kordian");
        click(CLOSE_WINDOW_BUTTON);
        waitForModalToClose();

        assertEquals("Pan Tadeusz", getText(TITLE_NAME));
    }

    @Test
    @DisplayName("FE-TIT-12")
    void shouldRemoveTitle() {
        registerAndLogIn();
        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");

        click(REMOVE_BUTTON);

        assertEquals("No titles", getText(INFO_MESSAGE));
        assertEquals(0, count(TITLE_ITEM));
    }

    @Test
    @DisplayName("FE-TIT-13")
    void shouldShowOnlyTitlesOfLoggedInUser() {
        registerAndLogIn();
        addTitle("Tytul pierwszego uzytkownika", "Autor A", "2001");

        registerAndLogIn();
        addTitle("Tytul drugiego uzytkownika", "Autor B", "2002");

        assertEquals(1, count(TITLE_ITEM));
        assertEquals("Tytul drugiego uzytkownika", getText(TITLE_NAME));
    }

    @Test
    @DisplayName("FE-TIT-14")
    void shouldGoToCopiesWithShowCopiesButton() {
        registerAndLogIn();
        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");

        openCopies();

        assertEquals("List of copies", getText(COPIES_HEADER));
    }

    @Test
    @DisplayName("FE-TIT-15")
    void shouldNotRemoveTitleWithCopies() {
        registerAndLogIn();
        addTitle("Pan Tadeusz", "Adam Mickiewicz", "1834");
        openCopies();
        addCopy();
        click(RETURN_BUTTON);
        waitForElement(TITLES_HEADER);

        click(REMOVE_BUTTON);

        assertEquals("You can't remove titles with copies!", getText(ERROR_MESSAGE));
        assertEquals(1, count(TITLE_ITEM));
    }

    @Test
    @DisplayName("FE-TIT-16")
    void shouldAddTitleWithTitleAndAuthorMadeOfSpacesOnly() {
        registerAndLogIn();

        addTitle("     ", "     ", "2002");

        assertEquals(1, count(TITLE_ITEM));
        assertEquals("", getText(TITLE_NAME));
    }

    @Test
    @DisplayName("FE-TIT-17")
    void shouldShowHtmlTagsInTitleAsPlainText() {
        registerAndLogIn();

        addTitle("<b>Test</b>", "Adam Mickiewicz", "1834");

        assertEquals("<b>Test</b>", getText(TITLE_NAME));
        assertEquals(0, count(TITLE_NAME + "/b"));
    }

    @Test
    @DisplayName("FE-TIT-18")
    void shouldNotAddTitleWithYearZero() {
        registerAndLogIn();

        fillTitleForm("Pan Tadeusz", "Adam Mickiewicz", "0");
        click(SUBMIT_BUTTON);

        assertEquals("\"year\" field shouldn't be empty...", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-TIT-19")
    void shouldAddTitleWithYear33333() {
        registerAndLogIn();

        addTitle("Pan Tadeusz", "Adam Mickiewicz", "33333");

        assertEquals("(33333)", getText(TITLE_YEAR));
    }

    @Test
    @DisplayName("FE-TIT-20")
    void shouldShowServerErrorForYear9999999999() {
        registerAndLogIn();

        fillTitleForm("Pan Tadeusz", "Adam Mickiewicz", "9999999999");
        click(SUBMIT_BUTTON);

        assertEquals("Request failed with status code 400", getText(ERROR_MESSAGE));
    }

    @Test
    @DisplayName("FE-TIT-21")
    void shouldAddOnlyOneTitleAfterDoubleClickOnAddTitle() throws InterruptedException {
        registerAndLogIn();

        fillTitleForm("Pan Tadeusz", "Adam Mickiewicz", "1834");
        new Actions(driver).doubleClick(driver.findElement(By.xpath(SUBMIT_BUTTON))).perform();
        waitForPresence(TITLE_ITEM);
        Thread.sleep(3000);

        assertEquals(1, count(TITLE_ITEM));
    }
}
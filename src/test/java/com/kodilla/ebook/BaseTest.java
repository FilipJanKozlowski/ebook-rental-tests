package com.kodilla.ebook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

abstract class BaseTest {

    protected static final String BASE_URL = "https://ta-bookrental-fe.onrender.com";
    protected static final String PASSWORD = "Haslo123";

    protected static final String LOGIN_INPUT = "//input[@id=\"login\"]";
    protected static final String PASSWORD_INPUT = "//input[@id=\"password\"]";
    protected static final String PASSWORD_REPEAT_INPUT = "//input[@id=\"password-repeat\"]";
    protected static final String LOGIN_BUTTON = "//button[@id=\"login-btn\"]";
    protected static final String REGISTER_BUTTON = "//button[@id=\"register-btn\"]";
    protected static final String LINK_TO_REGISTER = "//a[@href=\"/register\"]";
    protected static final String FORM_HEADER = "//form/h2";

    protected static final String ERROR_MESSAGE = "//div[contains(@class, \"alert--error\")]/p";
    protected static final String SUCCESS_MESSAGE = "//div[contains(@class, \"alert--success\")]/p";
    protected static final String INFO_MESSAGE = "//div[contains(@class, \"alert--info\")]/p";

    protected static final String MODAL_WINDOW = "//div[contains(@class, \"full-screen--fog\")]";
    protected static final String CLOSE_WINDOW_BUTTON = "//a[contains(@class, \"fog__icon-close\")]";
    protected static final String SUBMIT_BUTTON = "//button[@name=\"submit-button\"]";
    protected static final String EDIT_BUTTON = "//button[contains(@class, \"edit-btn\")]";
    protected static final String REMOVE_BUTTON = "//button[contains(@class, \"remove-btn\")]";
    protected static final String RETURN_BUTTON = "//button[@id=\"return-button\"]";
    protected static final String OPEN_CALENDAR = "//div[contains(@class, \"vdp-datepicker__calendar\") and not(contains(@style, \"none\"))]";

    protected static final String TITLES_HEADER = "//div[@id=\"titles\"]/h2";
    protected static final String ADD_TITLE_BUTTON = "//button[@id=\"add-title-button\"]";
    protected static final String TITLE_INPUT = "//input[@name=\"title\"]";
    protected static final String AUTHOR_INPUT = "//input[@name=\"author\"]";
    protected static final String YEAR_INPUT = "//input[@name=\"year\"]";
    protected static final String TITLE_ITEM = "//li[contains(@class, \"titles-list__item\")]";
    protected static final String TITLE_NAME = "//div[contains(@class, \"titles-list__item__title\")]";
    protected static final String TITLE_AUTHOR = "//div[contains(@class, \"titles-list__item__author\")]";
    protected static final String TITLE_YEAR = "//div[contains(@class, \"titles-list__item__year\")]";
    protected static final String SHOW_COPIES_BUTTON = "//button[contains(@class, \"show-copies-btn\")]";

    protected static final String COPIES_HEADER = "//div[@id=\"title-copies\"]/div/h2";
    protected static final String ADD_COPY_BUTTON = "//button[@id=\"add-item-button\"]";
    protected static final String COPY_ITEM = "//li[contains(@class, \"items-list__item\")]";
    protected static final String COPY_PURCHASE_DATE = "//div[contains(@class, \"items-list__item__purchase-date\")]";
    protected static final String COPY_STATUS = "//div[contains(@class, \"items-list__item__status\")]";
    protected static final String SHOW_HISTORY_BUTTON = "//button[contains(@class, \"show-rents-btn\")]";

    protected static final String RENTS_HEADER = "//div[@id=\"rents\"]/div/h2";
    protected static final String ADD_RENT_BUTTON = "//button[@id=\"add-rent-button\"]";
    protected static final String CUSTOMER_NAME_INPUT = "//input[@name=\"customer-name\"]";
    protected static final String RENT_ITEM = "//li[contains(@class, \"rents-list__rent\")]";
    protected static final String RENT_CUSTOMER_NAME = "//div[contains(@class, \"rents-list__rent__customer-name\")]";
    protected static final String RENT_DATE = "(//div[contains(@class, \"rents-list__rent__rent-date\")])[1]";
    protected static final String RENT_EXPIRATION_DATE = "(//div[contains(@class, \"rents-list__rent__rent-date\")])[2]";

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\selenium-drivers\\Chrome\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 60);
        wait.ignoring(ElementClickInterceptedException.class);
        wait.ignoring(StaleElementReferenceException.class);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    protected WebElement waitForElement(String xpath) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    protected WebElement waitForPresence(String xpath) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    protected void waitForModalToClose() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(MODAL_WINDOW)));
    }

    protected void waitForText(String xpath, String expectedText) {
        wait.until(ExpectedConditions.attributeToBe(By.xpath(xpath), "textContent", expectedText));
    }

    protected void click(String xpath) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        wait.until(webDriver -> {
            webDriver.findElement(By.xpath(xpath)).click();
            return true;
        });
    }

    protected void type(String xpath, String text) {
        waitForElement(xpath).sendKeys(text);
    }

    protected void replaceText(String xpath, String text) {
        WebElement input = waitForElement(xpath);
        input.clear();
        input.sendKeys(text);
    }

    protected String getText(String xpath) {
        return waitForPresence(xpath).getAttribute("textContent").trim();
    }

    protected int count(String xpath) {
        return driver.findElements(By.xpath(xpath)).size();
    }

    protected String uniqueLogin() {
        return "Filip" + System.currentTimeMillis();
    }

    protected void register(String login, String password, String repeatedPassword) {
        driver.get(BASE_URL + "/register");
        waitForElement(PASSWORD_REPEAT_INPUT);
        type(LOGIN_INPUT, login);
        type(PASSWORD_INPUT, password);
        type(PASSWORD_REPEAT_INPUT, repeatedPassword);
        click(REGISTER_BUTTON);
    }

    protected String registerNewUser() {
        String login = uniqueLogin();
        register(login, PASSWORD, PASSWORD);
        waitForPresence(SUCCESS_MESSAGE);
        return login;
    }

    protected void logIn(String login, String password) {
        driver.get(BASE_URL + "/login");
        waitForElement(LINK_TO_REGISTER);
        type(LOGIN_INPUT, login);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }

    protected void registerAndLogIn() {
        String login = registerNewUser();
        logIn(login, PASSWORD);
        waitForElement(TITLES_HEADER);
    }

    protected void fillTitleForm(String title, String author, String year) {
        click(ADD_TITLE_BUTTON);
        type(TITLE_INPUT, title);
        type(AUTHOR_INPUT, author);
        type(YEAR_INPUT, year);
    }

    protected void addTitle(String title, String author, String year) {
        fillTitleForm(title, author, year);
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForPresence(TITLE_ITEM);
    }

    protected void openCopies() {
        click(SHOW_COPIES_BUTTON);
        waitForElement(COPIES_HEADER);
    }

    protected void addCopy() {
        click(ADD_COPY_BUTTON);
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForPresence(COPY_ITEM);
    }

    protected void openRents() {
        click(SHOW_HISTORY_BUTTON);
        waitForElement(RENTS_HEADER);
    }

    protected void addRent(String customerName) {
        click(ADD_RENT_BUTTON);
        type(CUSTOMER_NAME_INPUT, customerName);
        click(SUBMIT_BUTTON);
        waitForModalToClose();
        waitForPresence(RENT_ITEM);
    }

    protected String pickDay(String inputName, String day) {
        String input = "//input[@name=\"" + inputName + "\"]";
        click(input);
        click(OPEN_CALENDAR + "//span[contains(@class, \"cell day\") and text()=\"" + day + "\"]");
        return driver.findElement(By.xpath(input)).getAttribute("value");
    }

    protected String pickDayInOtherMonth(String inputName, String arrow, String day) {
        String input = "//input[@name=\"" + inputName + "\"]";
        click(input);
        click(OPEN_CALENDAR + "//span[@class=\"" + arrow + "\"]");
        click(OPEN_CALENDAR + "//span[contains(@class, \"cell day\") and text()=\"" + day + "\"]");
        return driver.findElement(By.xpath(input)).getAttribute("value");
    }
}
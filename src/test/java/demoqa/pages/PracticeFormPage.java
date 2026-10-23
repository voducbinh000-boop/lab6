package demoqa.pages;

import demoqa.utils.StudentData;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page Object: chua locator va cac hanh dong tren form. Khong chua assert. */
public class PracticeFormPage {
    public static final String URL = "https://demoqa.com/automation-practice-form";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ===== Locators =====
    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By email = By.id("userEmail");
    private final By mobile = By.id("userNumber");
    private final By dobInput = By.id("dateOfBirthInput");
    private final By monthSelect = By.cssSelector(".react-datepicker__month-select");
    private final By yearSelect = By.cssSelector(".react-datepicker__year-select");
    private final By subjectsInput = By.id("subjectsInput");
    private final By uploadInput = By.id("uploadPicture");
    private final By address = By.id("currentAddress");
    private final By stateInput = By.cssSelector("#state input");
    private final By cityInput = By.cssSelector("#city input");
    private final By submitBtn = By.id("submit");
    private final By modalTitle = By.id("example-modal-sizes-title-lg");
    private final By closeModalBtn = By.id("closeLargeModal");
    // Option cua react-select (subjects, state, city)
    private final By optionLocator = By.cssSelector("[id*='-option-'], [class*='option']");

    public PracticeFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ===== Actions =====
    public PracticeFormPage open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName));
        removeAds();
        return this;
    }

    /** Xoa quang cao/footer che mat phan tu (DemoQA hay bi loi click). */
    private void removeAds() {
        ((JavascriptExecutor) driver).executeScript(
                "const s=['#fixedban','footer','iframe'];" +
                "s.forEach(q=>document.querySelectorAll(q).forEach(e=>e.remove()));");
    }

    public String getTitle() { return driver.getTitle(); }
    public String getCurrentUrl() { return driver.getCurrentUrl(); }

    public PracticeFormPage enterFirstName(String v) { type(firstName, v); return this; }
    public PracticeFormPage enterLastName(String v) { type(lastName, v); return this; }
    public PracticeFormPage enterEmail(String v) { type(email, v); return this; }
    public PracticeFormPage enterMobile(String v) { type(mobile, v); return this; }

    public String getFirstName() { return value(firstName); }
    public String getLastName() { return value(lastName); }
    public String getEmail() { return value(email); }
    public String getMobile() { return value(mobile); }

    public PracticeFormPage selectGender(String gender) {
        jsClick(By.xpath("//label[@for and normalize-space()='" + gender + "' and contains(@for,'gender-radio')]"));
        return this;
    }

    public PracticeFormPage selectHobby(String hobby) {
        jsClick(By.xpath("//label[contains(@for,'hobbies-checkbox') and normalize-space()='" + hobby + "']"));
        return this;
    }

    public PracticeFormPage selectDateOfBirth(String day, String month, String year) {
        jsClick(dobInput);
        wait.until(ExpectedConditions.visibilityOfElementLocated(monthSelect));
        new Select(driver.findElement(monthSelect)).selectByVisibleText(month);
        new Select(driver.findElement(yearSelect)).selectByVisibleText(year);
        By dayLocator = By.xpath("//div[contains(@class,'react-datepicker__day') " +
                "and not(contains(@class,'outside-month')) and normalize-space()='" + Integer.parseInt(day) + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(monthSelect));
        return this;
    }

    public String getDateOfBirthValue() { return value(dobInput); }

    public PracticeFormPage selectSubject(String subject) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(subjectsInput));
        pickOption(el, subject);
        return this;
    }

    public PracticeFormPage uploadFile(String absolutePath) {
        driver.findElement(uploadInput).sendKeys(absolutePath);
        return this;
    }

    public String getUploadedFileName() {
        // Gia tri tra ve co dang C:\fakepath\ten_file
        String v = value(uploadInput);
        return v.substring(Math.max(v.lastIndexOf('\\'), v.lastIndexOf('/')) + 1);
    }

    public PracticeFormPage enterAddress(String v) { type(address, v); return this; }

    public PracticeFormPage selectStateAndCity(String state, String city) {
        selectReact(stateInput, state);
        selectReact(cityInput, city);
        return this;
    }

    public PracticeFormPage clickSubmit() {
        jsClick(submitBtn);
        return this;
    }

    // ===== Modal =====
    public PracticeFormPage waitForModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
        return this;
    }

    public String getModalTitle() { return driver.findElement(modalTitle).getText(); }

    public String getModalValue(String label) {
        By loc = By.xpath("//div[@class='modal-body']//td[normalize-space()='" + label + "']/following-sibling::td");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).getText().trim();
    }

    public void closeModal() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            jsClick(closeModalBtn);
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.invisibilityOfElementLocated(modalTitle));
                return;
            } catch (TimeoutException e) {
                System.out.println("[Retry " + attempt + "/3] Modal chua dong, thu lai...");
            }
        }
        // Du phong: nhan phim Esc
        driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalTitle));
    }

    // ===== Flow day du =====
    public PracticeFormPage fillAndSubmit(StudentData d, String uploadPath) {
        enterFirstName(d.firstName());
        enterLastName(d.lastName());
        enterEmail(d.email());
        selectGender(d.gender());
        enterMobile(d.mobile());
        selectDateOfBirth(d.day(), d.month(), d.year());
        selectSubject(d.subject());
        selectHobby(d.hobby());
        if (uploadPath != null) uploadFile(uploadPath);
        enterAddress(d.address());
        selectStateAndCity(d.state(), d.city());
        return this;
    }

    // ===== Helpers =====
    private void type(By by, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        el.clear();
        el.sendKeys(text);
    }

    private String value(By by) {
        return driver.findElement(by).getAttribute("value");
    }

    private void jsClick(By by) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    /**
     * Go chu vao o react-select, cho menu hien ra roi nhan ENTER.
     * Neu menu khong hien (React chua nhan phim), xoa chu va thu lai toi da 3 lan.
     */
    private void pickOption(WebElement input, String text) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", input);
        for (int attempt = 1; attempt <= 3; attempt++) {
            js.executeScript("arguments[0].focus();", input);
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            input.sendKeys(text);
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                        d -> d.findElements(optionLocator).stream().anyMatch(WebElement::isDisplayed));
                input.sendKeys(Keys.ENTER);
                return;
            } catch (TimeoutException e) {
                System.out.println("[Retry " + attempt + "/3] Chua thay menu cho: " + text);
            }
        }
        throw new TimeoutException("Khong thay option nao cho: " + text);
    }

    private void selectReact(By inputLocator, String text) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));
        pickOption(input, text);
    }
}
package demoqa.tests;

import demoqa.base.BaseTest;
import demoqa.pages.PracticeFormPage;
import demoqa.utils.ScreenshotUtil;
import demoqa.utils.StudentData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Lop test chi chua logic kiem thu, thao tac giao dien nam trong PracticeFormPage. */
public class PracticeFormTest extends BaseTest {

    private static final String UPLOAD_PATH =
            Paths.get("src/test/resources/sample.txt").toAbsolutePath().toString();

    private final StudentData defaultStudent = new StudentData(
            "Nguyen", "Van A", "vana@example.com", "Male", "0901234567",
            "15", "May", "2000", "Maths", "Reading",
            "123 Le Loi, Quan 1, TP.HCM", "NCR", "Delhi");

    // ---------- 1. Mo trang va kiem tra tieu de ----------
    @Test(priority = 1)
    public void tc01_openPageAndVerifyTitle() {
        PracticeFormPage page = new PracticeFormPage(driver).open();
        Assert.assertEquals(page.getCurrentUrl(), PracticeFormPage.URL, "URL khong dung");
        Assert.assertFalse(page.getTitle().trim().isEmpty(), "Tieu de trang rong");
        System.out.println("Title: " + page.getTitle());
    }

    // ---------- 2. Dien thong tin ca nhan ----------
    @Test(priority = 2)
    public void tc02_fillPersonalInfo() {
        PracticeFormPage page = new PracticeFormPage(driver).open();
        page.enterFirstName("Nguyen").enterLastName("Van A")
            .enterEmail("vana@example.com").enterMobile("0901234567");
        Assert.assertEquals(page.getFirstName(), "Nguyen");
        Assert.assertEquals(page.getLastName(), "Van A");
        Assert.assertEquals(page.getEmail(), "vana@example.com");
        Assert.assertEquals(page.getMobile(), "0901234567");
        System.out.printf("Da nhap: %s %s | %s | %s%n", page.getFirstName(),
                page.getLastName(), page.getEmail(), page.getMobile());
    }

    // ---------- 3. Chon ngay sinh bang date picker ----------
    @Test(priority = 3)
    public void tc03_selectDateOfBirth() {
        PracticeFormPage page = new PracticeFormPage(driver).open();
        page.selectDateOfBirth("15", "May", "2000");
        String dob = page.getDateOfBirthValue();
        System.out.println("Ngay sinh hien thi: " + dob);
        Assert.assertTrue(dob.matches("\\d{2} [A-Za-z]{3},? ?\\d{4}"), "Dinh dang ngay sai: " + dob);
        Assert.assertTrue(dob.contains("15") && dob.contains("May") && dob.contains("2000"));
    }

    // ---------- 4. Tai tep len form ----------
    @Test(priority = 4)
    public void tc04_uploadFile() {
        PracticeFormPage page = new PracticeFormPage(driver).open();
        page.uploadFile(UPLOAD_PATH);
        String name = page.getUploadedFileName();
        System.out.println("File da upload: " + name);
        Assert.assertEquals(name, "sample.txt");
    }

    // ---------- 5. Kiem tra thong bao xac nhan sau submit ----------
    @Test(priority = 5)
    public void tc05_verifyConfirmationModal() {
        PracticeFormPage page = new PracticeFormPage(driver).open();
        page.fillAndSubmit(defaultStudent, UPLOAD_PATH);
        ScreenshotUtil.captureScreen(driver, "before_submit_tc05");
        page.clickSubmit().waitForModal();
        ScreenshotUtil.captureScreen(driver, "after_submit_tc05");

        Assert.assertEquals(page.getModalTitle(), "Thanks for submitting the form");
        verifyModal(page, defaultStudent);
        System.out.println("[PASS] Modal xac nhan hien thi dung du lieu.");
    }

    // ---------- 7. Kiem thu voi nhieu bo du lieu ----------
    @DataProvider(name = "students")
    public Object[][] students() {
        return new Object[][]{
            {new StudentData("Nguyen", "Van A", "vana@example.com", "Male", "0901234567",
                    "15", "May", "2000", "Maths", "Reading", "Quan 1, TP.HCM", "NCR", "Delhi")},
            {new StudentData("Tran", "Thi B", "thib@example.com", "Female", "0912345678",
                    "01", "January", "1999", "English", "Sports", "Quan 3, TP.HCM", "Uttar Pradesh", "Agra")},
            {new StudentData("Le", "Van C", "vanc@example.com", "Other", "0923456789",
                    "28", "December", "2001", "Physics", "Music", "Thu Duc, TP.HCM", "Haryana", "Karnal")}
        };
    }

    @Test(priority = 6, dataProvider = "students")
    public void tc07_multipleDataSets(StudentData data) {
        System.out.println("=== Chay bo du lieu: " + data.fullName() + " ===");
        PracticeFormPage page = new PracticeFormPage(driver).open();
        page.fillAndSubmit(data, UPLOAD_PATH).clickSubmit().waitForModal();
        verifyModal(page, data);
        System.out.println("[PASS] " + data.fullName());
    }

    // ---------- 8. Tu dong hoa quy trinh hoan chinh ----------
    @Test(priority = 7)
    public void tc08_fullFlow() {
        PracticeFormPage page = new PracticeFormPage(driver).open();
        Assert.assertEquals(page.getCurrentUrl(), PracticeFormPage.URL);

        page.fillAndSubmit(defaultStudent, UPLOAD_PATH);
        Assert.assertEquals(page.getUploadedFileName(), "sample.txt");
        ScreenshotUtil.captureScreen(driver, "before_submit");

        page.clickSubmit().waitForModal();
        ScreenshotUtil.captureScreen(driver, "after_submit");

        Assert.assertEquals(page.getModalTitle(), "Thanks for submitting the form");
        verifyModal(page, defaultStudent);
        page.closeModal();
        System.out.println("[PASS] Full flow hoan tat.");
    }

    // ---------- helper kiem tra modal ----------
    private void verifyModal(PracticeFormPage page, StudentData d) {
        Assert.assertEquals(page.getModalValue("Student Name"), d.fullName());
        Assert.assertEquals(page.getModalValue("Student Email"), d.email());
        Assert.assertEquals(page.getModalValue("Gender"), d.gender());
        Assert.assertEquals(page.getModalValue("Mobile"), d.mobile());
        Assert.assertTrue(page.getModalValue("Date of Birth").contains(d.year()));
        Assert.assertEquals(page.getModalValue("Subjects"), d.subject());
        Assert.assertEquals(page.getModalValue("Hobbies"), d.hobby());
        Assert.assertEquals(page.getModalValue("Picture"), "sample.txt");
        Assert.assertEquals(page.getModalValue("Address"), d.address());
        Assert.assertEquals(page.getModalValue("State and City"), d.state() + " " + d.city());
        System.out.println("Xac nhan: " + page.getModalValue("Student Name") + " | "
                + page.getModalValue("Student Email") + " | " + page.getModalValue("State and City"));
    }
}

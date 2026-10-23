package demoqa.base;

import demoqa.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        // options.addArguments("--headless=new"); // bo comment neu muon chay an
        driver = new ChromeDriver(options); // Selenium Manager tu tai chromedriver
        driver.manage().timeouts().implicitlyWait(Duration.ZERO); // chi dung explicit wait
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                ScreenshotUtil.captureScreen(driver, "FAIL_" + result.getName());
            }
            driver.quit();
        }
    }
}

package demoqa.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {
    public static void captureScreen(WebDriver driver, String prefix) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File dest = new File("screenshots/" + prefix + "_" + time + ".png");
            FileUtils.copyFile(src, dest);
            System.out.println("[Screenshot] " + dest.getPath());
        } catch (IOException e) {
            System.out.println("[Screenshot] Loi: " + e.getMessage());
        }
    }
}

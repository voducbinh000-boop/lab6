# Báo cáo thực hành Selenium – DemoQA Practice Form

**Trang kiểm thử:** https://demoqa.com/automation-practice-form
**Công nghệ:** Java 17, Maven, Selenium WebDriver 4.25, TestNG 7, Chrome

## 1. Cấu trúc project
- `pages/PracticeFormPage` – Page Object: locator + hành động (không assert).
- `tests/PracticeFormTest` – logic kiểm thử (TestNG, DataProvider).
- `base/BaseTest` – khởi tạo/đóng Chrome, chụp ảnh khi test lỗi.
- `utils/ScreenshotUtil` – hàm chụp màn hình (theo mẫu đề bài).
- `utils/StudentData` – dữ liệu đầu vào.

## 2. Các test case
| TC | Nội dung | Kết quả mong đợi |
|----|----------|------------------|
| 01 | Mở trang, kiểm tra URL và tiêu đề không rỗng | Pass |
| 02 | Điền Họ, Tên, Email, SĐT và in ra console | Giá trị khớp |
| 03 | Chọn ngày sinh bằng date picker | Hiển thị đúng ngày/tháng/năm |
| 04 | Upload file `sample.txt` | Tên file hiển thị đúng |
| 05 | Submit, kiểm tra modal xác nhận | Nội dung modal = dữ liệu đã nhập |
| 06 | Page Object Model | Áp dụng cho toàn bộ test |
| 07 | 3 bộ dữ liệu (DataProvider) | Mỗi bộ có kết quả riêng, in console |
| 08 | Quy trình hoàn chỉnh + chụp ảnh trước/sau submit | Pass |

## 3. Explicit wait
Dùng `WebDriverWait` + `ExpectedConditions` ở: chờ form tải, chờ date picker mở/đóng, chờ menu react-select, chờ modal xuất hiện/đóng.

## 4. Chụp màn hình
Lưu trong thư mục `screenshots/`: `before_submit_*.png`, `after_submit_*.png`, `FAIL_*.png` (khi lỗi).

## 5. Cách chạy
```
mvn clean test
```
Kết quả: `target/surefire-reports/`. Dán ảnh chụp console + ảnh trong `screenshots/` vào báo cáo nộp.

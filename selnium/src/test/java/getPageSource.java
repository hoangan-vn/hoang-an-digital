import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class getPageSource {

  @Test
  public void scrapePageWithCSS() {
    WebDriver driver = null;
    String URL = "https://kdl3govx7cxsk.ok.kimi.link/"; // input URL user want to clone
    try {
      driver = createFirefoxDriver();
      JavascriptExecutor js = (JavascriptExecutor) driver;
      driver.get(URL);
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
      Thread.sleep(2000);
      String htmlSource = driver.getPageSource();

      // Tạo thư mục result nếu chưa tồn tại
      new File("./result").mkdirs();

      saveToFile("NextJS_SourceCode.html", htmlSource);
      System.out.println("✓ Đã lưu HTML source (" + htmlSource.length() + " ký tự)");

      StringBuilder inlineCSS = new StringBuilder();
      List<WebElement> styleElements = driver.findElements(By.tagName("style"));

      for (int i = 0; i < styleElements.size(); i++) {
        String cssContent = (String) js.executeScript(
            "return arguments[0].innerHTML;", styleElements.get(i)
        );
        inlineCSS.append("/* ========== Style Tag #").append(i + 1)
            .append(" ========== */\n");
        inlineCSS.append(cssContent).append("\n\n");
      }

      saveToFile("Inline_Styles.css", inlineCSS.toString());
      System.out.println("✓ Đã lưu " + styleElements.size() + " inline style tags");

      List<WebElement> linkElements = driver.findElements(
          By.cssSelector("link[rel='stylesheet']")
      );

      StringBuilder allExternalCSS = new StringBuilder();
      int successCount = 0;

      for (int i = 0; i < linkElements.size(); i++) {
        String href = linkElements.get(i).getAttribute("href");

        if (href != null && !href.isEmpty()) {
          System.out.println("Đang tải CSS #" + (i + 1) + ": " + href);

          String cssContent = downloadCSS(href);
          if (cssContent != null) {
            allExternalCSS.append("/* ========== External CSS #")
                .append(i + 1).append(" ========== */\n");
            allExternalCSS.append("/* URL: ").append(href)
                .append(" */\n\n");
            allExternalCSS.append(cssContent).append("\n\n");
            successCount++;
          }
        }
      }

      saveToFile("External_Styles.css", allExternalCSS.toString());
      System.out.println("✓ Đã tải thành công " + successCount + "/" +
          linkElements.size() + " external CSS files");

      String computedStyles = extractComputedStyles(driver, js);
      saveToFile("Computed_Styles.css", computedStyles);
      System.out.println("✓ Đã lưu computed styles");

      String completeHTML = createCompleteHTML(htmlSource, inlineCSS.toString(),
          allExternalCSS.toString());
      saveToFile("Complete_Page.html", completeHTML);
      System.out.println("✓ Đã tạo file HTML hoàn chỉnh với tất cả CSS");

      System.out.println("\n=== HOÀN TẤT ===");
      System.out.println("Tất cả files đã được lưu trong thư mục dự án!");

    } catch (Exception e) {
      System.err.println("Lỗi: " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }

  /**
   * Khởi tạo Firefox WebDriver
   */
  private WebDriver createFirefoxDriver() {
    WebDriverManager.firefoxdriver().setup();

    FirefoxOptions options = new FirefoxOptions();

    // Headless mode - bỏ comment nếu chạy trên server/CI không có GUI
    // options.addArguments("--headless");

    // Tìm binary Firefox theo thứ tự ưu tiên
    String firefoxBinary = findFirstExecutable(
        System.getenv("FIREFOX_BIN"),
        "/snap/firefox/current/usr/lib/firefox/firefox",
        "/usr/lib/firefox/firefox",
        "/usr/lib64/firefox/firefox",
        "/opt/firefox/firefox",
        "/usr/bin/firefox",
        "C:\\Program Files\\Mozilla Firefox\\firefox.exe",       // Windows
        "/Applications/Firefox.app/Contents/MacOS/firefox"       // macOS
    );

    if (firefoxBinary != null) {
      System.out.println("Sử dụng Firefox tại: " + firefoxBinary);
      options.setBinary(firefoxBinary);
    } else {
      System.out.println("Không tìm thấy binary Firefox, thử dùng PATH mặc định...");
    }

    // Các preferences tối ưu cho scraping
    options.addPreference("permissions.default.image", 2);       // Không tải ảnh (tùy chọn, tắt nếu cần)
    options.addPreference("dom.webnotifications.enabled", false); // Tắt thông báo
    options.addPreference("media.volume_scale", "0.0");           // Tắt âm thanh

    return new FirefoxDriver(options);
  }

  /**
   * Tìm đường dẫn executable đầu tiên hợp lệ
   */
  private String findFirstExecutable(String... candidates) {
    for (String candidate : candidates) {
      if (candidate == null || candidate.isBlank()) {
        continue;
      }

      File file = new File(candidate);
      if (file.exists() && file.isFile() && file.canExecute()) {
        return file.getAbsolutePath();
      }
    }
    return null;
  }

  /**
   * Tải nội dung CSS từ URL
   */
  private String downloadCSS(String cssUrl) {
    try {
      URL url = new URL(cssUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Mozilla/5.0");
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);

      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8");
        scanner.useDelimiter("\\A");
        String content = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return content;
      } else {
        System.out.println("  ⚠ Không thể tải (HTTP " + responseCode + "): " + cssUrl);
      }
    } catch (Exception e) {
      System.out.println("  ⚠ Lỗi khi tải: " + cssUrl);
    }
    return null;
  }

  /**
   * Trích xuất computed styles từ các elements quan trọng
   */
  private String extractComputedStyles(WebDriver driver, JavascriptExecutor js) {
    String script =
        "let styles = '';" +
            "document.querySelectorAll('*').forEach((el, index) => {" +
            "  if (index < 50) {" + // Giới hạn 50 elements đầu tiên
            "    try {" +
            "      let computed = window.getComputedStyle(el);" +
            "      let selector = el.tagName.toLowerCase();" +
            "      if (el.id) selector += '#' + el.id;" +
            // Ép className về String để tránh lỗi SVGAnimatedString
            "      let className = (typeof el.className === 'string') " +
            "        ? el.className " +
            "        : (el.className.baseVal || '');" +
            "      if (className.trim()) selector += '.' + className.trim().split(/\\s+/).join('.');" +
            "      styles += '/* Element: ' + selector + ' */\\n';" +
            "      styles += selector + ' {\\n';" +
            "      for (let prop of ['display', 'position', 'width', 'height', " +
            "                        'margin', 'padding', 'color', 'background', " +
            "                        'font-size', 'font-family']) {" +
            "        styles += '  ' + prop + ': ' + computed[prop] + ';\\n';" +
            "      }" +
            "      styles += '}\\n\\n';" +
            "    } catch(e) {" +
            "      styles += '/* Skipped element (error): ' + e.message + ' */\\n\\n';" +
            "    }" +
            "  }" +
            "});" +
            "return styles;";

    return (String) js.executeScript(script);
  }

  /**
   * Tạo HTML hoàn chỉnh với tất cả CSS được embed
   */
  private String createCompleteHTML(String html, String inlineCSS, String externalCSS) {
    StringBuilder complete = new StringBuilder();
    int headEndIndex = html.indexOf("</head>");

    if (headEndIndex != -1) {
      complete.append(html, 0, headEndIndex);
      complete.append("\n<!-- ========== EMBEDDED CSS ========== -->\n");
      complete.append("<style>\n");
      complete.append("/* Inline Styles */\n");
      complete.append(inlineCSS);
      complete.append("\n/* External Styles */\n");
      complete.append(externalCSS);
      complete.append("</style>\n");
      complete.append(html.substring(headEndIndex));
    } else {
      complete.append(html);
    }

    return complete.toString();
  }

  /**
   * Lưu nội dung vào file
   */
  private void saveToFile(String filename, String content) throws IOException {
    FileWriter fileWriter = new FileWriter("./result/" + filename);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    printWriter.print(content);
    printWriter.close();
  }
}
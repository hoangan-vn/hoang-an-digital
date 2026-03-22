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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getPageSource {

  @Test
  public void scrapePageWithCSS() {
    WebDriver driver = null;
    String URL = "https://kdl3govx7cxsk.ok.kimi.link/"; // input URL user want to clone
    try {
      driver = createFirefoxDriver();
      JavascriptExecutor js = (JavascriptExecutor) driver;
      driver.get(URL);
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
      waitForDocumentReady(driver);
      scrollPageToLoadLazyContent(js);
      Thread.sleep(1500);
      waitForDocumentReady(driver);
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

      String externalCssMerged = allExternalCSS.toString();
      saveToFile("External_Styles.css", externalCssMerged);
      System.out.println("✓ Đã tải thành công " + successCount + "/" +
          linkElements.size() + " external CSS files");

      File assetsDir = new File("./result/assets");
      assetsDir.mkdirs();
      Map<String, String> urlToLocal = new LinkedHashMap<>();
      String pageBase = driver.getCurrentUrl();
      mergeCssUrlsIntoDownloadQueue(externalCssMerged, pageBase, urlToLocal);
      collectAssetUrlsFromDom(js, urlToLocal);
      int savedAssets = downloadAssets(urlToLocal, assetsDir);
      System.out.println("✓ Đã tải " + savedAssets + " file ảnh/font/media (vào result/assets/)");

      externalCssMerged = rewriteUrlsInText(externalCssMerged, urlToLocal);
      saveToFile("External_Styles_local.css", externalCssMerged);

      htmlSource = rewriteUrlsInText(htmlSource, urlToLocal);

      String computedStyles = extractComputedStyles(driver, js);
      saveToFile("Computed_Styles.css", computedStyles);
      System.out.println("✓ Đã lưu computed styles");

      String inlineForEmbed = rewriteUrlsInText(inlineCSS.toString(), urlToLocal);
      String completeHTML = createCompleteHTML(htmlSource, inlineForEmbed,
          externalCssMerged);
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

    // Bật tải ảnh để DOM và clone offline có đủ media (trước đây =2 là chặn ảnh → thiếu nội dung).
    options.addPreference("permissions.default.image", 1);
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
            "  if (index < 300) {" + // Giới hạn để file không quá lớn; tăng nếu cần
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

  private void waitForDocumentReady(WebDriver driver) {
    new WebDriverWait(driver, Duration.ofSeconds(30)).until(
        d -> "complete".equals(
            ((JavascriptExecutor) d).executeScript("return document.readyState"))
    );
  }

  /**
   * Cuộn từng đoạn để lazy-load ảnh / nội dung (Next.js, v.v.).
   */
  private void scrollPageToLoadLazyContent(JavascriptExecutor js) {
    Object h = js.executeScript(
        "return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);"
    );
    long height = h instanceof Number ? ((Number) h).longValue() : 0L;
    int step = Math.max(400, (int) Math.min(height / 10, 900));
    for (long y = 0; y <= height; y += step) {
      js.executeScript("window.scrollTo(0, arguments[0]);", y);
      try {
        Thread.sleep(120);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
    js.executeScript("window.scrollTo(0, 0);");
  }

  private static final Pattern CSS_URL_PATTERN = Pattern.compile(
      "url\\(\\s*([\"']?)([^\"')\\s]+)\\1\\s*\\)",
      Pattern.CASE_INSENSITIVE | Pattern.DOTALL
  );

  private void mergeCssUrlsIntoDownloadQueue(
      String css, String pageBase, Map<String, String> urlToLocal) {
    if (css == null || css.isEmpty()) {
      return;
    }
    Matcher m = CSS_URL_PATTERN.matcher(css);
    while (m.find()) {
      String raw = m.group(2).trim();
      if (raw.isEmpty() || raw.startsWith("data:") || raw.startsWith("#")) {
        continue;
      }
      String abs = toAbsoluteAssetUrl(raw, pageBase);
      if (abs != null) {
        urlToLocal.putIfAbsent(abs, null);
      }
    }
  }

  private String toAbsoluteAssetUrl(String raw, String pageBase) {
    try {
      if (raw.startsWith("//")) {
        URI base = URI.create(pageBase);
        String scheme = base.getScheme() != null ? base.getScheme() : "https";
        return URI.create(scheme + ":" + raw).normalize().toString();
      }
      if (raw.startsWith("http://") || raw.startsWith("https://")) {
        return raw;
      }
      return URI.create(pageBase).resolve(raw).normalize().toString();
    } catch (Exception e) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private void collectAssetUrlsFromDom(JavascriptExecutor js, Map<String, String> urlToLocal) {
    String script =
        "var out = [];"
            + "function add(u) {"
            + "  if (!u || u.indexOf('data:') === 0 || u.indexOf('blob:') === 0) return;"
            + "  try { out.push(new URL(u, document.baseURI).href); } catch (e) {}"
            + "}"
            + "document.querySelectorAll('img[src]').forEach(function(el) { add(el.getAttribute('src')); });"
            + "document.querySelectorAll('img[srcset]').forEach(function(el) {"
            + "  (el.getAttribute('srcset') || '').split(',').forEach(function(part) {"
            + "    var u = part.trim().split(/\\s+/)[0];"
            + "    add(u);"
            + "  });"
            + "});"
            + "document.querySelectorAll('source[srcset], source[src]').forEach(function(el) {"
            + "  if (el.getAttribute('src')) add(el.getAttribute('src'));"
            + "  (el.getAttribute('srcset') || '').split(',').forEach(function(part) {"
            + "    var u = part.trim().split(/\\s+/)[0];"
            + "    add(u);"
            + "  });"
            + "});"
            + "document.querySelectorAll('video[poster]').forEach(function(el) {"
            + "  add(el.getAttribute('poster'));"
            + "});"
            + "document.querySelectorAll("
            + "  'link[rel~=\"icon\"][href], link[rel=\"preload\"][href], "
            + "link[rel=\"apple-touch-icon\"][href]'"
            + ").forEach(function(el) { add(el.getAttribute('href')); });"
            + "return out;";
    Object raw = js.executeScript(script);
    if (!(raw instanceof List)) {
      return;
    }
    for (Object o : (List<Object>) raw) {
      if (o == null) {
        continue;
      }
      String u = o.toString();
      if (u.startsWith("http://") || u.startsWith("https://")) {
        urlToLocal.putIfAbsent(u, null);
      }
    }
  }

  private int downloadAssets(Map<String, String> urlToLocal, File assetsDir) {
    int saved = 0;
    List<String> keys = new ArrayList<>(urlToLocal.keySet());
    for (String url : keys) {
      if (urlToLocal.get(url) != null) {
        continue;
      }
      String local = downloadBinaryToAssetsFile(url, assetsDir);
      if (local != null) {
        urlToLocal.put(url, local);
        saved++;
      }
    }
    return saved;
  }

  private String downloadBinaryToAssetsFile(String urlString, File assetsDir) {
    HttpURLConnection connection = null;
    try {
      URL url = new URL(urlString);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
              + "Chrome/120.0.0.0 Safari/537.36");
      connection.setConnectTimeout(20000);
      connection.setReadTimeout(20000);
      connection.setInstanceFollowRedirects(true);
      int code = connection.getResponseCode();
      if (code != 200) {
        System.out.println("  ⚠ Asset HTTP " + code + ": " + urlString);
        return null;
      }
      String ext = guessExtension(connection.getContentType(), urlString);
      String fileName = "a_" + sha256Short(urlString) + ext;
      File outFile = new File(assetsDir, fileName);
      try (InputStream in = connection.getInputStream();
           FileOutputStream fo = new FileOutputStream(outFile)) {
        in.transferTo(fo);
      }
      return "assets/" + fileName;
    } catch (Exception e) {
      System.out.println("  ⚠ Không tải được asset: " + urlString);
      return null;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private static String sha256Short(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] d = md.digest(input.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder(16);
      for (int i = 0; i < 8; i++) {
        sb.append(String.format(Locale.ROOT, "%02x", d[i]));
      }
      return sb.toString();
    } catch (Exception e) {
      return String.valueOf(Math.abs(input.hashCode()));
    }
  }

  private static String guessExtension(String contentType, String urlString) {
    if (contentType != null) {
      String ct = contentType.toLowerCase(Locale.ROOT).split(";")[0].trim();
      switch (ct) {
        case "image/jpeg":
          return ".jpg";
        case "image/png":
          return ".png";
        case "image/gif":
          return ".gif";
        case "image/webp":
          return ".webp";
        case "image/svg+xml":
          return ".svg";
        case "font/woff2":
          return ".woff2";
        case "font/woff":
          return ".woff";
        case "font/ttf":
        case "application/x-font-ttf":
          return ".ttf";
        case "video/mp4":
          return ".mp4";
        default:
          break;
      }
    }
    String path = urlString;
    try {
      path = URI.create(urlString).getPath();
    } catch (Exception ignored) {
      // keep urlString
    }
    int q = path.indexOf('?');
    if (q >= 0) {
      path = path.substring(0, q);
    }
    int dot = path.lastIndexOf('.');
    if (dot > 0 && dot < path.length() - 1) {
      String ext = path.substring(dot).toLowerCase(Locale.ROOT);
      if (ext.length() <= 6 && ext.matches("\\.[a-z0-9]+")) {
        return ext;
      }
    }
    return ".bin";
  }

  private String rewriteUrlsInText(String text, Map<String, String> urlToLocal) {
    if (text == null || text.isEmpty()) {
      return text;
    }
    List<Map.Entry<String, String>> entries = new ArrayList<>();
    for (Map.Entry<String, String> e : urlToLocal.entrySet()) {
      if (e.getValue() != null) {
        entries.add(e);
      }
    }
    entries.sort(Comparator.comparingInt((Map.Entry<String, String> en) -> en.getKey().length())
        .reversed());
    String out = text;
    for (Map.Entry<String, String> e : entries) {
      out = out.replace(e.getKey(), e.getValue());
    }
    return out;
  }
}
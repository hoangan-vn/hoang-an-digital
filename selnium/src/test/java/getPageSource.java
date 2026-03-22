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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
      waitForImagesToSettle(js, 18_000);
      Thread.sleep(2_500);
      waitForDocumentReady(driver);
      String htmlSource = driver.getPageSource();

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
      mergeCssUrlsIntoDownloadQueue(inlineCSS.toString(), pageBase, urlToLocal);
      mergeCssUrlsIntoDownloadQueue(externalCssMerged, pageBase, urlToLocal);
      mergeCssUrlsIntoDownloadQueue(htmlSource, pageBase, urlToLocal);
      collectAssetUrlsFromDom(js, urlToLocal);
      collectInlineStyleAssetUrls(driver, pageBase, urlToLocal);
      int savedAssets = downloadAssets(urlToLocal, assetsDir, pageBase);
      System.out.println("✓ Đã tải " + savedAssets + " file ảnh/font/media (vào result/assets/)");

      patchDomAssetReferences(js, urlToLocal, pageBase);
      htmlSource = driver.getPageSource();

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

  private WebDriver createFirefoxDriver() {
    WebDriverManager.firefoxdriver().setup();

    FirefoxOptions options = new FirefoxOptions();

    String firefoxBinary = findFirstExecutable(
        System.getenv("FIREFOX_BIN"),
        "/snap/firefox/current/usr/lib/firefox/firefox",
        "/usr/lib/firefox/firefox",
        "/usr/lib64/firefox/firefox",
        "/opt/firefox/firefox",
        "/usr/bin/firefox",
        "C:\\Program Files\\Mozilla Firefox\\firefox.exe",
        "/Applications/Firefox.app/Contents/MacOS/firefox"
    );

    if (firefoxBinary != null) {
      System.out.println("Sử dụng Firefox tại: " + firefoxBinary);
      options.setBinary(firefoxBinary);
    } else {
      System.out.println("Không tìm thấy binary Firefox, thử dùng PATH mặc định...");
    }

    options.addPreference("permissions.default.image", 1);
    options.addPreference("dom.webnotifications.enabled", false);
    options.addPreference("media.volume_scale", "0.0");

    return new FirefoxDriver(options);
  }

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

  private String extractComputedStyles(WebDriver driver, JavascriptExecutor js) {
    String script =
        "let styles = '';" +
            "document.querySelectorAll('*').forEach((el, index) => {" +
            "  if (index < 300) {" +
            "    try {" +
            "      let computed = window.getComputedStyle(el);" +
            "      let selector = el.tagName.toLowerCase();" +
            "      if (el.id) selector += '#' + el.id;" +
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
   * Cuộn chậm (bước nhỏ + nghỉ lâu) để lazy-load ảnh kịp; lặp 2 lượt.
   */
  private void scrollPageToLoadLazyContent(JavascriptExecutor js) throws InterruptedException {
    for (int pass = 0; pass < 2; pass++) {
      Object h = js.executeScript(
          "return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);"
      );
      long height = h instanceof Number ? ((Number) h).longValue() : 0L;
      int step = 320;
      for (long y = 0; y <= height; y += step) {
        js.executeScript("window.scrollTo(0, arguments[0]);", y);
        Thread.sleep(320);
      }
      js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
      Thread.sleep(600);
      js.executeScript("window.scrollTo(0, 0);");
      Thread.sleep(400);
    }
  }

  /**
   * Chờ ảnh http(s) có complete + naturalWidth &gt; 0 (hoặc hết timeout).
   */
  private void waitForImagesToSettle(JavascriptExecutor js, int maxWaitMs)
      throws InterruptedException {
    long deadline = System.currentTimeMillis() + maxWaitMs;
    while (System.currentTimeMillis() < deadline) {
      Object n = js.executeScript(
          "var n=0;"
              + "document.querySelectorAll('img').forEach(function(i){"
              + "  var s = i.getAttribute('src')||'';"
              + "  if (!s || s.indexOf('data:')===0 || s.indexOf('blob:')===0) return;"
              + "  if (!i.complete || i.naturalWidth===0) n++;"
              + "});"
              + "return n;"
      );
      long pending = n instanceof Number ? ((Number) n).longValue() : 0L;
      if (pending == 0) {
        return;
      }
      Thread.sleep(450);
    }
  }

  private static final Pattern CSS_URL_PATTERN = Pattern.compile(
      "url\\(\\s*(?:\"([^\"]*)\"|'([^']*)'|([^\\)]+))\\s*\\)",
      Pattern.CASE_INSENSITIVE | Pattern.DOTALL
  );

  private static String cssUrlMatchGroup(Matcher m) {
    if (m.group(1) != null) {
      return m.group(1);
    }
    if (m.group(2) != null) {
      return m.group(2);
    }
    return m.group(3);
  }

  private void mergeCssUrlsIntoDownloadQueue(
      String css, String pageBase, Map<String, String> urlToLocal) {
    if (css == null || css.isEmpty()) {
      return;
    }
    Matcher m = CSS_URL_PATTERN.matcher(css);
    while (m.find()) {
      String raw = cssUrlMatchGroup(m);
      if (raw == null) {
        continue;
      }
      raw = raw.trim();
      if (raw.isEmpty() || raw.startsWith("data:") || raw.startsWith("#")) {
        continue;
      }
      String abs = toAbsoluteAssetUrl(raw, pageBase);
      if (abs != null) {
        urlToLocal.putIfAbsent(abs, null);
      }
    }
  }

  /**
   * url(...) trong thuộc tính style="" — xử lý bằng Java để tránh lỗi regex trong executeScript.
   */
  private void collectInlineStyleAssetUrls(
      WebDriver driver, String pageBase, Map<String, String> urlToLocal) {
    List<WebElement> styled = driver.findElements(By.cssSelector("[style]"));
    for (WebElement el : styled) {
      String st = el.getAttribute("style");
      if (st == null || st.isEmpty()) {
        continue;
      }
      Matcher m = CSS_URL_PATTERN.matcher(st);
      while (m.find()) {
        String raw = cssUrlMatchGroup(m);
        if (raw == null) {
          continue;
        }
        raw = raw.trim();
        if (raw.isEmpty() || raw.startsWith("data:") || raw.startsWith("#")) {
          continue;
        }
        String abs = toAbsoluteAssetUrl(raw, pageBase);
        if (abs != null) {
          urlToLocal.putIfAbsent(abs, null);
        }
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
            + "function splitSrcset(ss) {"
            + "  (ss || '').split(',').forEach(function(part) {"
            + "    var u = part.trim().split(/\\s+/)[0];"
            + "    add(u);"
            + "  });"
            + "}"
            + "document.querySelectorAll('img').forEach(function(el) {"
            + "  add(el.getAttribute('src'));"
            + "  add(el.getAttribute('data-src'));"
            + "  add(el.getAttribute('data-original'));"
            + "  add(el.getAttribute('data-lazy-src'));"
            + "  add(el.getAttribute('data-url'));"
            + "  splitSrcset(el.getAttribute('srcset'));"
            + "  splitSrcset(el.getAttribute('data-srcset'));"
            + "  try { if (el.currentSrc) add(el.currentSrc); } catch (e) {}"
            + "});"
            + "document.querySelectorAll('input[type=\"image\"][src]').forEach(function(el) {"
            + "  add(el.getAttribute('src'));"
            + "});"
            + "document.querySelectorAll('source[srcset], source[src]').forEach(function(el) {"
            + "  if (el.getAttribute('src')) add(el.getAttribute('src'));"
            + "  splitSrcset(el.getAttribute('srcset'));"
            + "});"
            + "document.querySelectorAll('video[poster]').forEach(function(el) {"
            + "  add(el.getAttribute('poster'));"
            + "});"
            + "document.querySelectorAll("
            + "  'link[rel~=\"icon\"][href], link[rel=\"preload\"][href], "
            + "link[rel=\"apple-touch-icon\"][href]'"
            + ").forEach(function(el) { add(el.getAttribute('href')); });"
            + "document.querySelectorAll("
            + "  'meta[property=\"og:image\"][content], meta[name=\"twitter:image\"][content]'"
            + ").forEach(function(el) { add(el.getAttribute('content')); });"
            + "document.querySelectorAll('svg image').forEach(function(el) {"
            + "  var h = el.getAttribute('href');"
            + "  if (!h && el.getAttributeNS) {"
            + "    h = el.getAttributeNS('http://www.w3.org/1999/xlink', 'href');"
            + "  }"
            + "  add(h);"
            + "});"
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

  private void patchDomAssetReferences(
      JavascriptExecutor js, Map<String, String> urlToLocal, String pageBase) {
    Map<String, String> localToRepresentativeAbs = new LinkedHashMap<>();
    for (Map.Entry<String, String> e : urlToLocal.entrySet()) {
      if (e.getValue() == null) {
        continue;
      }
      String local = e.getValue();
      String abs = e.getKey();
      localToRepresentativeAbs.merge(
          local, abs, (a, b) -> a.length() >= b.length() ? a : b);
    }
    List<Object> pairs = new ArrayList<>();
    for (Map.Entry<String, String> e : localToRepresentativeAbs.entrySet()) {
      pairs.add(new String[]{e.getValue(), e.getKey()});
    }
    if (pairs.isEmpty()) {
      return;
    }
    js.executeScript(
        "var pairs = arguments[0];"
            + "var base = arguments[1] || document.baseURI;"
            + "function norm(h) {"
            + "  try { return new URL(h, base).href; } catch (err) { return h; }"
            + "}"
            + "function matches(u, abs) {"
            + "  if (!u || !abs) return false;"
            + "  try { return norm(u) === norm(abs); } catch (e) { return u === abs; }"
            + "}"
            + "pairs.forEach(function(pair) {"
            + "  var abs = pair[0]; var local = pair[1];"
            + "  document.querySelectorAll('img').forEach(function(el) {"
            + "    var cs = '';"
            + "    try { cs = el.currentSrc || ''; } catch (e) {}"
            + "    if (matches(cs, abs) || matches(el.getAttribute('src'), abs)"
            + "        || matches(el.getAttribute('data-src'), abs)"
            + "        || matches(el.getAttribute('data-lazy-src'), abs)"
            + "        || matches(el.getAttribute('data-original'), abs)) {"
            + "      el.setAttribute('src', local);"
            + "      el.removeAttribute('srcset');"
            + "      el.removeAttribute('data-srcset');"
            + "      el.removeAttribute('data-src');"
            + "      el.removeAttribute('data-lazy-src');"
            + "    }"
            + "  });"
            + "  document.querySelectorAll('source[src]').forEach(function(el) {"
            + "    if (matches(el.getAttribute('src'), abs)) el.setAttribute('src', local);"
            + "  });"
            + "  document.querySelectorAll('video[poster]').forEach(function(el) {"
            + "    if (matches(el.getAttribute('poster'), abs)) el.setAttribute('poster', local);"
            + "  });"
            + "  document.querySelectorAll('input[type=\"image\"][src]').forEach(function(el) {"
            + "    if (matches(el.getAttribute('src'), abs)) el.setAttribute('src', local);"
            + "  });"
            + "  document.querySelectorAll('svg image').forEach(function(el) {"
            + "    var h = el.getAttribute('href');"
            + "    if (!h && el.getAttributeNS) {"
            + "      h = el.getAttributeNS('http://www.w3.org/1999/xlink', 'href');"
            + "    }"
            + "    if (matches(h, abs)) {"
            + "      el.setAttribute('href', local);"
            + "      if (el.setAttributeNS) {"
            + "        el.setAttributeNS('http://www.w3.org/1999/xlink', 'href', local);"
            + "      }"
            + "    }"
            + "  });"
            + "});",
        pairs,
        pageBase
    );
  }

  private int downloadAssets(
      Map<String, String> urlToLocal, File assetsDir, String refererPage) {
    int saved = 0;
    List<String> keys = new ArrayList<>(urlToLocal.keySet());
    for (String url : keys) {
      if (urlToLocal.get(url) != null) {
        continue;
      }
      String local = downloadBinaryToAssetsFile(url, assetsDir, refererPage);
      if (local != null) {
        registerUrlAliases(url, local, urlToLocal);
        saved++;
      }
    }
    return saved;
  }

  private void registerUrlAliases(String originalUrl, String localPath, Map<String, String> urlToLocal) {
    for (String variant : computeUrlVariants(originalUrl)) {
      urlToLocal.put(variant, localPath);
    }
  }

  private static List<String> computeUrlVariants(String u) {
    LinkedHashSet<String> set = new LinkedHashSet<>();
    if (u != null && !u.isBlank()) {
      set.add(u);
      try {
        String ascii = URI.create(u).toASCIIString();
        set.add(ascii);
      } catch (Exception ignored) {
        // skip
      }
      if (u.contains("&")) {
        set.add(u.replace("&", "&amp;"));
      }
      try {
        String dec = URLDecoder.decode(u, StandardCharsets.UTF_8);
        if (!dec.equals(u)) {
          set.add(dec);
        }
      } catch (Exception ignored) {
        // skip
      }
    }
    return new ArrayList<>(set);
  }

  private String downloadBinaryToAssetsFile(String urlString, File assetsDir, String refererPage) {
    HttpURLConnection connection = null;
    try {
      URL url = new URL(urlString);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
              + "Chrome/120.0.0.0 Safari/537.36");
      connection.setRequestProperty("Accept",
          "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
      if (refererPage != null && !refererPage.isBlank()) {
        connection.setRequestProperty("Referer", refererPage);
      }
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
      System.out.println("  ⚠ Không tải được asset: " + urlString + " — " + e.getMessage());
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
        case "image/avif":
          return ".avif";
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

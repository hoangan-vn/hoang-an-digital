# Template Email Service Documentation

## Tổng quan

Template Email Service mở rộng Mail Service với khả năng gửi email sử dụng templates có sẵn với dữ liệu tùy chỉnh. Service hỗ trợ:

- **Template-based emails** với FreeMarker
- **Dynamic content** với variables
- **Bulk template emails** cho nhiều recipients
- **Template preview** với sample data
- **Template management** và documentation

## Tính năng mới

### 1. **Template Email System**

- **Pre-built templates** cho các use cases phổ biến
- **Dynamic data binding** với FreeMarker
- **Template validation** và error handling
- **Template preview** functionality

### 2. **Available Templates**

#### **Newsletter Template**

- **Template name**: `newsletter`
- **Use case**: Weekly/monthly newsletters
- **Required variables**: `title`, `content`, `articles`
- **Optional variables**: `unsubscribeLink`, `currentYear`

#### **Promotion Template**

- **Template name**: `promotion`
- **Use case**: Promotional campaigns, sales
- **Required variables**: `promoTitle`, `discount`, `validUntil`, `promoCode`
- **Optional variables**: `productImage`, `currentYear`

## API Endpoints

### **Template Email Operations**

#### 1. Send Template Email

```http
POST /api/v1/mail/send/template
Content-Type: application/json

{
  "to": "user@example.com",
  "cc": ["manager@example.com"],
  "bcc": ["admin@example.com"],
  "subject": "Weekly Newsletter - Happy Ending",
  "templateName": "newsletter",
  "templateData": {
    "title": "Weekly Newsletter",
    "content": "Check out our latest updates and offers!",
    "articles": [
      {
        "title": "New Products",
        "summary": "Discover our latest arrivals",
        "link": "https://happyending.com/new-products"
      },
      {
        "title": "Special Offers",
        "summary": "Don't miss our exclusive deals",
        "link": "https://happyending.com/offers"
      }
    ],
    "unsubscribeLink": "https://happyending.com/unsubscribe",
    "currentYear": 2024
  },
  "attachments": []
}
```

#### 2. Send Bulk Template Email

```http
POST /api/v1/mail/send/template/bulk
Content-Type: application/json

{
  "recipients": ["user1@example.com", "user2@example.com", "user3@example.com"],
  "subject": "Black Friday Sale - 50% OFF!",
  "templateName": "promotion",
  "templateData": {
    "promoTitle": "Black Friday Sale",
    "discount": "50% OFF",
    "validUntil": "2024-01-31",
    "promoCode": "BLACKFRIDAY50",
    "productImage": "https://example.com/black-friday-banner.jpg",
    "currentYear": 2024
  }
}
```

#### 3. Get Available Templates

```http
GET /api/v1/mail/templates
```

Response:

```json
{
  "success": true,
  "data": [
    {
      "templateName": "newsletter",
      "subject": "{{title}} - Happy Ending Newsletter",
      "description": "Newsletter email template",
      "requiredVariables": ["title", "content", "articles"],
      "optionalVariables": ["unsubscribeLink", "currentYear"],
      "exampleData": {
        "title": "Weekly Newsletter",
        "content": "Check out our latest updates and offers!",
        "articles": [
          {
            "title": "New Products",
            "summary": "Discover our latest arrivals"
          }
        ],
        "unsubscribeLink": "https://app.com/unsubscribe",
        "currentYear": 2024
      }
    }
  ]
}
```

#### 4. Preview Template

```http
GET /api/v1/mail/templates/newsletter/preview
Content-Type: application/json

{
  "title": "Custom Newsletter Title",
  "content": "Custom content here",
  "articles": [
    {
      "title": "Custom Article",
      "summary": "Custom article summary"
    }
  ]
}
```

Response:

```json
{
  "success": true,
  "data": {
    "templateName": "newsletter",
    "subject": "Custom Newsletter Title - Happy Ending Newsletter",
    "content": "<html>...rendered HTML content...</html>",
    "data": {
      "title": "Custom Newsletter Title",
      "content": "Custom content here",
      "articles": [...]
    }
  }
}
```

## Template Examples

### **1. Newsletter Template Usage**

#### **Send Newsletter Email**

```kotlin
@Service
class NewsletterService(
    private val mailService: MailService
) {
    
    fun sendWeeklyNewsletter(subscribers: List<String>) {
        val templateData = mapOf(
            "title" to "Weekly Newsletter",
            "content" to "Check out our latest updates and offers!",
            "articles" to listOf(
                mapOf(
                    "title" to "New Products",
                    "summary" to "Discover our latest arrivals",
                    "link" to "https://happyending.com/new-products"
                ),
                mapOf(
                    "title" to "Special Offers",
                    "summary" to "Don't miss our exclusive deals",
                    "link" to "https://happyending.com/offers"
                )
            ),
            "unsubscribeLink" to "https://happyending.com/unsubscribe",
            "currentYear" to 2024
        )
        
        val bulkTemplateEmailDTO = BulkTemplateEmailDTO(
            recipients = subscribers,
            subject = "Weekly Newsletter - Happy Ending",
            templateName = "newsletter",
            templateData = templateData
        )
        
        mailService.sendBulkTemplateEmail(bulkTemplateEmailDTO)
    }
}
```

### **2. Promotion Template Usage**

#### **Send Promotional Email**

```kotlin
@Service
class PromotionService(
    private val mailService: MailService
) {
    
    fun sendBlackFridayPromotion(customers: List<String>) {
        val templateData = mapOf(
            "promoTitle" to "Black Friday Sale",
            "discount" to "50% OFF",
            "validUntil" to "2024-01-31",
            "promoCode" to "BLACKFRIDAY50",
            "productImage" to "https://example.com/black-friday-banner.jpg",
            "currentYear" to 2024
        )
        
        val bulkTemplateEmailDTO = BulkTemplateEmailDTO(
            recipients = customers,
            subject = "Black Friday Sale - 50% OFF!",
            templateName = "promotion",
            templateData = templateData
        )
        
        mailService.sendBulkTemplateEmail(bulkTemplateEmailDTO)
    }
}
```

### **3. Custom Template Usage**

#### **Send Custom Template Email**

```kotlin
@Service
class CustomEmailService(
    private val mailService: MailService
) {
    
    fun sendCustomTemplateEmail(
        to: String,
        templateName: String,
        customData: Map<String, Any>
    ) {
        val templateEmailDTO = TemplateEmailDTO(
            to = to,
            subject = "Custom Email Subject",
            templateName = templateName,
            templateData = customData
        )
        
        mailService.sendTemplateEmail(templateEmailDTO)
    }
}
```

## Template Development

### **1. Creating New Templates**

#### **Template Structure**

```html
<!-- templates/custom-template.ftl -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Happy Ending</title>
    <style>
        /* CSS styling */
    </style>
</head>
<body>
    <div class="header">
        <h1>${title}</h1>
    </div>
    
    <div class="content">
        <h2>Hello ${userName}!</h2>
        <p>${message}</p>
        
        <#if actionUrl?has_content>
        <div style="text-align: center;">
            <a href="${actionUrl}" class="button">${actionText}</a>
        </div>
        </#if>
    </div>
    
    <div class="footer">
        <p>© ${currentYear} Happy Ending. All rights reserved.</p>
    </div>
</body>
</html>
```

#### **Register Template in Service**

```kotlin
// Add to MailService.getAvailableTemplates()
TemplateInfoDTO(
    templateName = "custom-template",
    subject = "{{title}} - Happy Ending",
    description = "Custom template for specific use case",
    requiredVariables = listOf("title", "userName", "message"),
    optionalVariables = listOf("actionUrl", "actionText", "currentYear"),
    exampleData = mapOf(
        "title" to "Custom Title",
        "userName" to "John Doe",
        "message" to "Custom message here",
        "actionUrl" to "https://app.com/action",
        "actionText" to "Take Action",
        "currentYear" to 2024
    )
)
```

### **2. Template Variables**

#### **FreeMarker Syntax**

```html
<!-- Basic variables -->
<h1>${title}</h1>
<p>Hello ${userName}!</p>

<!-- Conditional content -->
<#if condition>
    <p>This content shows when condition is true</p>
</#if>

<!-- Lists -->
<#list items as item>
    <div>${item.name}</div>
</#list>

<!-- Default values -->
<p>${optionalVariable!"Default Value"}</p>
```

#### **Common Variables**

- **`currentYear`** - Current year (auto-added)
- **`userName`** - User's name
- **`title`** - Email title
- **`message`** - Main message content
- **`actionUrl`** - Call-to-action URL
- **`actionText`** - Call-to-action text

## Best Practices

### **1. Template Design**

- **Responsive design** cho mobile devices
- **Professional styling** với consistent branding
- **Clear call-to-action** buttons
- **Fallback content** cho missing variables

### **2. Variable Management**

- **Required variables** validation
- **Default values** cho optional variables
- **Type checking** cho complex data
- **Error handling** cho missing templates

### **3. Performance**

- **Template caching** trong FreeMarker
- **Async processing** cho bulk emails
- **Rate limiting** để tránh spam
- **Template validation** trước khi gửi

### **4. Testing**

- **Template preview** functionality
- **Sample data** testing
- **Email client** compatibility
- **Mobile responsiveness** testing

## Troubleshooting

### **Common Issues**

#### **1. Template Not Found**

```bash
# Check template exists
ls src/main/resources/templates/

# Verify template name
curl -X GET "http://localhost:8085/api/v1/mail/templates"
```

#### **2. Variable Errors**

```bash
# Preview template with data
curl -X GET "http://localhost:8085/api/v1/mail/templates/newsletter/preview" \
  -H "Content-Type: application/json" \
  -d '{"title": "Test", "content": "Test content"}'
```

#### **3. Template Rendering Issues**

```bash
# Check FreeMarker logs
tail -f logs/application.log | grep "FreeMarker"

# Test template processing
curl -X POST "http://localhost:8085/api/v1/mail/send/template" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "test@example.com",
    "subject": "Test",
    "templateName": "newsletter",
    "templateData": {"title": "Test"}
  }'
```

### **Debug Mode**

```properties
# Enable FreeMarker debug
logging.level.freemarker=DEBUG

# Enable mail debug
spring.mail.properties.mail.debug=true
```

## Advanced Features

### **1. Dynamic Subject Lines**

```kotlin
val subject = "Welcome ${userName} - Happy Ending!"
// Template sẽ render: "Welcome John Doe - Happy Ending!"
```

### **2. Conditional Content**

```html
<#if userType == "premium">
    <div class="premium-content">Premium user benefits</div>
<#else>
    <div class="standard-content">Standard user benefits</div>
</#if>
```

### **3. Loop Processing**

```html
<#list orderItems as item>
    <tr>
        <td>${item.name}</td>
        <td>${item.quantity}</td>
        <td>$${item.price}</td>
    </tr>
</#list>
```

Bây giờ bạn có một **Template Email Service** hoàn chỉnh với khả năng gửi email sử dụng templates có sẵn và tùy chỉnh! 📧🎨

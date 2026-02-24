# Mail Service Documentation

## Tổng quan

Mail Service được thiết kế để gửi email với các tính năng:

- **Template-based emails** với FreeMarker
- **Bulk email** support
- **Email tracking** và history
- **Attachment support**
- **Retry mechanism** cho failed emails
- **MongoDB storage** cho email history

## Kiến trúc

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controllers   │    │    Services      │    │   Repositories  │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ MailController  │    │ MailService     │    │ EmailRepository │
│                 │    │ EmailTemplate   │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   REST APIs     │    │   JavaMailSender│    │    MongoDB      │
│   /send         │    │   SMTP Server   │    │   Email History │
│   /bulk         │    │   Templates     │    │   Status Track  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Tính năng chính

### 1. **Email Types**

- **Welcome emails** - Chào mừng user mới
- **Password reset** - Reset password
- **Order confirmation** - Xác nhận đơn hàng
- **Notifications** - Thông báo chung
- **Custom emails** - Email tùy chỉnh

### 2. **Template System**

- **FreeMarker templates** với HTML/CSS
- **Dynamic content** với variables
- **Responsive design** cho mobile
- **Professional styling**

### 3. **Email Management**

- **Bulk sending** cho nhiều recipients
- **Attachment support** cho files
- **Email history** tracking
- **Retry mechanism** cho failed emails

## API Endpoints

### **Basic Email Operations**

#### 1. Send Single Email

```http
POST /api/v1/mail/send
Content-Type: application/json

{
  "to": "user@example.com",
  "cc": ["manager@example.com"],
  "bcc": ["admin@example.com"],
  "subject": "Test Email",
  "content": "<h1>Hello World!</h1>",
  "isHtml": true,
  "attachments": []
}
```

#### 2. Send Bulk Email

```http
POST /api/v1/mail/send/bulk
Content-Type: application/json

{
  "recipients": ["user1@example.com", "user2@example.com"],
  "subject": "Bulk Notification",
  "content": "<h1>Important Update</h1>",
  "isHtml": true
}
```

### **Template-based Emails**

#### 3. Welcome Email

```http
POST /api/v1/mail/send/welcome
Content-Type: application/x-www-form-urlencoded

to=user@example.com&userName=John Doe&activationLink=https://app.com/activate?token=abc123
```

#### 4. Password Reset Email

```http
POST /api/v1/mail/send/password-reset
Content-Type: application/x-www-form-urlencoded

to=user@example.com&userName=John Doe&resetLink=https://app.com/reset?token=xyz789
```

#### 5. Order Confirmation Email

```http
POST /api/v1/mail/send/order-confirmation
Content-Type: application/x-www-form-urlencoded

to=customer@example.com&userName=John Doe&orderNumber=ORD-123&orderDate=2024-01-15&totalAmount=99.99
```

#### 6. Notification Email

```http
POST /api/v1/mail/send/notification
Content-Type: application/x-www-form-urlencoded

to=user@example.com&title=System Maintenance&message=We will perform maintenance tonight&actionUrl=https://app.com/status&actionText=Check Status
```

### **Email Management**

#### 7. Send Email with Attachment

```http
POST /api/v1/mail/send/with-attachment
Content-Type: multipart/form-data

to=user@example.com&subject=Document&content=Please find attached&attachments=file1.pdf,file2.jpg
```

#### 8. Get Email History

```http
GET /api/v1/mail/history/user@example.com?days=30
```

#### 9. Get Pending Emails

```http
GET /api/v1/mail/pending
```

#### 10. Get Failed Emails

```http
GET /api/v1/mail/failed?hours=24
```

#### 11. Retry Failed Emails

```http
POST /api/v1/mail/retry
```

## Email Templates

### **1. Welcome Template**

```html
<!-- templates/welcome.ftl -->
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to Happy Ending!</title>
    <style>/* Professional CSS styling */</style>
</head>
<body>
    <div class="header">
        <h1>🎉 Welcome to Happy Ending!</h1>
    </div>
    <div class="content">
        <h2>Hello ${userName}!</h2>
        <p>Thank you for joining Happy Ending!</p>
        <a href="${activationLink}" class="button">Activate Account</a>
    </div>
</body>
</html>
```

### **2. Password Reset Template**

```html
<!-- templates/password-reset.ftl -->
<!DOCTYPE html>
<html>
<head>
    <title>Password Reset - Happy Ending</title>
</head>
<body>
    <div class="header">
        <h1>🔐 Password Reset Request</h1>
    </div>
    <div class="content">
        <h2>Hello ${userName}!</h2>
        <p>We received a request to reset your password.</p>
        <a href="${resetLink}" class="button">Reset Password</a>
    </div>
</body>
</html>
```

### **3. Order Confirmation Template**

```html
<!-- templates/order-confirmation.ftl -->
<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmation - Happy Ending</title>
</head>
<body>
    <div class="header">
        <h1>🎉 Order Confirmation</h1>
    </div>
    <div class="content">
        <h2>Hello ${userName}!</h2>
        <p>Thank you for your order!</p>
        <p><strong>Order Number:</strong> #${orderNumber}</p>
        <p><strong>Total Amount:</strong> $${totalAmount}</p>
    </div>
</body>
</html>
```

## Cấu hình

### **1. Application Properties**

```properties
# Mail Configuration
spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_PORT:587}
spring.mail.username=${MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=${MAIL_SMTP_AUTH:true}
spring.mail.properties.mail.smtp.starttls.enable=${MAIL_SMTP_STARTTLS:true}
spring.mail.properties.mail.smtp.ssl.trust=${MAIL_SMTP_SSL_TRUST:smtp.gmail.com}
spring.mail.properties.mail.smtp.ssl.protocols=${MAIL_SMTP_SSL_PROTOCOLS:TLSv1.2}
spring.mail.properties.mail.debug=${MAIL_DEBUG:false}
```

### **2. Environment Variables**

```bash
# Gmail Configuration
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password
export MAIL_SMTP_AUTH=true
export MAIL_SMTP_STARTTLS=true
export MAIL_SMTP_SSL_TRUST=smtp.gmail.com
export MAIL_DEBUG=false
```

### **3. Gmail Setup**

1. **Enable 2-Factor Authentication**
2. **Generate App Password**:
   - Go to Google Account Settings
   - Security → 2-Step Verification
   - App passwords → Generate password
3. **Use App Password** trong `MAIL_PASSWORD`

## Cách sử dụng

### **1. Trong Service khác**

```kotlin
@Service
class UserService(
    private val mailService: MailService
) {
    
    fun createUser(user: User): User {
        // Create user logic
        val savedUser = userRepository.save(user)
        
        // Send welcome email
        mailService.sendWelcomeEmail(
            to = user.email,
            userName = user.name,
            activationLink = "https://app.com/activate?token=${user.activationToken}"
        )
        
        return savedUser
    }
    
    fun resetPassword(email: String): Boolean {
        // Generate reset token
        val resetToken = generateResetToken()
        
        // Send password reset email
        mailService.sendPasswordResetEmail(
            to = email,
            userName = "User",
            resetLink = "https://app.com/reset?token=$resetToken"
        )
        
        return true
    }
}
```

### **2. Order Service Integration**

```kotlin
@Service
class OrderService(
    private val mailService: MailService
) {
    
    fun createOrder(order: Order): Order {
        val savedOrder = orderRepository.save(order)
        
        // Send order confirmation email
        mailService.sendOrderConfirmationEmail(
            to = order.customerEmail,
            userName = order.customerName,
            orderNumber = order.orderNumber,
            orderDate = order.createdAt.toString(),
            totalAmount = order.totalAmount.toString(),
            items = order.items.map { item ->
                mapOf(
                    "name" to item.productName,
                    "quantity" to item.quantity,
                    "price" to item.price,
                    "total" to (item.price * item.quantity)
                )
            }
        )
        
        return savedOrder
    }
}
```

### **3. Notification Service**

```kotlin
@Service
class NotificationService(
    private val mailService: MailService
) {
    
    fun sendSystemNotification(
        users: List<User>,
        title: String,
        message: String,
        actionUrl: String? = null
    ) {
        users.forEach { user ->
            mailService.sendNotificationEmail(
                to = user.email,
                title = title,
                message = message,
                actionUrl = actionUrl,
                actionText = "View Details"
            )
        }
    }
}
```

## Email Status Tracking

### **Status Types:**

- **PENDING** - Chờ gửi
- **SENT** - Đã gửi thành công
- **FAILED** - Gửi thất bại
- **DELIVERED** - Đã giao đến inbox
- **BOUNCED** - Email bị trả về

### **MongoDB Storage:**

```json
{
  "_id": "ObjectId",
  "to": "user@example.com",
  "subject": "Welcome to Happy Ending!",
  "content": "<html>...</html>",
  "status": "SENT",
  "sentAt": "2024-01-15T10:30:00",
  "createdAt": "2024-01-15T10:29:45",
  "templateName": "welcome",
  "attachments": []
}
```

## Best Practices

### **1. Email Security**

- Sử dụng **App Passwords** thay vì regular passwords
- Enable **2-Factor Authentication**
- Sử dụng **TLS/SSL** encryption
- Validate **email addresses** trước khi gửi

### **2. Performance**

- Sử dụng **async processing** cho bulk emails
- Implement **rate limiting** để tránh spam
- **Queue system** cho high-volume emails
- **Retry mechanism** cho failed emails

### **3. Template Design**

- **Responsive design** cho mobile
- **Professional styling** với CSS
- **Clear call-to-action** buttons
- **Fallback content** cho email clients

### **4. Monitoring**

- Track **email delivery rates**
- Monitor **bounce rates**
- Log **failed emails** for debugging
- **Retry mechanism** cho temporary failures

## Troubleshooting

### **Common Issues:**

1. **Authentication failed**: Check username/password
2. **Connection timeout**: Check SMTP settings
3. **Template not found**: Check template path
4. **Attachment too large**: Check file size limits

### **Debug Mode:**

```properties
spring.mail.properties.mail.debug=true
```

### **Logs:**

```bash
# Check mail service logs
tail -f logs/application.log | grep "MailService"

# Check failed emails
curl -X GET "http://localhost:8085/api/v1/mail/failed"

# Retry failed emails
curl -X POST "http://localhost:8085/api/v1/mail/retry"
```

Bây giờ bạn có một **Mail Service** hoàn chỉnh với template system, bulk email support, và email tracking! 📧🚀

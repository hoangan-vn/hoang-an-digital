<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Happy Ending</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            background: linear-gradient(135deg, #6c5ce7 0%, #a29bfe 100%);
            color: white;
            padding: 30px;
            text-align: center;
            border-radius: 10px 10px 0 0;
        }
        .content {
            background: #f9f9f9;
            padding: 30px;
            border-radius: 0 0 10px 10px;
        }
        .button {
            display: inline-block;
            background: #6c5ce7;
            color: white;
            padding: 12px 30px;
            text-decoration: none;
            border-radius: 5px;
            margin: 20px 0;
        }
        .message-box {
            background: white;
            padding: 20px;
            border-radius: 5px;
            margin: 20px 0;
            border-left: 4px solid #6c5ce7;
        }
        .footer {
            text-align: center;
            margin-top: 30px;
            color: #666;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>🔔 ${title}</h1>
        <p>Important notification from Happy Ending</p>
    </div>
    
    <div class="content">
        <div class="message-box">
            <p>${message}</p>
        </div>
        
        <#if actionUrl?has_content && actionText?has_content>
        <div style="text-align: center;">
            <a href="${actionUrl}" class="button">${actionText}</a>
        </div>
        </#if>
        
        <h3>💡 Stay Connected</h3>
        <ul>
            <li>📱 Follow us on social media for updates</li>
            <li>📧 Check your email for important notifications</li>
            <li>🌐 Visit our website for the latest news</li>
            <li>💬 Contact us if you need assistance</li>
        </ul>
        
        <h3>📞 Need Help?</h3>
        <p>If you have any questions, our support team is here to help:</p>
        <ul>
            <li>📧 Email: support@happyending.com</li>
            <li>📞 Phone: 1-800-HAPPY-END</li>
            <li>💬 Live Chat: Available 24/7</li>
        </ul>
        
        <p>Best regards,<br>The Happy Ending Team</p>
    </div>
    
    <div class="footer">
        <p>© ${currentYear} Happy Ending. All rights reserved.</p>
        <p>This notification was sent to keep you informed about your account.</p>
    </div>
</body>
</html>

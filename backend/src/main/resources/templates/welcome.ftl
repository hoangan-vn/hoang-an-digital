<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to Happy Ending!</title>
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
            background: #667eea;
            color: white;
            padding: 12px 30px;
            text-decoration: none;
            border-radius: 5px;
            margin: 20px 0;
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
        <h1>🎉 Welcome to Happy Ending!</h1>
        <p>Your account has been created successfully</p>
    </div>
    
    <div class="content">
        <h2>Hello ${userName}!</h2>
        
        <p>Thank you for joining Happy Ending! We're excited to have you on board.</p>
        
        <p>To get started, please activate your account by clicking the button below:</p>
        
        <div style="text-align: center;">
            <a href="${activationLink}" class="button">Activate Account</a>
        </div>
        
        <p>If the button doesn't work, you can copy and paste this link into your browser:</p>
        <p style="word-break: break-all; background: #eee; padding: 10px; border-radius: 5px;">
            ${activationLink}
        </p>
        
        <h3>What's next?</h3>
        <ul>
            <li>✅ Activate your account</li>
            <li>🛍️ Browse our amazing products</li>
            <li>💝 Create your wishlist</li>
            <li>🎁 Enjoy exclusive member benefits</li>
        </ul>
        
        <p>If you have any questions, feel free to contact our support team.</p>
        
        <p>Best regards,<br>The Happy Ending Team</p>
    </div>
    
    <div class="footer">
        <p>© ${currentYear} Happy Ending. All rights reserved.</p>
        <p>This email was sent to you because you created an account with us.</p>
    </div>
</body>
</html>

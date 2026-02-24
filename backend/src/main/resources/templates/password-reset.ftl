<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Reset - Happy Ending</title>
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
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
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
            background: #ff6b6b;
            color: white;
            padding: 12px 30px;
            text-decoration: none;
            border-radius: 5px;
            margin: 20px 0;
        }
        .warning {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            padding: 15px;
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
        <h1>🔐 Password Reset Request</h1>
        <p>Reset your Happy Ending account password</p>
    </div>
    
    <div class="content">
        <h2>Hello ${userName}!</h2>
        
        <p>We received a request to reset your password for your Happy Ending account.</p>
        
        <p>To reset your password, please click the button below:</p>
        
        <div style="text-align: center;">
            <a href="${resetLink}" class="button">Reset Password</a>
        </div>
        
        <p>If the button doesn't work, you can copy and paste this link into your browser:</p>
        <p style="word-break: break-all; background: #eee; padding: 10px; border-radius: 5px;">
            ${resetLink}
        </p>
        
        <div class="warning">
            <h3>⚠️ Important Security Information</h3>
            <ul>
                <li>This link will expire in 24 hours</li>
                <li>If you didn't request this password reset, please ignore this email</li>
                <li>Your password will not be changed until you click the link above</li>
                <li>For security reasons, this link can only be used once</li>
            </ul>
        </div>
        
        <h3>Security Tips:</h3>
        <ul>
            <li>🔒 Use a strong, unique password</li>
            <li>🔄 Don't reuse passwords from other accounts</li>
            <li>📱 Enable two-factor authentication if available</li>
            <li>🚫 Never share your password with anyone</li>
        </ul>
        
        <p>If you have any questions or need assistance, please contact our support team.</p>
        
        <p>Best regards,<br>The Happy Ending Team</p>
    </div>
    
    <div class="footer">
        <p>© ${currentYear} Happy Ending. All rights reserved.</p>
        <p>This email was sent because a password reset was requested for your account.</p>
    </div>
</body>
</html>

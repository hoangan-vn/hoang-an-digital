<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Happy Ending Newsletter</title>
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
        .article {
            background: white;
            padding: 20px;
            margin: 20px 0;
            border-radius: 5px;
            border-left: 4px solid #667eea;
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
        .unsubscribe {
            text-align: center;
            margin-top: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>📰 ${title}</h1>
        <p>Happy Ending Newsletter</p>
    </div>
    
    <div class="content">
        <h2>Hello Subscriber!</h2>
        
        <p>${content}</p>
        
        <#if articles?has_content>
        <h3>📖 This Week's Articles</h3>
        
        <#list articles as article>
        <div class="article">
            <h4>${article.title}</h4>
            <p>${article.summary}</p>
            <#if article.link?has_content>
            <a href="${article.link}" class="button">Read More</a>
            </#if>
        </div>
        </#list>
        </#if>
        
        <h3>🎉 What's New This Week</h3>
        <ul>
            <li>🛍️ New products added to our catalog</li>
            <li>💰 Exclusive member discounts</li>
            <li>🚚 Free shipping on orders over $50</li>
            <li>⭐ Customer reviews and testimonials</li>
        </ul>
        
        <h3>💡 Pro Tips</h3>
        <ul>
            <li>📱 Follow us on social media for daily updates</li>
            <li>🔔 Enable notifications for special offers</li>
            <li>💝 Create wishlists for easy shopping</li>
            <li>📧 Check your email for personalized recommendations</li>
        </ul>
        
        <div style="text-align: center; margin: 30px 0;">
            <a href="https://happyending.com/shop" class="button">Shop Now</a>
        </div>
        
        <p>Thank you for being a valued member of the Happy Ending community!</p>
        
        <p>Best regards,<br>The Happy Ending Team</p>
    </div>
    
    <#if unsubscribeLink?has_content>
    <div class="unsubscribe">
        <p>Don't want to receive these emails?</p>
        <a href="${unsubscribeLink}">Unsubscribe</a>
    </div>
    </#if>
    
    <div class="footer">
        <p>© ${currentYear} Happy Ending. All rights reserved.</p>
        <p>This newsletter was sent to you because you subscribed to our updates.</p>
    </div>
</body>
</html>

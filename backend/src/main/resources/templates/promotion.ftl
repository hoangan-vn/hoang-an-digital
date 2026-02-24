<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${promoTitle} - Happy Ending</title>
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
        .promo-banner {
            background: linear-gradient(45deg, #ff6b6b, #ee5a24);
            color: white;
            padding: 30px;
            text-align: center;
            border-radius: 10px;
            margin: 20px 0;
        }
        .discount {
            font-size: 48px;
            font-weight: bold;
            margin: 10px 0;
        }
        .promo-code {
            background: white;
            color: #ff6b6b;
            padding: 10px 20px;
            border-radius: 25px;
            font-weight: bold;
            font-size: 18px;
            display: inline-block;
            margin: 10px 0;
        }
        .button {
            display: inline-block;
            background: #ff6b6b;
            color: white;
            padding: 15px 40px;
            text-decoration: none;
            border-radius: 25px;
            margin: 20px 0;
            font-size: 18px;
            font-weight: bold;
        }
        .product-image {
            max-width: 100%;
            height: auto;
            border-radius: 10px;
            margin: 20px 0;
        }
        .urgency {
            background: #fff3cd;
            border: 2px solid #ffeaa7;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
            text-align: center;
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
        <h1>🎉 ${promoTitle}</h1>
        <p>Limited Time Offer - Don't Miss Out!</p>
    </div>
    
    <div class="content">
        <div class="promo-banner">
            <h2>🔥 Special Promotion</h2>
            <div class="discount">${discount}</div>
            <p>Use code: <span class="promo-code">${promoCode}</span></p>
            <p>Valid until: ${validUntil}</p>
        </div>
        
        <#if productImage?has_content>
        <div style="text-align: center;">
            <img src="${productImage}" alt="Promotional Product" class="product-image">
        </div>
        </#if>
        
        <h3>🎯 Why Choose Happy Ending?</h3>
        <ul>
            <li>✅ High-quality products</li>
            <li>🚚 Fast and reliable shipping</li>
            <li>💯 100% satisfaction guarantee</li>
            <li>🛡️ Secure payment processing</li>
            <li>📞 24/7 customer support</li>
        </ul>
        
        <div class="urgency">
            <h3>⏰ Limited Time Offer!</h3>
            <p>This promotion expires on <strong>${validUntil}</strong></p>
            <p>Don't wait - grab this amazing deal now!</p>
        </div>
        
        <div style="text-align: center;">
            <a href="https://happyending.com/shop?promo=${promoCode}" class="button">Shop Now & Save!</a>
        </div>
        
        <h3>💡 How to Use Your Discount</h3>
        <ol>
            <li>Browse our amazing products</li>
            <li>Add items to your cart</li>
            <li>Enter promo code: <strong>${promoCode}</strong></li>
            <li>Enjoy your savings!</li>
        </ol>
        
        <h3>📱 Stay Connected</h3>
        <p>Follow us on social media for more exclusive offers:</p>
        <ul>
            <li>📘 Facebook: @HappyEnding</li>
            <li>📷 Instagram: @happyending_official</li>
            <li>🐦 Twitter: @HappyEnding</li>
        </ul>
        
        <p>Questions? Contact our support team at support@happyending.com</p>
        
        <p>Happy Shopping!<br>The Happy Ending Team</p>
    </div>
    
    <div class="footer">
        <p>© ${currentYear} Happy Ending. All rights reserved.</p>
        <p>This promotional email was sent to you because you're a valued customer.</p>
        <p><a href="https://happyending.com/unsubscribe">Unsubscribe</a> | <a href="https://happyending.com/preferences">Email Preferences</a></p>
    </div>
</body>
</html>

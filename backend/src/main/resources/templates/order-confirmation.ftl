<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation - Happy Ending</title>
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
            background: linear-gradient(135deg, #00b894 0%, #00a085 100%);
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
        .order-info {
            background: white;
            padding: 20px;
            border-radius: 5px;
            margin: 20px 0;
            border-left: 4px solid #00b894;
        }
        .items-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        .items-table th,
        .items-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .items-table th {
            background: #f8f9fa;
            font-weight: bold;
        }
        .total {
            background: #00b894;
            color: white;
            padding: 15px;
            border-radius: 5px;
            text-align: center;
            font-size: 18px;
            font-weight: bold;
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
        <h1>🎉 Order Confirmation</h1>
        <p>Thank you for your order!</p>
    </div>
    
    <div class="content">
        <h2>Hello ${userName}!</h2>
        
        <p>Thank you for your order! We're excited to prepare your items for delivery.</p>
        
        <div class="order-info">
            <h3>📋 Order Details</h3>
            <p><strong>Order Number:</strong> #${orderNumber}</p>
            <p><strong>Order Date:</strong> ${orderDate}</p>
            <p><strong>Status:</strong> <span style="color: #00b894; font-weight: bold;">Confirmed</span></p>
        </div>
        
        <h3>🛍️ Order Items</h3>
        <table class="items-table">
            <thead>
                <tr>
                    <th>Item</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <#list items as item>
                <tr>
                    <td>${item.name}</td>
                    <td>${item.quantity}</td>
                    <td>$${item.price}</td>
                    <td>$${item.total}</td>
                </tr>
                </#list>
            </tbody>
        </table>
        
        <div class="total">
            Total Amount: $${totalAmount}
        </div>
        
        <h3>📦 What's Next?</h3>
        <ul>
            <li>✅ Your order has been confirmed</li>
            <li>📋 We're preparing your items</li>
            <li>🚚 You'll receive a shipping notification soon</li>
            <li>📱 Track your order in your account</li>
        </ul>
        
        <h3>📞 Need Help?</h3>
        <p>If you have any questions about your order, please contact our customer service team:</p>
        <ul>
            <li>📧 Email: support@happyending.com</li>
            <li>📞 Phone: 1-800-HAPPY-END</li>
            <li>💬 Live Chat: Available 24/7</li>
        </ul>
        
        <p>Thank you for choosing Happy Ending!</p>
        
        <p>Best regards,<br>The Happy Ending Team</p>
    </div>
    
    <div class="footer">
        <p>© ${currentYear} Happy Ending. All rights reserved.</p>
        <p>This email confirms your recent order with Happy Ending.</p>
    </div>
</body>
</html>

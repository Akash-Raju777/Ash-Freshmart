package com.supermarket.management.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class DemoDataSeederService {
    private final JdbcTemplate jdbcTemplate;

    public DemoDataSeederService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void seedBusiness(String businessId) {
        if (businessId == null || businessId.trim().isEmpty() || businessId.equalsIgnoreCase("null")) {
            return;
        }
        
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM products WHERE business_id = ?",
                Integer.class,
                businessId
        );
        
        if (count != null && count < 5) {
            System.out.println("DemoDataSeederService: Auto-seeding catalog for business: '" + businessId + "'");
            
            // Clear any partial records to avoid conflicts
            jdbcTemplate.update("DELETE FROM products WHERE business_id = ?", businessId);
            jdbcTemplate.update("DELETE FROM customer_profiles WHERE business_id = ?", businessId);
            jdbcTemplate.update("DELETE FROM offers WHERE business_id = ?", businessId);
            jdbcTemplate.update("DELETE FROM sales WHERE business_id = ?", businessId);

            String today = LocalDate.now().toString();
            String mfg = LocalDate.now().minusDays(10).toString();
            String arrival = LocalDate.now().minusDays(5).toString();
            String expNear = LocalDate.now().plusDays(10).toString();
            String expFar = LocalDate.now().plusMonths(6).toString();
            String expPassed = LocalDate.now().minusDays(2).toString();

            Object[][] demoProducts = new Object[][] {
                {"P101", "Organic Whole Milk", "Lactaid", "https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 45, 4.49},
                {"P102", "Greek Yogurt Strawberry", "Chobani", "https://images.unsplash.com/photo-1488477181946-6428a0291777?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 30, 1.89},
                {"P103", "Cheddar Cheese Block", "Cabot", "https://images.unsplash.com/photo-1486299267070-83823f5448dd?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 20, 5.99},
                {"P104", "Unsalted Butter", "Kerrygold", "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 15, 3.99},
                {"P105", "Sour Cream Light", "Daisy", "https://images.unsplash.com/photo-1528698827591-e19ccd7bc23d?auto=format&fit=crop&q=80&w=300", mfg, expPassed, arrival, 8, 2.49},
                {"P106", "Coca-Cola 12-Pack", "Coca-Cola", "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 50, 7.99},
                {"P107", "Orange Juice 100%", "Tropicana", "https://images.unsplash.com/photo-1613478223719-2ab802602423?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 3, 4.99},
                {"P108", "Ground Coffee Roast", "Starbucks", "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 25, 9.99},
                {"P109", "Green Tea Bags", "Lipton", "https://images.unsplash.com/photo-1597481499750-3e6b22637e12?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 40, 3.49},
                {"P110", "Spring Water 24-Pack", "Poland Spring", "https://images.unsplash.com/photo-1608885898957-a599fb1b4641?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 60, 5.49},
                {"P111", "Potato Chips Classic", "Lay's", "https://images.unsplash.com/photo-1566478989037-eec170784d0b?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 35, 3.99},
                {"P112", "Chocolate Chip Cookies", "Chips Ahoy", "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 28, 3.29},
                {"P113", "Tortilla Chips", "Tostitos", "https://images.unsplash.com/photo-1518047601542-79f18c655718?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 30, 4.29},
                {"P114", "Roasted Almonds", "Blue Diamond", "https://images.unsplash.com/photo-1508061253366-f7da158b6d46?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 0, 6.99},
                {"P115", "Microwave Popcorn", "Orville", "https://images.unsplash.com/photo-1578849278619-e73505e9610f?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 20, 2.99},
                {"P116", "Whole Wheat Bread", "Nature's Own", "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 5, 3.29},
                {"P117", "Butter Croissants", "Local Bakery", "https://images.unsplash.com/photo-1555507036-ab1f4038808a?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 12, 4.49},
                {"P118", "Blueberry Muffins", "Local Bakery", "https://images.unsplash.com/photo-1607958996333-41aef7caefaa?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 10, 3.99},
                {"P119", "Bagels Plain 6-Pack", "Thomas", "https://images.unsplash.com/photo-1585478259715-876acc5be8eb?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 8, 3.49},
                {"P120", "Chocolate Donuts 6-Pack", "Entenmann's", "https://images.unsplash.com/photo-1551024601-bec78aea704b?auto=format&fit=crop&q=80&w=300", mfg, expPassed, arrival, 2, 4.99},
                {"P121", "Red Apples 3lb Bag", "Washington", "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 24, 4.99},
                {"P122", "Bananas Bunch", "Dole", "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 35, 1.99},
                {"P123", "Strawberries Clamshell", "Driscoll's", "https://images.unsplash.com/photo-1464965911861-746a04b4bca6?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 4, 3.99},
                {"P124", "Blueberries Pint", "Driscoll's", "https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 18, 4.49},
                {"P125", "Avocados 4-Pack", "Hass", "https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 15, 5.49},
                {"P126", "Roma Tomatoes 1lb", "Greenhouse", "https://images.unsplash.com/photo-1595855759920-86582396756a?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 40, 2.49},
                {"P127", "Iceberg Lettuce", "Fresh Farms", "https://images.unsplash.com/photo-1622484211148-716598e0e640?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 14, 1.89},
                {"P128", "Fresh Carrots 2lb Bag", "Grimmway", "https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?auto=format&fit=crop&q=80&w=300", mfg, expNear, arrival, 25, 1.99},
                {"P129", "Yellow Onions 3lb", "Fresh Farms", "https://images.unsplash.com/photo-1508747702841-008cbda7779e?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 20, 2.99},
                {"P130", "Russet Potatoes 5lb Bag", "Idaho", "https://images.unsplash.com/photo-1518977676601-b53f82aba655?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 18, 4.99},
                {"P131", "Dish Soap 24oz", "Dawn", "https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 30, 3.49},
                {"P132", "Laundry Detergent Pods", "Tide", "https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 15, 14.99},
                {"P133", "All-Purpose Cleaner Spray", "Lysol", "https://images.unsplash.com/photo-1584820927498-cfe5211fd9bf?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 22, 4.29},
                {"P134", "Paper Towels 6-Rolls", "Bounty", "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 20, 8.99},
                {"P135", "Trash Bags 13 Gallon", "Glad", "https://images.unsplash.com/photo-1610312278520-dbc8935952d9?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 0, 9.99},
                {"P136", "Frozen Cheese Pizza", "DiGiorno", "https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 16, 6.99},
                {"P137", "Vanilla Ice Cream Pint", "Haagen-Dazs", "https://images.unsplash.com/photo-1563805042-7684c019e1cb?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 25, 5.49},
                {"P138", "Frozen Mixed Vegetables", "Birds Eye", "https://images.unsplash.com/photo-1574316071802-0d684efa7bf5?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 35, 2.49},
                {"P139", "Chicken Nuggets Frozen", "Tyson", "https://images.unsplash.com/photo-1562967914-608f82629710?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 18, 7.99},
                {"P140", "Frozen Waffles", "Eggo", "https://images.unsplash.com/photo-1587314168485-3236d6710814?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 12, 3.29},
                {"P141", "Olive Oil Extra Virgin", "Filippo Berio", "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 18, 12.99},
                {"P142", "Spaghetti Pasta 1lb", "Barilla", "https://images.unsplash.com/photo-1551462147-ff29053bfc14?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 40, 1.49},
                {"P143", "Marinara Pasta Sauce", "Rao's", "https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 24, 7.49},
                {"P144", "Canned Tuna in Water", "Bumble Bee", "https://images.unsplash.com/photo-1534482421-64566f976cfa?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 50, 1.79},
                {"P145", "Peanut Butter Creamy", "Jif", "https://images.unsplash.com/photo-1590080875515-8a3a8dc5735e?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 22, 3.99},
                {"P146", "Strawberry Jam", "Smucker's", "https://images.unsplash.com/photo-1584820927498-cfe5211fd9bf?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 15, 3.29},
                {"P147", "Organic Honey", "Nate's", "https://images.unsplash.com/photo-1587049352846-4a222e784d38?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 18, 6.49},
                {"P148", "Quick Oats Rolled", "Quaker", "https://images.unsplash.com/photo-1586444248902-2f64eddc13df?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 20, 4.99},
                {"P149", "Breakfast Cereal Honey Nut", "Cheerios", "https://images.unsplash.com/photo-1586444248902-2f64eddc13df?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 15, 4.49},
                {"P150", "Table Salt Iodized", "Morton", "https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?auto=format&fit=crop&q=80&w=300", mfg, expFar, arrival, 30, 1.29}
            };

            for (Object[] p : demoProducts) {
                jdbcTemplate.update(
                        "INSERT INTO products (id, name, brand, photoUrl, mfgDate, expDate, arrivingDate, quantity, price, business_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8], businessId
                );
            }
            
            jdbcTemplate.update("INSERT INTO customer_profiles (mobile, name, points, business_id) VALUES (?, ?, ?, ?)", "1234567890", "John Doe", 150, businessId);
            jdbcTemplate.update("INSERT INTO customer_profiles (mobile, name, points, business_id) VALUES (?, ?, ?, ?)", "9876543210", "Jane Smith", 320, businessId);
            jdbcTemplate.update("INSERT INTO customer_profiles (mobile, name, points, business_id) VALUES (?, ?, ?, ?)", "5556667777", "Bob Johnson", 45, businessId);

            jdbcTemplate.update("INSERT INTO offers (offerId, productId, offerType, discount, active, startDate, endDate, business_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", "O1_" + Math.abs(businessId.hashCode()), "P101", "BOGO", 0.0, true, today, expNear, businessId);
            jdbcTemplate.update("INSERT INTO offers (offerId, productId, offerType, discount, active, startDate, endDate, business_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", "O2_" + Math.abs(businessId.hashCode()), "P136", "PERCENTAGE", 20.0, true, today, expNear, businessId);
            jdbcTemplate.update("INSERT INTO offers (offerId, productId, offerType, discount, active, startDate, endDate, business_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", "O3_" + Math.abs(businessId.hashCode()), "P112", "B2G1", 0.0, true, today, expNear, businessId);

            for (int i = 1; i <= 20; i++) {
                String billId = "B" + (System.currentTimeMillis() - i * 3600 * 1000);
                String pId = "P1" + (10 + (i % 10));
                String pName = "Product " + pId;
                double price = 4.99;
                int qty = 1 + (i % 3);
                double sub = price * qty;
                String date = LocalDate.now().minusDays(i % 6).toString();
                
                jdbcTemplate.update(
                        "INSERT INTO sales (billId, productId, quantitySold, saleDate, totalAmount, productName, price, originalLineTotal, discountApplied, customerMobile, customerName, pointsEarned, totalPoints, subtotal, discount, tax, finalAmount, business_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        billId, pId, qty, date, sub, pName, price, sub, 0.0, "1234567890", "John Doe", (int)sub, 100, sub, 0.0, sub * 0.05, sub * 1.05, businessId
                );
            }
            System.out.println("DemoDataSeederService: Seeded all demo data successfully for business: " + businessId);
        }
    }
}

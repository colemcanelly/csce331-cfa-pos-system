--2 busiest months (most orders over a month)
SELECT to_char(date_trunc(‘month’,date),’YYYY-MM’) AS month, AVG(qty_sold) AS avg_qty_sold FROM Daily_Inventory	GROUP BY month ORDER BY avg_qty_sold DESC	LIMIT 2;

--Most popular item 
SELECT menu_item FROM Order_Items GROUP BY menu_item ORDER BY COUNT(*) DESC LIMIT 1;

--Most used kiosk
SELECT kiosk_id, COUNT(*) AS usage_count FROM Orders GROUP BY kiosk_id ORDER BY usage_count DESC LIMIT 1;

--Least popular item
SELECT menu_item FROM Order_Items GROUP BY menu_item ORDER BY COUNT(*) ASC LIMIT 1;
--2 slowest months
SELECT to_char(date_trunc('month', date), 'YYYY-MM') AS month, AVG(qty_sold) AS avg_qty_sold FROM Daily_Inventory GROUP BY month ORDER BY avg_qty_sold ASC LIMIT 2;

--Busiest day
SELECT date, SUM(qty_sold) AS total_qty_sold FROM Daily_Inventory GROUP BY date ORDER BY total_qty_sold DESC LIMIT 1;

--total revenue
SELECT SUM(food_price) AS total_revenue FROM Order_Items;

--Top 3 months that performed the best( most revenue)
SELECT to_char(order_date, 'YYYY-MM') AS order_month, SUM(order_total) AS revenue FROM Orders GROUP BY order_month ORDER BY revenue DESC LIMIT 3;

--most common customer name
SELECT customer_name FROM Order GROUP BY customer_name ORDER BY COUNT(*) DESC LIMIT 1;

--average order price
SELECT AVG(order_total) AS average_order_price FROM Order;

--average revenue per day of operation
SELECT SUM(order_total) / COUNT(DISTINCT DATE(order_date)) AS avg_revenue_per_day FROM Order GROUP BY DATE(order_date);

--most used ingredient
SELECT ingredient FROM Daily_Inventory GROUP BY ingredient ORDER BY COUNT(*) DESC LIMIT 1;

--most expensive order
SELECT MAX(order_total) AS most_expensive_order FROM Order 

--average number of items per order
SELECT COUNT(menu_item) / COUNT(DISTINCT order_id) AS avg_items_per_order FROM Order_Items GROUP BY order_id;

--most items ordered in one order
SELECT MAX(item_count) AS max_items_order FROM (SELECT order_id, COUNT(menu_item) AS item_count FROM Order_Items GROUP BY order_id) subquery;
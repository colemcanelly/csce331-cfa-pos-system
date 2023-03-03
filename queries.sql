--2 busiest months (most orders over a month) (works)
SELECT TO_CHAR(DATE_TRUNC('month', order_date), 'MM-YYYY') AS month, COUNT(order_id) AS total_orders FROM Orders GROUP BY month ORDER BY total_orders DESC LIMIT 2;

--Most popular item (works)
SELECT menu_item FROM Order_Items GROUP BY menu_item ORDER BY COUNT(*) DESC LIMIT 1;

--Most used kiosk (works)
SELECT kiosk_id, COUNT(*) AS usage_count FROM Orders GROUP BY kiosk_id ORDER BY usage_count DESC LIMIT 1;

--Least popular item (works)
SELECT menu_item FROM Order_Items GROUP BY menu_item ORDER BY COUNT(*) ASC LIMIT 1;

--2 slowest  (works)
SELECT TO_CHAR(DATE_TRUNC('month', order_date), 'MM-YYYY') AS month, COUNT(order_id) AS total_orders FROM Orders GROUP BY month ORDER BY total_orders ASC LIMIT 2;

--Busiest day (works)
SELECT order_date, COUNT(order_num) AS total_orders FROM Orders GROUP BY order_date ORDER BY total_orders DESC LIMIT 1;

--total revenue (works)
SELECT SUM(order_total) AS total_revenue FROM Orders;

--Top 3 months that performed the best( most revenue) (works)
SELECT to_char(order_date, 'YYYY-MM') AS order_month, SUM(order_total) AS revenue FROM Orders GROUP BY order_month ORDER BY revenue DESC LIMIT 3;

--most common customer name (works)
SELECT customer_name FROM Orders GROUP BY customer_name ORDER BY COUNT(*) DESC LIMIT 4;

--average order price (works)
SELECT AVG(order_total) AS average_order_price FROM Orders;

--average revenue for each day of operation (works)
SELECT AVG(daily_total) AS avg_revenue FROM (SELECT order_date, SUM(order_total) AS daily_total FROM Orders GROUP BY order_date) AS daily_totals;

--most used ingredient (works)
SELECT mode() WITHIN GROUP (ORDER BY ingredient) AS common_ingredient FROM Daily_Inventory;

--most expensive order (works)
SELECT MAX(order_total) AS most_expensive_order FROM Orders; 

--average number of items per order (works)
SELECT AVG(avg_items_per_order) as overall_avg_items_per_order 
FROM (SELECT COUNT(menu_item) / COUNT(DISTINCT order_id) AS avg_items_per_order FROM Order_Items GROUP BY order_id) subquery;

--most items ordered in one order (works)
SELECT MAX(item_count) AS max_items_order FROM (SELECT order_id, COUNT(menu_item) AS item_count FROM Order_Items GROUP BY order_id) subquery;
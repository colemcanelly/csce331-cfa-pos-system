package application.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.*;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Integration class to interface between frontend and backend
 * 
 * @author  colemcanelly
 */
public class CFADataBase {
    private static final String SELECT_MANAGER = "SELECT * FROM managers";
    private static final String SELECT_KIOSK = "SELECT * FROM kiosk";
    private static final String SELECT_MENU = "SELECT * FROM menu";
    private static final String SELECT_SUPPLY = "SELECT * FROM supply";
    private static final String SELECT_RECIPE = "SELECT * FROM recipes";
    private static final String SELECT_ORDER = "SELECT * FROM orders";
    private static final String SELECT_ORDER_ITEMS = "SELECT * FROM order_items";
    private static final String SELECT_DAILY_INV = "SELECT * FROM daily_inventory";
    private static final List<String> MANAGER_PKS = Arrays.asList("manager_id");
    private static final List<String> KIOSK_PKS = Arrays.asList("kiosk_id");
    private static final List<String> MENU_PKS = Arrays.asList("menu_item");
    private static final List<String> SUPPLY_PKS = Arrays.asList("ingredient");
    private static final List<String> RECIPE_PKS = Arrays.asList("menu_item", "ingredient");
    private static final List<String> ORDER_PKS = Arrays.asList("order_id");
    private static final List<String> ORDER_ITEMS_PKS = Arrays.asList("order_id", "menu_item");
    private static final List<String> DAILY_INV_PKS = Arrays.asList("entry_date", "ingredient");
    
    public PostgreSQL psql = null;

    public enum Table {
        MANAGER, KIOSK, MENU, SUPPLY, RECIPE, ORDER, ORDER_ITEMS, DAILY_INV 
    }

    public CFADataBase()
    {
        psql = new PostgreSQL();
    }

    /**
     * Gets the menu in a dictionary of key-value pairs, where key = primary key
     *      ex. {{"menu_item", "ChickFil-A Chicken Sandwich"}, ... }
     * @param  __table  The desired table to query   
     * @return          Dictionary of entries in the requested table (dictionaries)
     */
    public Map<String, Map<String, String>> get(Table __table) {
        Map<String, Map<String, String>> table = null;
        try {
            switch (__table) {
                case MANAGER:
                    table = rsToMap(psql.select(SELECT_MANAGER), MANAGER_PKS);    
                    break;
                case KIOSK:
                    table = rsToMap(psql.select(SELECT_KIOSK), KIOSK_PKS);
                    break;
                case MENU:
                    table = rsToMap(psql.select(SELECT_MENU), MENU_PKS);
                    break;
                case SUPPLY:
                    table = rsToMap(psql.select(SELECT_SUPPLY), SUPPLY_PKS);
                    break;
                case RECIPE:
                    table = rsToMap(psql.select(SELECT_RECIPE), RECIPE_PKS);
                    break;
                case ORDER:
                    table = rsToMap(psql.select(SELECT_ORDER), ORDER_PKS);
                    break;
                case ORDER_ITEMS:
                    table = rsToMap(psql.select(SELECT_ORDER_ITEMS), ORDER_ITEMS_PKS);
                    break;
                case DAILY_INV:
                    table = rsToMap(psql.select(SELECT_DAILY_INV),DAILY_INV_PKS);    
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error refreshing menu.");
            return null;
        }
        return table;
    }

    /**
     * Gets the inventory for each item that is in the most recent day entered into the daily_inventory table.
     * 
     * @return Table of the most recent inventory report. (Dictionary of Dictionaries)
     */
    public Map<String, Map<String, String>> getNewestInv()
    {
        Map<String, Map<String, String>> table = null;
        try {
            String latest_daily_inv_entries = "SELECT entry_date, ingredient, qty_eod FROM daily_inventory WHERE entry_date = (SELECT max(entry_date) FROM daily_inventory);";
            table = rsToMap(psql.select(latest_daily_inv_entries), DAILY_INV_PKS);
        } catch (Exception e) {
            System.out.println("Error refreshing menu.");
            return null;
        }
        return table;
    }

    /**
     * Gets a collection of items whose current quantity is lower than the specified threshold.
     * Presents the manager with a view of all items that are currently low and in need of restocking.
     * 
     * @return Table of current low items that need restocking. (Dictionary of Dictionaries)
     */
    public Map<String, Map<String, String>> getRestockReport()
    {
        Map<String, Map<String, String>> table = null;
        try {
            String restock_report = ""
                .concat("SELECT ")
                .concat(    "today.ingredient AS ingredient, ")
                .concat(    "today.qty_curr AS quantity, ")
                .concat(    "supply.threshold AS threshold ")
                .concat("FROM ( ")
                .concat(    "SELECT DISTINCT ON (ingredient) ")
                .concat(        "ingredient, ")
                .concat(        "qty_eod AS qty_curr ")
                .concat(    "FROM daily_inventory ")
                .concat(    "ORDER  BY ingredient, entry_date DESC) AS today ")
                .concat("INNER JOIN supply ON today.ingredient = supply.ingredient AND supply.threshold >= today.qty_curr;");
            table = rsToMapTwo(psql.select(restock_report), SUPPLY_PKS);
        } catch (Exception e) {
            System.out.println("Error fetching restock report");
            return null;
        }
        return table;
    }

    /**
     * Gets a collection of items who have only been selling <10% of their starting quantity each day.
     * Presents the manager with a table of items which are retaining extreme excess at the end of each specified day.
     * 
     * @param   date    Which day to generate the excess report for
     * @return Table of the items with excess. (Dictionary of Dictionaries)
     */
    public Map<String, Map<String, String>> getExcessReport(LocalDate date)
    {
        Map<String, Map<String, String>> table = null;
        try {
        	String excess_report = "WITH cte AS (\r\n"
    	   			+ "    SELECT \r\n"
    	   			+ "        ingredient, \r\n"
    	   			+ "        qty_sod, \r\n"
    	   			+ "        SUM(qty_sold) as total_qty_sold, \r\n"
    	   			+ "        ((qty_sod - SUM(qty_sold))/qty_sod)*100 as percentage_diff\r\n"
    	   			+ "    FROM \r\n"
    	   			+ "        daily_inventory\r\n"
    	   			+ "    WHERE \r\n"
    	   			+ "        entry_date >= '" + date + "' \r\n"
    	   			+ "    GROUP BY \r\n"
    	   			+ "        ingredient, qty_sod\r\n"
    	   			+ ")\r\n"
    	   			+ "SELECT \r\n"
    	   			+ "    ingredient, \r\n"
    	   			+ "    qty_sod, \r\n"
    	   			+ "    total_qty_sold, \r\n"
    	   			+ "    percentage_diff\r\n"
    	   			+ "FROM \r\n"
    	   			+ "    cte\r\n"
    	   			+ "WHERE \r\n"
    	   			+ "    percentage_diff > 90;";
        	System.out.println(excess_report);
            table = rsToMapTwo(psql.select(excess_report), SUPPLY_PKS);
        } catch (Exception e) {
            System.out.println("Error fetching excess report");
            return null;
        }
        return table;
    }

    public Map<String, Map<String, String>> getSalesReport(String start_date, String end_date)
    {
        return getSalesReport(start_date, end_date, "06:00:00", "22:30:00");
    }

    /**
     * Generates a sales report given starting date and time, and ending date and time.
     * 
     * @param   start_date  Date to start the sales report on
     * @param   end_date    Date to end the sales report on
     * @param   start_time  Time of day during the `start_date` to start the report
     * @param   end_time    Time of day during the `end_date` to end the report
     * 
     * @return Sales report in table format (Dictionary of Dictionaries)
     */
    public Map<String, Map<String, String>> getSalesReport(String start_date, String end_date, String start_time, String end_time)
    {
        Map<String, Map<String, String>> table = null;
        try {
            String sales_report_by_item = "SELECT COALESCE(menu.menu_item, 'Total') AS menu_item, COALESCE(SUM(order_items.menu_item_quantity * order_items.food_price), 0) AS total_revenue " +
                    "FROM orders " +
                    "JOIN menu ON 1=1 " +
                    "LEFT JOIN order_items ON orders.order_id = order_items.order_id AND menu.menu_item = order_items.menu_item" +
                    " WHERE orders.order_date BETWEEN '" + start_date + "' AND '" + end_date + "' " +
                    "AND orders.order_time BETWEEN '" + start_time + "' AND '" + end_time + "' " +
                    "GROUP BY ROLLUP(menu.menu_item);";
                
            table = rsToMapTwo(psql.select(sales_report_by_item), MENU_PKS);
        } catch (Exception e) {
            System.out.println("Error fetching sales report using 2");
            return null;
        }
        return table;
    }

    /**
     * Used to execute an insertion query from our database (CREATE, UPDATE, INSERT, etc.).
     * @param ordered_items List of "menu_item" strings, ie the name of the item to order
     * @param customer_name Customer Name
     * @param kiosk_id      Integer ID of the kiosk that created the order
     * @param order_num     The number of the order being created
     * @return  Boolean to represent insertion sucess or failure.
     */
    public boolean newOrder(ArrayList<String> ordered_items, String customer_name, int kiosk_id, int order_num)
    {
        // Insert a new order without totaling the order for time complexity
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Double order_total = 0.0;
        String order_query = String.format("INSERT INTO orders VALUES ((SELECT nextval('orders_order_id_seq')), %d, '%s', '%s', %f, '%s', %d);", order_num, date.toString(), time.toString(), order_total, customer_name, kiosk_id);
        if (psql.query(order_query) < 0) {
            System.out.println("Error inserting order");
            System.out.println(order_query);
            return false;
        }
        // Query the order id of the new order that was just created
        String query_order_id = "SELECT max(order_id) FROM orders;";
        int order_id = -1;
        try {
            ResultSet res = psql.select(query_order_id);
            res.next();
            System.out.println(res.getString("max"));
            order_id = res.getInt("max");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error getting order id for order item");
            return false;
        }
        // Create order_item entries for each, using the order_id from the original order, while calculating the order total
        for (String item_name : ordered_items) {
            Double food_price = Double.parseDouble(get(Table.MENU).get(item_name).get("food_price"));
            order_total += food_price;
            String order_item_query = String.format("INSERT INTO order_items VALUES (%d, '%s', %d, %f);", order_id, item_name, 1, food_price);
            System.out.println(order_item_query);
            if (psql.query(order_item_query) < 0) {
                System.out.println("Error inserting order item");
                return false;
            }
            // update menu_item_quantity
        }
        // Update the original order with the calculated order total
        String update_order = String.format("UPDATE orders SET order_total = %f WHERE order_id = %d;",order_total, order_id);
        if (psql.query(update_order) != 1) {
            System.out.println(String.format("Incomplete record where order_id = %d", order_id));
            return false;
        }
        return true;
    }

    /**
     * Converts a JDBC Result Set to a Java Dictionary of Dictionaries given primary keys for the table
     * 
     * @param   rs          ResultSet to convert
     * @param   pk_names    List of primary keys for the Set in string format
     * @return  Dictionary of Dictionaries for the given result set
     */
    private static Map<String, Map<String, String>> rsToMap(ResultSet rs, List<String> pk_names)
    {
        Map<String, Map<String, String>> results = new HashMap<String, Map<String, String>>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()) {
                rs.refreshRow();
                Map<String, String> row = new HashMap<String, String>();
                String pk = "";
                for (int i = 1; i <= columns; i++) {
                    String col_name = md.getColumnName(i);
                    String attribute = rs.getString(i);
                    if (pk_names.contains(col_name)) pk += attribute;
                    row.put(col_name, attribute);
                }
                results.put(pk, row);
            }
            rs.beforeFirst();
        } catch (Exception e) {
            System.out.println("Error accessing Database rsMap.");
        }
        return results;
    }

    /**
     * OVERLOAD
     * Converts a JDBC Result Set to a Java Dictionary of Dictionaries given primary keys for the table
     * 
     * @param   rs          ResultSet to convert
     * @param   pk_names    List of primary keys for the Set in string format
     * @return  Dictionary of Dictionaries for the given result set
     */
    private static Map<String, Map<String, String>> rsToMapTwo(ResultSet rs, List<String> pk_names)
    {
        Map<String, Map<String, String>> results = new HashMap<String, Map<String, String>>();
        try {
        	
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()) {
            	
                //rs.refreshRow();
               
                Map<String, String> row = new HashMap<String, String>();
              
                String pk = "";
                for (int i = 1; i <= columns; i++) {
                    String col_name = md.getColumnName(i);
                    String attribute = rs.getString(i);
                    //System.out.println("Col_name = " + col_name + ", attribute = " + attribute);
                    if (pk_names.contains(col_name)) pk += attribute;
                    row.put(col_name, attribute);
                    
                }
                results.put(pk, row);
            }
           // rs.beforeFirst();
        } catch (Exception e) {
            System.out.println("Error accessing Database. 1");
        }
        return results;
    }
}

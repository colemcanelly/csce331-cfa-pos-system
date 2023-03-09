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
     * @param t     The desired table to query   
     * @return      Dictionary of menu entries (dictionaries)
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
     * Used to execute an insertion query from our database (CREATE, UPDATE, INSERT, etc.).
     * @param ordered_items List of "menu_item" strings, ie the name of the item to order
     * @param customer_name  Customer Name
     * @param kiosk_id  Integer ID of the kiosk that created the order
     * @return      Number of rows impacted by your query
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
            System.out.println("Error accessing Database.");
        }
        return results;
    }
}

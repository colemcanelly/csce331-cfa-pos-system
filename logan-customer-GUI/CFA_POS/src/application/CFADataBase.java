package application;

import java.util.HashMap;
import java.util.ArrayList;
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
    private static final String MENU_QUERY = "SELECT * FROM menu";
    private static final String SUPPLY_QUERY = "SELECT * FROM supply";

    private PostgreSQL psql = null;
    private ResultSet menu_set = null;
    private ResultSet supply_set = null;
    private Map<String, Map<String, String>> menu = null;
    private Map<String, Map<String, String>> supply = null;


    public CFADataBase()
    {
        psql = new PostgreSQL();
        menu_set = psql.select(MENU_QUERY);
        supply_set = psql.select(SUPPLY_QUERY);
        refreshMenu();
    }

    /**
     * Gets the menu in a dictionary of key-value pairs, where key = primary key
     *      ex. {{"menu_item", "ChickFil-A Chicken Sandwich"}, ... }
     * @param 
     * @return      Dictionary of menu entries (dictionaries)
     */
    public Map<String, Map<String, String>> getMenu() {
        // refreshMenu();
        return menu; 
    }

    /**
     * Refreshes the menu, returns true if successful
     * @param 
     * @return      Status of refresh request
     */
    public Boolean refreshMenu()
    {
        try {
            ArrayList<String> pks = new ArrayList<String>();
            pks.add("menu_item");
            this.menu = rsToMap(menu_set, pks);
        } catch (Exception e) {
            System.out.println("Error refreshing menu.");
            return false;
        }
        return true;
    }

    /**
     * Gets the menu in a dictionary of key-value pairs, where key = primary key
     *      ex. {{"menu_item", "ChickFil-A Chicken Sandwich"}, ... }
     * @param 
     * @return      Dictionary of menu entries (dictionaries)
     */
    public Map<String, Map<String, String>> getSupply() {
        // refreshMenu();
        return supply; 
    }

    /**
     * Refreshes the menu, returns true if successful
     * @param 
     * @return      Status of refresh request
     */
    public Boolean refreshSupply()
    {
        try {
            ArrayList<String> pks = new ArrayList<String>();
            pks.add("ingredient");
            this.menu = rsToMap(supply_set, pks);
        } catch (Exception e) {
            System.out.println("Error refreshing menu.");
            return false;
        }
        return true;
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
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Double order_total = 0.0;

        for (String item_name : ordered_items) {
            Double food_price = Double.parseDouble(menu.get(item_name).get("food_price"));
            order_total += food_price;
            String order_item_query = String.format(
                "INSERT INTO order_items VALUES (nextval('orders_order_id_seq'), %s, %d, %f);",
                item_name, 1, food_price); 
            if (psql.query(order_item_query) < 0) {
                System.out.println("Error inserting order item");
                return false;
            }
            // update menu_item_quantity
        }
        String order_query = String.format(
            "INSERT INTO orders VALUES (%d, %s, %s, %f, %s, %d);",
            order_num, date.toString(), time.toString(), order_total, customer_name, kiosk_id);
        if (psql.query(order_query) < 0) {
            System.out.println("Error inserting order");
            return false;
        }
        return true;
    }

    private static Map<String, Map<String, String>> rsToMap(ResultSet rs, ArrayList<String> pk_names)
    {
        Map<String, Map<String, String>> results = new HashMap<String, Map<String, String>>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()) {
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
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
        return results;
    }
}

package menutable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.Map;

/**
 * Integration class to interface between frontend and backend
 *
 * @author  colemcanelly
 */
public class CFADataBase {
    private static final String MENU_QUERY = "SELECT * FROM menu";

    private ResultSet menu_set = null;
    private Map<String, Map<String, String>> menu = null;


    public CFADataBase()
    {
        PostgreSQL psql = new PostgreSQL();
        menu_set = psql.query(MENU_QUERY);
        refreshMenu();
    }

    /**
     * Gets the menu in a collection of key-value pairs
     *      ex. {{"menu_item", "ChickFil-A Chicken Sandwich"}, ... }
     * @param
     * @return      Collection of menu entries
     */
    public Map<String, Map<String, String>> getMenu() { return menu; }

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
     * Used to execute an insertion query from our database (CREATE, UPDATE, INSERT, etc.).
     * @param ordered_items List of "menu_item" strings, ie the name of the item to order
     * @param name  Customer Name
     * @param kiosk_id  Integer ID of the kiosk that created the order
     * @return      Number of rows impacted by your query
     */
    public int newOrder(ArrayList<String> ordered_items, String name, int kiosk_id) { return 0; }

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
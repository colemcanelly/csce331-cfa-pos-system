package application;
import java.sql.*;
import java.util.ArrayList;

/**
 * Abstraction class to query and update the PostgreSQL database given SQL commands
 * 
 * @author  colemcanelly
 */
public class PostgreSQL {
    private static final String USER = "csce315331_delta_master";
    private static final String PSWD = "airplane";
    private Connection conn = null;
    private ArrayList<Statement> stmts = null;

    public PostgreSQL ()
    {
        stmts = new ArrayList<Statement>();
        // Building the connection
        try {
            Class.forName("org.postgresql.Driver");
            String server_url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_delta";
            this.conn = DriverManager.getConnection(server_url, USER, PSWD);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } // end try catch
        System.out.println("Opened database successfully");
    }

    /**
     * Closes the PostgreSQL database connection
     * @param 
     * @return
     */
    public void close ()
    {
        // closing the connection
        try {
            for (Statement stmt : stmts) {
                stmt.close();
            }
            this.conn.close();
            System.out.println("Connection Closed.");
        } catch (Exception e) {
            System.out.println("Connection NOT Closed.");
        } // end try catch
    }
    
    /**
     * Used to execute a `SELECT` query from our database
     * @param sql   The SELECT statment to run
     * @return      The results of that query, in a ResultSet object
     */
    public ResultSet query (String sql)
    {
        ResultSet result = null;
        try {
            // create a statement object that can be interactively updated and sensitive to changes made in real-time
            stmts.add(this.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));
            // send statement to DBMS
            result = stmts.get(stmts.size() - 1).executeQuery(sql);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
        }
        return result;
    }

    /**
     * Used to execute an insertion query from our database (CREATE, UPDATE, INSERT, etc.).
     * @param sql   The sql query you would like to execute
     * @return      Number of rows impacted by your query
     */
    public int update (String sql)
    { 
        int rows = 0;
        try {
            // create a statement object that can be interactively updated and sensitive to changes made in real-time
            Statement s = this.conn.createStatement();
            // send statement to DBMS
            rows = s.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error accessing Database.");
            return -1;
        }
        return rows;
    }

}// end Class

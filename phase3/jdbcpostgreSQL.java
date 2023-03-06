import java.sql.*;
//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019, 10-15-2022
 */
public class jdbcpostgreSQL {
  public static void main(String args[]) {
    //Building the connection
     Connection conn = null;
     try {
       /*
        org.postgresql.driver is located in the postgresql-42.2.8.jar file.
        this jar file must be included in your class path to allow the connection.
       */
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_delta",
           dbSetup.user, dbSetup.pswd);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }//end try catch
     System.out.println("Opened database successfully");

     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
       String sqlStatement = "SELECT * FROM menu";
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
       System.out.println("Menu Items from Menu");
       System.out.println("______________________________________");
       while (result.next()) {
         System.out.println(result.getString("menu_item"));
         System.out.println(result.getString("food_price"));
       }
   } catch (Exception e){
     System.out.println("Error accessing Database.");
   }

    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class

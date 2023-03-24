package application.controllers;


/**
 * Excess Item is a class gets and sets necessary items for an Excess Item. Used in ManagerExcessReportController.java
 * 
 * @author      Weston Cadena <westoncadena@gmail.com>
 * @version     1.3                 (current version number of program)
 * @since       1.3         (the version of the package this class was first added to)
 */
public class ExcessItem {
    private String ingredient;
    private String start_quantity;
    private String quantity_sold;
    private String percent_difference;

    public ExcessItem(String ingredient, String start_quantity, String quantity_sold, String percent_difference) {
        this.ingredient = ingredient;
        this.start_quantity = start_quantity;
        this.quantity_sold = quantity_sold;
        this.percent_difference = percent_difference;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getStart_quantity() {
        return start_quantity;
    }

    public void setStart_quantity(String start_quantity) {
        this.start_quantity = start_quantity;
    }

    public String getQuantity_sold() {
        return quantity_sold;
    }

    public void setQuantity_sold(String quantity_sold) {
        this.quantity_sold = quantity_sold;
    }
    
    public String getPercent_difference() {
        return percent_difference;
    }
    
    public void setPercent_difference(String percent_difference) {
        this.percent_difference = percent_difference;
    }
}
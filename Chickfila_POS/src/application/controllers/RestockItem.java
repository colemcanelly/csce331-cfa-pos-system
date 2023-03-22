package application.controllers;

public class RestockItem {
    private String ingredient;
    private String quantity;
    private String threshold;
    private String percent_difference;

    public RestockItem(String ingredient, String quantity, String threshold ) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.threshold = threshold;
        this.percent_difference = percent_difference;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }
}
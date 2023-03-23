package application.controllers;

import java.sql.Date;

public class IngredientItem {
    private String ingredient;
    private String supply;
    private String curr_date;

    public IngredientItem(String ingredient, String supply, String curr_date) {
        this.ingredient = ingredient;
        this.supply = supply;
        this.curr_date = curr_date;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getCurr_date() {
        return curr_date;
    }

    public void setCurr_date(String curr_date) {
        this.curr_date = curr_date;
    }
}
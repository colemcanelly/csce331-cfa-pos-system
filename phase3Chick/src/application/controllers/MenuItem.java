package application.controllers;

/**
 * Menu Item is a class gets and sets necessary items for an Menu Item. Used in MangerMenuController.java
 * 
 * @author      Ryan Paul
 * @version     1.3                 (current version number of program)
 * @since       1.3         (the version of the package this class was first added to)
 */
public class MenuItem {
    String item_name;
    float item_price;
    String item_combo;
    String item_category;


    public MenuItem(String item_name, float item_price, String item_combo, String item_category){
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_combo = item_combo;
        this.item_category = item_category;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public String getItem_combo() {
        return item_combo;
    }

    public void setItem_combo(String item_combo) {
        this.item_combo = item_combo;
    }

    public String getMenu_item_name() {
        return item_name;
    }

    public void setMenu_item_name(String item_name) {
        this.item_name = item_name;
    }


    public float getItem_price() {
        return item_price;
    }

    public void setItem_price(float item_price) {
        this.item_price = item_price;
    }
}
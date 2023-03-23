package application.controllers;

public class SalesItem {
    String item_name;
    float item_total;

    public SalesItem(String item_name, float item_total){
        this.item_name = item_name;
        this.item_total = item_total;
    }
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public float getItem_total() {
        return item_total;
    }

    public void setItem_total(float item_total) {
        this.item_total = item_total;
    }
}

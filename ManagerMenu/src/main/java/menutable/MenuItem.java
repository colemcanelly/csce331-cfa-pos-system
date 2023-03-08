package menutable;

public class MenuItem {
    String item_name;
    float item_price;

    public MenuItem(String item_name, float item_price){
        this.item_name = item_name;
        this.item_price = item_price;
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

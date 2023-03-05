-- This creates the tables we need and fills the with the generated data

CREATE TABLE managers (
    manager_id INT PRIMARY KEY,
    manager_pw VARCHAR(255)
);
\copy managers from './DataGeneration/managers.csv' CSV


CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    order_num INT,
    order_date DATE,
    order_time TIME,
    order_total FLOAT,
    customer_name VARCHAR(255),
    kiosk_id INT,
    FOREIGN KEY (kiosk_id) REFERENCES kiosk(kiosk_id)
);
\copy orders from './DataGeneration/orders.csv' CSV


CREATE TABLE menu (
    menu_item VARCHAR(255) PRIMARY KEY,
    combo BOOLEAN,
    food_price FLOAT,
    img VARCHAR(255)
);
\copy menu from './DataGeneration/menu.csv' CSV


CREATE TABLE order_items (
    order_id INT,
    menu_item VARCHAR(255),
    menu_item_quantity INT,
    food_price FLOAT,
    PRIMARY KEY (order_id, menu_item),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (menu_item) REFERENCES menu(menu_item)
);
\copy order_items from './DataGeneration/order_items.csv' CSV




CREATE TABLE recipes (
    menu_item VARCHAR(255),
    ingredient VARCHAR(255),
    portion_count FLOAT,
    PRIMARY KEY (menu_item, ingredient),
    FOREIGN KEY (menu_item) REFERENCES menu(menu_item),
    FOREIGN KEY (ingredient) REFERENCES supply(ingredient)
);
\copy recipes from './DataGeneration/recipes.csv' CSV


CREATE TABLE daily_inventory (
    entry_date DATE,
    ingredient VARCHAR(255),
    qty_sod FLOAT,
    qty_eod FLOAT,
    qty_new FLOAT,
    qty_sold FLOAT,
    PRIMARY KEY (entry_date, ingredient),
    FOREIGN KEY (ingredient) REFERENCES supply(ingredient)
);
\copy daily_inventory from './DataGeneration/daily_inventory.csv' CSV

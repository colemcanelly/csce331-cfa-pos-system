-- Remove all previous tables
DROP TABLE 
    managers,
    kiosk,
    menu,
    supply,
    recipes,
    orders,
    order_items,
    daily_inventory;


-- This creates the tables we need and fills the with the generated data
CREATE TABLE managers (
    manager_id INT PRIMARY KEY,
    manager_pw VARCHAR(255)
);

CREATE TABLE kiosk (
    kiosk_id INT PRIMARY KEY,
    kiosk_on BOOLEAN
);

CREATE TABLE menu (
    menu_item VARCHAR(255) PRIMARY KEY,
    menu_cat VARCHAR(255),
    combo BOOLEAN,
    food_price FLOAT,
    img VARCHAR(255)
);

CREATE TABLE supply (
    ingredient VARCHAR(255) PRIMARY KEY,
    threshold FLOAT,
    restock_quantity FLOAT
);

CREATE TABLE recipes (
    menu_item VARCHAR(255),
    ingredient VARCHAR(255),
    portion_count FLOAT,
    PRIMARY KEY (menu_item, ingredient),
    FOREIGN KEY (menu_item) REFERENCES menu(menu_item),
    FOREIGN KEY (ingredient) REFERENCES supply(ingredient)
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    order_num INT,
    order_date DATE,
    order_time TIME,
    order_total FLOAT,
    customer_name VARCHAR(255),
    kiosk_id INT,
    FOREIGN KEY (kiosk_id) REFERENCES kiosk(kiosk_id)
);

CREATE TABLE order_items (
    order_id INT,
    menu_item VARCHAR(255),
    menu_item_quantity INT,
    food_price FLOAT,
    PRIMARY KEY (order_id, menu_item),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (menu_item) REFERENCES menu(menu_item)
);

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

-- Populate new tables with CSV's
\copy managers from './csv/managers.csv' CSV
\copy kiosk from './csv/kiosks.csv' CSV
\copy menu from './csv/menu.csv' CSV
\copy supply from './csv/supply.csv' CSV
\copy recipes from './csv/recipes.csv' CSV
\copy orders from './csv/orders.csv' CSV
\copy order_items from './csv/order_items.csv' CSV
\copy daily_inventory from './csv/daily_inventory.csv' CSV

-- Set the sequence value for entering new data
 SELECT setval('orders_order_id_seq', (SELECT max(order_id) FROM orders));
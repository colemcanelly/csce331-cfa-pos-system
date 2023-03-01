import random,datetime

#########################################################################################
##      CLASS DEFINITIONS
class Kiosk:
    def __init__(self, line: str) -> None:
        attr = line.split(',')
        self.kiosk_id: int = int(attr[0])   # PK
        self.kiosk_on: bool = attr[1].lower() in ['true', '1', 'on', 't', 'y']
        self.curr_order_num: int = 0
    def __repr__(self):
        return ",".join([str(val) for val in self.__dict__.values()])


class MenuItem:
    def __init__(self, line: str) -> None:
        attr = line.split(',')
        self.item_name: str = attr[0]       # PK
        self.combo: bool = attr[1].lower() in ['true', '1', 'on', 't', 'y']
        self.price: float = float(attr[2])
        self.img: str = attr[3]
    def __repr__(self):
        return ",".join([str(val) for val in self.__dict__.values()])


class Recipe:
    def __init__(self, line: str) -> None:
        attr = line.split(',')
        self.menu_item: str = attr[0]       # PK
        self.ingredient: str = attr[1]      # PK
        self.portion_count: float = float(attr[2])
    
    def updateInventory(self, qty: float) -> None:
        global DailyInv
        onhand = next(filter(lambda itm : itm.ingredient == self.ingredient, DailyInv))
        onhand.qty_sold += (self.portion_count * qty)
    
    def __repr__(self):
        return ",".join([str(val) for val in self.__dict__.values()])


class Supply:
    def __init__(self, line: str) -> None:
        attr = line.split(',')
        self.ingredient: str = attr[0]      # PK
        self.threshold: float = float(attr[1])
        self.restock_quantity: float = float(attr[2])
    def __repr__(self):
        return ",".join([str(val) for val in self.__dict__.values()])

# Order will automatically create `ORDER` and `ORDER_ITEMS`,
#   which updates the `INVENTORY` through use of the recipe
class Order:
    def __init__(
            self,
            id:             int, 
            date:           str, 
            time:           str, 
            total:          float, 
            customer_name:  str, 
            kiosk_id:       int,
            order_items:          dict[str,int]
            ) -> None:
        # hopefully this gets be reference, idk
        global Kiosks
        kiosk = next(filter(lambda k : k.kiosk_id == kiosk_id, Kiosks))
        kiosk.curr_order_num += 1

        self.order_id:      int = id        # PK
        self.order_num:     int = (kiosk.kiosk_id * 1000) + kiosk.curr_order_num
        self.order_date:    str = date
        self.order_time:    str = time
        self.order_total:   float = total
        self.patron_name:   str = customer_name
        self.kiosk_id:      int = kiosk_id

        global OrderItems
        OrderItems.extend(list(map(lambda entry : OrderItem(self.order_id, entry), order_items.items())))

    def __repr__(self):
        return ",".join([str(val) for val in self.__dict__.values()])


class OrderItem:
    def __init__(self, id: int, order_entry: tuple[str,int]) -> None:
        global Menu, Recipes
        self.order_id: int = id
        self.menu_item: str = str(order_entry[0])
        self.menu_item_qty: int = int(order_entry[1])
        self.price: float = next(filter(lambda itm : itm.item_name == self.menu_item, Menu)).price

        map(
            lambda x: x.updateInventory(self.menu_item_qty), 
            filter(lambda x : x.menu_item == self.menu_item, Recipes)
            )
            
    def __repr__(self):
        return ",".join([str(val) for val in self.__dict__.values()])


class Inventory:
    def __init__(
            self, 
            date: str, 
            ingredient: str, 
            qty_sod: float, 
            qty_eod: float, 
            qty_new: float, 
            qty_sold: float
            ) -> None:
        self.date: str = date
        self.ingredient: str = ingredient
        self.qty_sod: float = qty_sod
        self.qty_eod: float = qty_eod
        self.qty_new: float = qty_new
        self.qty_sold: float = qty_sold
    
    def __repr__(self):
        return ",".join([str(val) for val in self.__dict__.values()])


#########################################################################################
##      GLOBAL VARS
Kiosks:     list[Kiosk]
Orders:     list[Order] = []
OrderItems: list[OrderItem] = []
Menu:       list[MenuItem]
Recipes:    list[Recipe]
Supplies:   list[Supply]
DailyInv:   list[Inventory] = []
NAMES:      list[str] = ["Weston", "Cole", "Ryan", "Logan", "Prof Ritchie"]
GAMEDAYS:   list[datetime.date] = [datetime.date(2022, 9, 10), datetime.date(2022, 9, 17)]
order_count: int = 0
DAILY_INVENTORY_FILE: str = "daily_inventory.csv"
ORDERS_FILE: str = "orders.csv"
ORDER_ITEMS_FILE: str = "order_items.csv"
#########################################################################################
##      METHODS

def fetchData(filename: str, constructor) -> list:
    fin = open(filename, 'r')
    data:list[str] = fin.readlines()
    return list(map(lambda line : constructor(line.replace('\n', '')), data))

# Creates an order and returns the total price
#       - Random customer name
#       - Random kiosk used
#       - Random number of order entries
#       - Random Menu item selected
#       - Random number of each Menu item
def generateSingleOrder(date: datetime.date, time: str) -> float:
    global Kiosks, Orders, OrderItems, Menu, Recipes, Supplies, DailyInv, order_count
    kiosk:  Kiosk   = random.choice(Kiosks) # random kiosk
    name:   str     = random.choice(NAMES)  # random customer
    total:  float   = 0.0
    items:  dict[str, int] = {}
    for entry in range(random.randint(1, 5)):   # random number of order entries
        item = random.choice(Menu)      # random item on the menu
        if (item.combo):
            items.update({"Waffle Potato Fries - Medium":1})
            items.update({"Soft Drinks Medium":1})
        number = random.randint(1,4)    # how many of that item
        total += (item.price * number)
        items.update({item.item_name: number})
    items.update({"To Go Bag":1})
    items.update({"Napkins":1})
    Orders.append(Order(order_count, str(date), time, total, name, kiosk.kiosk_id, items))
    order_count += 1
    return total

# Simulate a day, return the total profit
def simulateDay(date: datetime.date, min_profit: float) -> float:
    global Supplies, DailyInv, DAILY_INVENTORY_FILE
    DailyInv = list(map(lambda s : Inventory(str(date), s.ingredient, s.restock_quantity, 0.0, s.restock_quantity, 0.0), Supplies))

    min_profit += random.randint(0, int(min_profit / 10) + 1)   # random amount over the minimum
    profit: float = 0.0
    dateandtime = datetime.datetime(date.year, date.month, date.day, 8, 00) # 8:00 am
    while (profit < min_profit):
        dateandtime += datetime.timedelta(seconds=35)
        profit += generateSingleOrder(date, str(dateandtime.time()))
    fout = open(DAILY_INVENTORY_FILE, 'w')
    fout.writelines(list(map(lambda s: str(s) + '\n', DailyInv)))
    fout.close()
    DailyInv = []

    return profit

# Simulate a week
def simulateWeek(start_date: datetime.date, min_profit: float) -> float:
    global GAMEDAYS
    days_per_week = 6
    daily_profit: float = min_profit / days_per_week
    date: datetime.date = start_date
    profit: float = 0.0
    for day in range(days_per_week):
        if (date in GAMEDAYS):
            daily_profit += 10
        profit += simulateDay(date, daily_profit)
        date += datetime.timedelta(days=1)
    return profit

def simulate(start_date: datetime.date, num_weeks: int, min_profit: float):
    weekly_profit: float = min_profit / num_weeks
    date: datetime.date = start_date
    profit: float = 0.0
    order_id = 0

    print("Running Simulation")
    for week in range(num_weeks):
        profit += simulateWeek(date, weekly_profit)
        date += datetime.timedelta(days=7)
    print(f"Finshed. Profit = ${profit}")

def outputTables() -> None:
    global ORDERS_FILE, ORDER_ITEMS_FILE, Orders, OrderItems
    w_order_table = open(ORDERS_FILE, 'w')
    w_order_table.writelines(list(map(lambda s: str(s) + '\n', Orders)))
    w_order_table.close()
    w_order_items_table = open(ORDER_ITEMS_FILE, 'w')
    w_order_items_table.writelines(list(map(lambda s: str(s) + '\n', OrderItems)))
    w_order_items_table.close()

def main() -> None:
    kiosk_file: str = "kiosks.csv"
    menu_file: str = "menu.csv"
    recipe_file: str = "recipes.csv"
    supply_file: str = "supply.csv"

    global Kiosks, Menu, Recipes, Supplies
    Kiosks = fetchData(kiosk_file, lambda line : Kiosk(line))
    Menu = fetchData(menu_file, lambda line : MenuItem(line))
    Recipes = fetchData(recipe_file, lambda line : Recipe(line))
    Supplies = fetchData(supply_file, lambda line : Supply(line))
    
    start_date: datetime.date = datetime.date(2022, 2, 21)
    num_weeks = 52
    profit = 1000000.00
    simulate(start_date, num_weeks, profit)
    outputTables()

if __name__ == "__main__":
    main()
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
        supply = next(filter(lambda itm : itm.ingredient == self.ingredient, SUPPLIES))
        onhand = next(filter(lambda itm : itm.ingredient == self.ingredient, InvToday))
        onhand.qty_sold += (self.portion_count * qty)
        if ((onhand.qty_sod + onhand.qty_new - onhand.qty_sold) > supply.threshold):
            onhand.qty_new += supply.restock_quantity
    
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
        kiosk = next(filter(lambda k : k.kiosk_id == kiosk_id, Kiosks))
        kiosk.curr_order_num += 1

        self.order_id:      int = id        # PK
        assert (kiosk.curr_order_num < 1000), f"Order Number overflow: #{kiosk.curr_order_num}"
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
        self.order_id: int = id
        self.menu_item: str = str(order_entry[0])
        self.menu_item_qty: int = int(order_entry[1])
        self.price: float = next(filter(lambda itm : itm.item_name == self.menu_item, MENU)).price

        map(
            lambda x: x.updateInventory(self.menu_item_qty), 
            filter(lambda x : x.menu_item == self.menu_item, RECIPES)
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
MENU:       list[MenuItem]
RECIPES:    list[Recipe]
SUPPLIES:   list[Supply]
Orders:     list[Order]     = []
OrderItems: list[OrderItem] = []
InvToday:   list[Inventory] = []
InvHistory: list[Inventory] = []
NAMES:      list[str] = ["Weston", "Cole", "Ryan", "Logan", "Prof Ritchie"]
GAMEDAYS:   list[datetime.date] = [datetime.date(2022, 9, 10), datetime.date(2022, 9, 17)]
order_count: int = 0
#########################################################################################
##      FILE METHODS

def fetchTable(filename: str, constructor) -> list:
    fin = open(filename, 'r')
    data:list[str] = fin.readlines()
    return list(map(lambda line : constructor(line.replace('\n', '')), data))

def loadTables(date: datetime.date) -> None:
    kiosk_file: str = "./csv/kiosks.csv"
    menu_file: str = "./csv/menu.csv"
    recipe_file: str = "./csv/recipes.csv"
    supply_file: str = "./csv/supply.csv"

    global Kiosks, MENU, RECIPES, SUPPLIES, InvToday
    Kiosks      = fetchTable(kiosk_file, lambda line : Kiosk(line))
    MENU        = fetchTable(menu_file, lambda line : MenuItem(line))
    RECIPES     = fetchTable(recipe_file, lambda line : Recipe(line))
    SUPPLIES    = fetchTable(supply_file, lambda line : Supply(line))
    InvToday    = list(map(lambda s : Inventory(str(date), s.ingredient, 0.0, 0.0, s.restock_quantity, 0.0), SUPPLIES))

def unloadTables() -> None:
    global Orders
    orders_file: str = "./csv/orders.csv"
    w_order_table = open(orders_file, 'w')
    w_order_table.writelines(list(map(lambda s: str(s) + '\n', Orders)))
    w_order_table.close()
    global OrderItems
    order_items_file: str = "./csv/order_items.csv"
    w_order_items_table = open(order_items_file, 'w')
    w_order_items_table.writelines(list(map(lambda s: str(s) + '\n', OrderItems)))
    w_order_items_table.close()
    global InvHistory
    daily_inventory_file: str = "./csv/daily_inventory.csv"
    w_daily_inventory = open(daily_inventory_file, 'w')
    w_daily_inventory.writelines(list(map(lambda s: str(s) + '\n', InvHistory)))
    w_daily_inventory.close()
    
#########################################################################################
##      SIMULATION METHODS

# Creates an order and returns the total price
#       - Random customer name
#       - Random kiosk used
#       - Random number of order entries
#       - Random menu item selected
#       - Random number of each menu item
def generateSingleOrder(date: datetime.date, time: str) -> float:
    global Orders, OrderItems, order_count
    kiosk:  Kiosk   = random.choice(Kiosks) # random kiosk
    name:   str     = random.choice(NAMES)  # random customer
    total:  float   = 0.0
    items:  dict[str, int] = {}
    for _ in range(random.randint(1, 5)):   # random number of order entries
        item = random.choice(MENU)      # random item on the menu
        item_qty = random.randint(1,4)    # how many of that item
        if (item.combo):
            items.update({"Waffle Potato Fries - Medium":item_qty})
            items.update({"Soft Drinks Medium":item_qty})
        total += (item.price * item_qty)
        items.update({item.item_name: item_qty})
    items.update({"To Go Bag":1})
    items.update({"Napkins":1})
    Orders.append(Order(order_count, str(date), time, total, name, kiosk.kiosk_id, items))
    order_count += 1
    return total

def preOpening(date: datetime.date) -> None:
    global InvToday
    for item in InvToday:
        item.date = str(date)
        item.qty_sod = item.qty_eod
        item.qty_eod = 0.0
        item.qty_new = 0.0
        item.qty_sold = 0.0

def postClosing() -> None:
    global Kiosks, InvToday, InvHistory
    for kiosk in Kiosks:
        kiosk.curr_order_num = 0
    for item in InvToday:
        item.qty_eod = item.qty_sod + item.qty_new - item.qty_sold
    InvHistory.extend(InvToday)

# Simulate a day, return the total profit
def simulateDay(date: datetime.date, min_profit: float) -> float:
    profit: float = 0.0
    min_profit += (min_profit * (random.random() / 10))     # random amount over the minimum
    date_and_time = datetime.datetime(date.year, date.month, date.day, 8, 00) # 8:00 am
    preOpening(date)
    while (profit < min_profit):
        date_and_time += datetime.timedelta(seconds=35)
        profit += generateSingleOrder(date, str(date_and_time.time()))
    postClosing()
    return profit

# Simulate a week
def simulateWeek(start_date: datetime.date, min_profit: float) -> float:
    business_days = 6
    daily_profit: float = min_profit / business_days
    date: datetime.date = start_date
    profit: float = 0.0
    for _ in range(business_days):
        if (date in GAMEDAYS):
            daily_profit *= 10
        profit += simulateDay(date, daily_profit)
        date += datetime.timedelta(days=1)
    assert date == (start_date + datetime.timedelta(days=6)), "Incomplete weekly simulation"
    return profit

def simulate(start_date: datetime.date, num_weeks: int, min_profit: float) -> None:
    weekly_profit: float = min_profit / num_weeks
    date: datetime.date = start_date
    profit: float = 0.0
    order_id = 0

    print("\tRunning Simulation")
    for _ in range(num_weeks):
        profit += simulateWeek(date, weekly_profit)
        date += datetime.timedelta(days=7)
    print(f"\tFinshed.\n\tRevenue = ${profit}")


def main() -> None:   
    start_date: datetime.date = datetime.date(2022, 2, 21)
    num_weeks = 52
    profit = 1000000.00
    print("Beginning simulation . . . ")
    print(f"\tBegin on {start_date},\n\tSimulate for {num_weeks} weeks,\n\tGenerate a minimum profit of ${profit}")
    confirm = input("Press enter to confirm: ")
    if (confirm != ""):
        exit()
    loadTables(start_date)
    simulate(start_date, num_weeks, profit)
    unloadTables()

if __name__ == "__main__":
    main()
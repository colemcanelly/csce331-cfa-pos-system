import random,datetime

#########################################################################################
##      CLASS DEFINITIONS
class Table:
    def __repr__(self):
        return ",".join([str(val) for val in self.__dict__.values()])

class User(Table):
    def __init__(self, line: str) -> None:
        atts = iter(line.split(','))
        self.user_id: int = int(next(atts))   # PK
        self.permissions: int = int(next(atts))
        self.username: str = next(atts)
        self.user_pw: str = next(atts)
        self.fname: str = next(atts)
        self.lname: str = next(atts)

# class Kiosk(Table):
#     def __init__(self, line: str) -> None:
#         atts = iter(line.split(','))
#         self.kiosk_id: int = int(next(atts))   # PK
#         self.kiosk_on: bool = next(atts).lower() in ['true', '1', 'on', 't', 'y']
#         self.curr_order_num: int = 0

class MenuItem(Table):
    def __init__(self, line: str) -> None:
        atts = iter(line.split(','))
        self.item_name: str = next(atts)       # PK
        self.menu_category: str = next(atts)
        self.combo: bool = next(atts).lower() in ['true', '1', 'on', 't', 'y']
        self.price: float = float(next(atts))
        self.img: str = next(atts)

class Supply(Table):
    def __init__(self, line: str) -> None:
        atts = iter(line.split(','))
        self.ingredient: str = next(atts)      # PK
        self.threshold: float = float(next(atts))
        self.restock_quantity: float = float(next(atts))

class Inventory(Table):
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

class Recipe(Table):
    def __init__(self, line: str) -> None:
        atts = iter(line.split(','))
        self.menu_item: str = next(atts)       # PK
        self.ingredient: str = next(atts)      # PK
        self.portion_count: float = float(next(atts))
    
    def updateInventory(self, qty: float, inv_today: list[Inventory]) -> None:
        supply = next(filter(lambda itm : itm.ingredient == self.ingredient, SUPPLIES))
        onhand = next(filter(lambda itm : itm.ingredient == self.ingredient, inv_today))
        onhand.qty_sold += (self.portion_count * qty)
        if ((onhand.qty_sod + onhand.qty_new - onhand.qty_sold) < supply.threshold):
            onhand.qty_new += supply.restock_quantity
    
# Order will automatically create `ORDER` and `ORDER_ITEMS`,
#   which updates the `INVENTORY` through use of the recipe
class Order(Table):
    def __init__(self, date: str, time: str, inv_today: list[Inventory]) -> None:
        global Orders, OrderItems, order_count
        # hopefully this gets be reference, idk
        # kiosk = next(filter(lambda k : k.kiosk_id == kiosk_id, Kiosks))
        user:  User   = random.choice(Users) # random user
        # assert (kiosk.curr_order_num < 1000), f"Order Number overflow: #{kiosk.curr_order_num}"
        
        # Attributes
        self.order_id:      int = order_count        # PK
        # self.order_num:     int = (kiosk.kiosk_id * 1000) + kiosk.curr_order_num
        self.order_date:    str = date
        self.order_time:    str = time
        self.order_total:  float   = 0.0
        self.customer_fname:   str = user.fname if (user.permissions == 0) else random.choice(Users).fname
        self.order_creator:      int = user.user_id

        # Generate random order
        items:  dict[str, int] = {}
        for _ in range(random.randint(1, 5)):   # random number of order entries
            item = random.choice(MENU)      # random item on the menu
            item_qty = random.randint(1,4)    # how many of that item
            if (item.combo):
                items.update({"Waffle Potato Fries - Medium":item_qty})
                items.update({"Soft Drinks Medium":item_qty})
            self.order_total += (item.price * item_qty)
            items.update({item.item_name: item_qty})
        items.update({"To Go Bag":1})
        items.update({"Napkins":1})
        order_count += 1
        OrderItems.extend(list(map(lambda entry : OrderItem(self.order_id, entry, inv_today), items.items())))

class OrderItem(Table):
    def __init__(self, id: int, order_entry: tuple[str,int], inv_today: list[Inventory]) -> None:
        self.order_id: int = id
        self.menu_item: str = str(order_entry[0])
        self.menu_item_qty: int = int(order_entry[1])
        self.price: float = next(filter(lambda itm : itm.item_name == self.menu_item, MENU)).price

        for recipe in filter(lambda x : x.menu_item == self.menu_item, RECIPES):
            recipe.updateInventory(self.menu_item_qty, inv_today)
            

#########################################################################################
##      GLOBAL VARS
Users:      list[User]
# Kiosks:     list[Kiosk]
MENU:       list[MenuItem]
RECIPES:    list[Recipe]
SUPPLIES:   list[Supply]
Orders:     list[Order]     = []
OrderItems: list[OrderItem] = []
InvTempl:   list[Inventory] = []
InvHistory: list[list[Inventory]] = []
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
    # kiosk_file: str = "./csv/kiosks.csv"
    user_file: str = "./csv/users.csv"
    menu_file: str = "./csv/menu.csv"
    recipe_file: str = "./csv/recipes.csv"
    supply_file: str = "./csv/supply.csv"

    global Users, MENU, RECIPES, SUPPLIES, InvTempl #, Kiosks
    # Kiosks      = fetchTable(kiosk_file, lambda line : Kiosk(line))
    Users      = fetchTable(user_file, lambda line : User(line))
    MENU        = fetchTable(menu_file, lambda line : MenuItem(line))
    RECIPES     = fetchTable(recipe_file, lambda line : Recipe(line))
    SUPPLIES    = fetchTable(supply_file, lambda line : Supply(line))
    InvTempl    = list(map(lambda s : Inventory(str(date), s.ingredient, 0.0, 0.0, s.restock_quantity, 0.0), SUPPLIES))


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
    for inv in InvHistory:
        w_daily_inventory.writelines(list(map(lambda s: str(s) + '\n', inv)))
    w_daily_inventory.close()
    
#########################################################################################
##      SIMULATION METHODS

def preOpening(date: datetime.date) -> list[Inventory]:
    global InvTempl, InvHistory #, Kiosks
    # for kiosk in Kiosks:
    #     kiosk.curr_order_num = 0
    
    inv_today: list[Inventory] = []
    inv_yesterday: list[Inventory] = InvHistory[-1] if (len(InvHistory) > 0) else InvTempl
    for i in range(0, len(InvTempl)):
        qty_sod: float = inv_yesterday[i].qty_eod + inv_yesterday[i].qty_new - inv_yesterday[i].qty_sold
        inv_today.insert(i, Inventory(str(date), inv_yesterday[i].ingredient, qty_sod, 0.0, 0.0, 0.0))
        # print(inv_today[i])
    return inv_today

def postClosing(inv_today: list[Inventory]) -> None:
    global InvHistory
    for item in inv_today:
        item.qty_eod = item.qty_sod + item.qty_new - item.qty_sold
    InvHistory.append(inv_today)

# Simulate a day, return the total profit
def simulateDay(date: datetime.date, min_profit: float) -> float:
    profit: float = 0.0
    min_profit += (min_profit * (random.random() / 10))     # random amount over the minimum
    date_and_time = datetime.datetime(date.year, date.month, date.day, 8, 00) # 8:00 am
    inv_today: list[Inventory] = preOpening(date)
    while (profit < min_profit):
        date_and_time += datetime.timedelta(seconds=35)
        Orders.append(Order(str(date), str(date_and_time.time()), inv_today))
        profit += Orders[-1].order_total
    postClosing(inv_today)
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

    print("\tRunning Simulation")
    for _ in range(num_weeks):
        profit += simulateWeek(date, weekly_profit)
        date += datetime.timedelta(days=7)
    print(f"\tFinshed.\n\tRevenue = ${profit}")


def main() -> None:   
    start_date: datetime.date = datetime.date(2022, 2, 21)
    num_weeks = 100
    profit = 1900000.00
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
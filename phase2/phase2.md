# Project 2 Phase 2 Iteration
## Creating 52 weeks worth of data
In phase 2 we were challenged to polulate our databases over a 52 week period, generating over $1,000,000 in revenue at the Chickfala. In addition we needed to create 15 different SQL queries that will demonstate that our data is sucessfully populated and meets necessary requirements.
## Task Breakdown
* Ryan Paul - Tasked with creating and populating the menu item table inside our database. This will contain every item that is avaliable for purchase at our Chickfil-a. In addtion, Ryan will help create the 15 different SQL queries needed for our in lab demonstation

* Cole McAnelly - Create the python script that will take in our hand populated tables, and generate over 52 weeks worth of data for our database in the Orders, Order_Items, and Daily_Inventory tables. In the script, Cole must generate over $1,000,000 in simulated revenue over the 52 week span, as well as create 2 game days with a higher amount of sales.

* Logan Kettle - Will create a better document from phase 1 outlining how each entitity relates to each other in our project. In addition, Logan will help create the 15 different SQL queries needed for our in lab demonstation.

* Weston Cadena - Tasked with creating and populating ingredients and well as the few other tables needed to be poulated by hand. In addition, maintain a log of our progress over this phase.

## Updated
* restock_quantity attribute in supply - This will allow us to have a generic value to order when the supply values goes under our threshold
* combo attribute in menu_item - This will allow us to know if a menu item is a combo. If it is, then we will add a medium dring and fries to it.


## Important Notes
Right now in our daily inventory file is in need of another iteration in order to create a more realistic simulation. Do to time constraints in phase 2, our python script starts each day at a maxed restock value for each supply, and then updates it throughout the day. We will need to redo this script, in order to allow for the stock to keep up with real time quantitys, and be restocked when necessary.

In addition, we will need to simulate orders containing sauces. At the end of each order, sauces should be added to the order_item at the end of each order generation
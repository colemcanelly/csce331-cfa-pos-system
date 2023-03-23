# csce331-cfa-pos-system Project 2 Phase 4 Iteration
In Phase 4 of Project 2, we implemented different reports that the manager can see. These reports gave valuable information to the manager to understand basic knowlege of how the store is operating. The final part of phase 4 can be found in the phase3Chick folder.


#Features
##Sales Report
Given a time window, display the sales by item from the order history.


##X and Z reports
An X report shows the total sales for that day (since last Z report). A Z report shows the sales for that day and zeros the totals. This is done at closing time.  Previous X reports are not needed, but saving the Z reports in a new table could be helpful.

 

##Excess Report
Given a timestamp, display the list of items that only sold less than 10% of their inventory between the timestamp and the current time, assuming no restocks have happened during the window.


##Restock Report
Display the list of items whose current inventory is less than the item's minimum amount to have around before needing to restock.


##New Seasonal Menu Item
Your vendor just got a new seasonal menu item they have never sold before. Provide and demonstrate the ability to add this menu item to their POS (and any associated inventory items).

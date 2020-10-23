# Changelog

**-----v1.1.2 Hotfix-----**  
**Added** some extra lore for Book Stands.  
**Added** visual for selected Item in Market  

**Fixed** crash when copying Link Books.  
**Fixed** tooltip overlay stretching too far and looking weird.  
**Fixed** air Items showing up in Market.  

**-----v1.1.1 Hotfix-----**  

**Fixed** crash from printing the error message when a Player wrenched a Block that wasn't theirs.  

**-----v1.1.0 (Money Update)-----**

**Added** Market.  
The Market is a server-wide shop that allows Players to buy and sell Items.  
The Market's trades are edited by config, allowing server admins to create trades that every Player can see.  
There is an example config file showing how it works here: https://github.com/Calemi/CalemiUtils-1.15/blob/master/MarketItemsListExample.json.  
The Market can also be automated by connecting a Bank and placing an inventory on top of the Market.  

**Added** Cheap Money Bag & Rich Money Bag.  
The Money Bags contain a random amount of Coins when opened.  
They are currently not obtainable through crafting/loot.  
Instead, this Item could be obtained by a quest reward from another mod or some other way.  
Their random range values are in the config.  

**Added** Coin Stack.
There is a Coin Stack for every type of Coin.  
They can be placed by holding 8 or more coins.  
They show up in caves and are pretty uncommon.  
Their generation rarity value and the ability to disable their generation are in the config.

**Added** the ability to broadcast a Trading Post.  
There is a button in the Trading Post GUI that, when pressed, sends a message to everyone on the server containing information about the trade.  
There is a delay when the button is pressed (currently 10 seconds) that the player must wait untill broadcasting again, preventing major spam.  
The broadcast delay value and the ability to disable broadcasts are in the config.  

**Added** Coin sound effects for collecting Coins, trading in the Market, and opening Money Bags.

**Changed** the location of the config file!  
If you want to keep your old config values, please make sure you transfer your old file into the folder located at "config/CalemiUtils/".

**Changed** the "/cu" command to "/cutils".
The "/cu" command was conflicting with some mods.

**Removed** Villager trade. 

**Fixed** Trading Post not working well with certain stack sizes.  
**Fixed** Trading Post not working well with certain NBTs.  
**Fixed** Trading Post's overlay not showing some info.  

**Refined** code by more organizing and comments. 

**-----v1.0.7-----**

**Added** Villager trade (1 emerald for a quarter).  
**Added** rarity to some items (only changes the name color).  

**Fixed** commands not being registered.  
**Removed** empty lore in the Trading Post overlay.  

**-----v1.0.6-----**

**Added** a feature that allows Link Book to save the Player's rotation when linking & teleporting.  
**Added** Quark & Forge tags for Cobblestone. This will allow different Cobblestones from other mods to work with this mod too.  

**Changed** Wallet's Curios slot to look more like the others.  
**Changed** Trading Post's GUI elements to fit better with Inventory Sorter.  

**Fixed** Wallet's overlay showing when in F3.  

**-----v1.0.5-----**

**Fixed** Wallet & Entity print spam.  
**Fixed** Trading Post overlay for ItemStack lore.  

**-----v1.0.4-----**

**Fixed** command arguments not working on servers.  
**Fixed** Blender not functioning properly.  
**Fixed** Wallet overlay position config option not changing its position.  

**-----v1.0.3-----**

**Added** Curios Support.  

**Fixed** minor bugs and crashes.  

**Refined** code by more organizing and comments.  

**-----v1.0.2-----**

**Added** an overlay when the cursor is over a Trading Post. This overlay will display information about the trade.   

**Changed** tooltip background texture to be transparent.  

**Fixed** crash when Book Stand is broken.  
**Fixed** crash when Link Book is right-clicked into a Book Stand.  
**Fixed** currency added to Bank not syncing to the client.  
**Fixed** Trading Post not recognizing Items with no NBT.  
**Fixed** Starlight Sledgehammer not being Enchantable.  

**-----v1.0.1-----**

**Added** the ability to rename Blocks with inventories (like a Chest does when used in Anvil).  

**Removed** Blueprint from Creative Tab.  

**Fixed** crash when opening the Trading Post GUI.  
**Fixed** Trading Posts not using NBT when trading.  
**Fixed** Trading Posts amount values and price values being unrestricted. Previously you were allowed to sell items with a negative price value and or amount.  
**Fixed** Bank's name being lower-case in its GUI.  
**Fixed** Bank preventing Coins from being placed in GUI.  
**Fixed** Wallet being shown in Spectator mode. Now only shows when not in Spectator mode.  

**-----v1.0.0-----**

**Updated** mod to Minecraft 1.15.2  

**Added** Item Stand.  
The Item Stand can display any Item or Block you want. 
It can change into 4 different forms (one is invisible), and it contains options to transform, rotate, spin, and scale the displayed Item. 
It is excellent for shop signs, interior design, and adding extra detail to builds.

**Added** Mob Beacon.  
The Mob Beacon will prevent natural enemy spawning in its placed chunk. 
This was added due to the lack of Mob prevention options in mods for 1.15.2.

# Plugin Configuration

Here you will find helpful guidelines that explain the configuration better than the little notes in the files.
You can find all the config files [here](https://github.com/xbunnies/BunnieCoins/blob/master/src/main/resources/)!

## Config
General settings for the plugin.

### Settings
Under the settings section you are able to configure prefix, currency name, database as well as if the store is opened or not.

Database only supports MySQL and SQLite currently. However, MongoDB support will be coming very soon!

### Messages
Just like my previous plugins there is a section for messages making all messages configurable. This section is quite straight forward you are able to configure the messages to your liking if there's an internal placeholder I've missed do message me and I will add it!

## Menus
Upon previous requests from my other plugins to make the menus more configurable I've added 'actions' and slot.

#### Actions:
- CLOSE_MENU (will work for all menus)
- OPEN_CATEGORY_MENU (will only work under the categories section in the 'store' menu)
- CONFIRM (will only work under confirmation menus)
- CANCEL (will only work under confirmation menus)

#### Static Sections:
*I've tried making it as configurable as possible however there are some things that I was not able to make completely configurable.*

- 'purchase-history.size' - There is no size variable here due to it being a PageMenu and due to the placements of the page buttons its best to leave it as is however if requested will make it configurable!
- 'store.categories' - This is used to define your categories for the plugin not just the menu.
- 'purchase-history.purchase' - This is used to define the item within the players' history.

## Products
Within this file is where you will create your products for your servers' store! It's important to remember that the categories you create in menus will matter when defining the category for your Product.
```yaml
products:
  seraph:
    name: "Seraph"
    category: "rank" #Category the product gets applied to.
    lore:
      - ""
    material: EMERALD
    slot: 0 #The position the product will be.
    price: 1500
    purchase-commands: #Commands to execute on purchase.
      - "say %player% has purchased Seraph rank!"
    refund-commands:  #Commands to execute on refund.
      - "say %player% has refunded their Seraph rank!"

```
    

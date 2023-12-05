package me.bunnie.bunniecoins.ui;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.store.category.Category;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.ui.action.Action;
import me.bunnie.bunniecoins.utils.Config;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import me.bunnie.bunniecoins.utils.ui.menu.Button;
import me.bunnie.bunniecoins.utils.ui.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopMenu extends Menu {

    private final BCPlugin plugin;
    private final BCPlayer bcPlayer;
    private final Config menusYML;
    private final int size;

    public ShopMenu(int size, Player player) {
        super(size, player);
        this.plugin = BCPlugin.getInstance();
        this.menusYML = plugin.getMenusYML();
        this.size = size;
        bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());

        bcPlayer.loadPurchases();
    }

    @Override
    public String getTitle() {
        return plugin.getMenusYML().getString("store.title")
                .replace("%currency%", plugin.getCurrencyName());
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        String path = "store.buttons";

        for(String s : menusYML.getConfigurationSection(path).getKeys(false)) {
            int slot = menusYML.getInt(path + "." + s + ".slot");
            buttons.put(slot, new Button() {
                @Override
                public ItemStack getItem(Player player) {
                    String name = menusYML.getString(path + "." + s + ".name")
                            .replace("%currency%", plugin.getCurrencyName());
                    List<String> toReplace = menusYML.getStringList(path + "." + s + ".lore");
                    ArrayList<String> lore = new ArrayList<>();
                    for(String s : toReplace) {
                        s = s.replace("%player.balance%", String.valueOf(bcPlayer.getCoins()));
                        s = s.replace("%currency%", plugin.getCurrencyName());
                        lore.add(s);
                    }
                    Material material = Material.valueOf(menusYML.getString(path + "." + s + ".material"));
                    return new ItemBuilder(material)
                            .setName(name)
                            .setLore(lore)
                            .setGlow(menusYML.getBoolean(path + "." + s + ".enchanted"))
                            .build();
                }
            });
        }

        for(Category category : plugin.getShopManager().findAllCategories()) {
            int slot = category.getMenuSlot();
            buttons.put(slot, getCategoryButton(category));
        }

        if(menusYML.getBoolean("store.filler.enabled")) {
            Material material = Material.valueOf(menusYML.getString("store.filler.material"));
            for(int i = 0; i < size; i++) {
                if(!buttons.containsKey(i)) {
                    buttons.put(i, new Button() {
                        @Override
                        public ItemStack getItem(Player player) {
                            return new ItemBuilder(material)
                                    .setName("&r")
                                    .build();
                        }
                    });
                }
            }
        }

        return buttons;
    }

    private Button getCategoryButton(Category category) {
        return new Button() {
            @Override
            public ItemStack getItem(Player player) {
                ArrayList<String> toLore = new ArrayList<>();
                List<Product> products = plugin.getShopManager().findProductsByCategory(category);
                for (String s : category.getDescription()) {
                    s = s.replace("%" + category.getName().toLowerCase() + ".packages%",
                            String.valueOf(products.size()));
                    toLore.add(s);
                }
                return new ItemBuilder(category.getIcon())
                        .setName(category.getDisplayName())
                        .setLore(toLore)
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
                        .setGlow(menusYML.getBoolean("store.categories." + category.getName().toLowerCase() + ".enchanted"))
                        .build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
               Action action = Action.valueOf(menusYML.getString("store.categories." + category.getName().toLowerCase() + ".action"));

                switch (action) {
                    case CLOSE_MENU -> player.closeInventory();
                    case OPEN_CATEGORY_MENU -> {
                        player.closeInventory();
                        new CategoryMenu(size, player, category).open();
                    }
                }
            }
        };
    }
}

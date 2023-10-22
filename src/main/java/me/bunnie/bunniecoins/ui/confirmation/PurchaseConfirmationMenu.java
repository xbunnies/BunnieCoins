package me.bunnie.bunniecoins.ui.confirmation;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.ProductPurchaseEvent;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.ui.action.Action;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import me.bunnie.bunniecoins.utils.ui.menu.Button;
import me.bunnie.bunniecoins.utils.ui.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseConfirmationMenu extends Menu {

    private final BCPlugin plugin = BCPlugin.getInstance();
    private final Product product;
    private final BCPlayer bcPlayer;
    private final int size;

    public PurchaseConfirmationMenu(int size, Player player, Product product) {
        super(product.getName(), size, player);
        this.size = size;
        this.bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
        this.product = product;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        String path = "confirmation.buttons";
        for (String s : plugin.getMenusYML().getConfigurationSection(path).getKeys(false)) {
            if (plugin.getMenusYML().isInt(path  + "." + s + ".slot")) {
                int slot = plugin.getMenusYML().getInt(path  + "." + s + ".slot");
                buttons.put(slot, new Button() {
                    @Override
                    public ItemStack getItem(Player player) {
                        String name = plugin.getMenusYML().getString(path  + "." + s + ".name")
                                .replace("%product.name%", product.getDisplayName());
                        Material material = Material.valueOf(plugin.getMenusYML().getString(path  + "." + s + ".material")
                        .replace("%product.icon%", product.getIcon().name()));
                        List<String> toReplace = plugin.getMenusYML().getStringList(path  + "." + s + ".lore");
                        ArrayList<String> lore = new ArrayList<>();
                        for (String s : toReplace) {
                            s = s.replace("%product.cost%", String.valueOf(product.getCost()));
                            if(s.contains("%product.lore%")) {
                                lore.addAll(product.getDescription());
                            }
                            lore.add(s);

                            if(s.contains("%product.lore%")) {
                                lore.remove(s);
                            }
                        }
                        return new ItemBuilder(material)
                                .setName(name)
                                .setLore(lore)
                                .setGlow(plugin.getMenusYML().getBoolean(path + ".enchanted"))
                                .build();
                    }
                    @Override
                    public void onButtonClick(Player player, int slot, ClickType clickType) {
                        Action action = Action.valueOf(plugin.getMenusYML().getString(path + "." + s + ".action"));

                        switch (action) {
                            case CLOSE_MENU -> player.closeInventory();
                            case CANCEL -> player.closeInventory();
                            case CONFIRM -> {
                                player.closeInventory();
                                plugin.getServer().getPluginManager().callEvent(new ProductPurchaseEvent(player, bcPlayer, product));
                            }
                        }

                    }
                });
            } else if (plugin.getMenusYML().isList(path  + "." + s + ".slot")) {
                List<Integer> slotList = plugin.getMenusYML().getIntegerList(path  + "." + s + ".slot");
                for (int slot : slotList) {
                    buttons.put(slot, new Button() {
                        @Override
                        public ItemStack getItem(Player player) {
                            String name = plugin.getMenusYML().getString(path  + "." + s + ".name")
                                    .replace("%product.name%", product.getDisplayName());
                            Material material = Material.valueOf(plugin.getMenusYML().getString(path  + "." + s + ".material")
                                    .replace("%product.icon%", product.getIcon().name()));
                            List<String> toReplace = plugin.getMenusYML().getStringList(path  + "." + s + ".lore");
                            ArrayList<String> lore = new ArrayList<>();
                            for (String s : toReplace) {
                                s = s.replace("%product.cost%", String.valueOf(product.getCost()));
                                if(s.contains("%product.lore%")) {
                                    lore.addAll(product.getDescription());
                                }
                                lore.add(s);

                                if(s.contains("%product.lore%")) {
                                    lore.remove(s);
                                }
                            }
                            return new ItemBuilder(material)
                                    .setName(name)
                                    .setLore(lore)
                                    .setGlow(plugin.getMenusYML().getBoolean(path + ".enchanted"))
                                    .build();
                        }
                        @Override
                        public void onButtonClick(Player player, int slot, ClickType clickType) {
                            Action action = Action.valueOf(plugin.getMenusYML().getString(path + "." + s + ".action"));

                            switch (action) {
                                case CLOSE_MENU -> player.closeInventory();
                                case CANCEL -> player.closeInventory();
                                case CONFIRM -> {
                                    player.closeInventory();
                                    /*
                                    TODO:
                                    Balance Check
                                    Could do something like fade if user doesnt have enough we display something to buy the coins from the store
                                     */
                                    plugin.getServer().getPluginManager().callEvent(new ProductPurchaseEvent(player, bcPlayer, product));
                                }
                            }
                        }
                    });
                }
            }
        }

        if(plugin.getMenusYML().getBoolean("confirmation.filler.enabled")) {
            Material material = Material.valueOf(plugin.getMenusYML().getString("confirmation.filler.material"));
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

    private void run(Button button, ItemStack toReplace) {

    }


}

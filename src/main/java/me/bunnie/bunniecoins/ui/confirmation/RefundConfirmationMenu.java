package me.bunnie.bunniecoins.ui.confirmation;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.product.ProductRefundEvent;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.ui.action.Action;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import me.bunnie.bunniecoins.utils.ui.menu.Button;
import me.bunnie.bunniecoins.utils.ui.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefundConfirmationMenu extends Menu {

    private final BCPlugin plugin;
    private final Purchase purchase;
    private final Product product;
    private final BCPlayer bcPlayer;
    private final int size;
    private final DecimalFormat decimalFormat;

    public RefundConfirmationMenu(int size, Player player, BCPlayer bcPlayer, Purchase purchase) {
        super(size, player);
        this.plugin = BCPlugin.getInstance();
        this.size = size;
        this.bcPlayer = bcPlayer;
        this.purchase = purchase;
        this.product = purchase.getProduct();
        this.decimalFormat = new DecimalFormat("#,###.#");
    }

    @Override
    public String getTitle() {
        return plugin.getMenusYML().getString("refund-confirmation.title");
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        String path = "refund-confirmation.buttons";
        for (String s : plugin.getMenusYML().getConfigurationSection(path).getKeys(false)) {
            if (plugin.getMenusYML().isInt(path + "." + s + ".slot")) {
                int slot = plugin.getMenusYML().getInt(path + "." + s + ".slot");
                buttons.put(slot, getButton(s));
            } else if (plugin.getMenusYML().isList(path + "." + s + ".slot")) {
                List<Integer> slotList = plugin.getMenusYML().getIntegerList(path + "." + s + ".slot");
                for (int slot : slotList) {
                    buttons.put(slot, getButton(s));
                }
            }
        }

        if (plugin.getMenusYML().getBoolean("confirmation.filler.enabled")) {
            Material material = Material.valueOf(plugin.getMenusYML().getString("confirmation.filler.material"));
            for (int i = 0; i < size; i++) {
                if (!buttons.containsKey(i)) {
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

    private Button getButton(String s) {
        String path = "refund-confirmation.buttons";
        return new Button() {
            @Override
            public ItemStack getItem(Player player) {
                String name = plugin.getMenusYML().getString(path + "." + s + ".name")
                        .replace("%product.name%", product.getDisplayName());
                Material material = Material.valueOf(plugin.getMenusYML().getString(path + "." + s + ".material")
                        .replace("%product.icon%", product.getIcon().name()));
                List<String> toReplace = plugin.getMenusYML().getStringList(path + "." + s + ".lore");
                ArrayList<String> lore = new ArrayList<>();
                for (String s : toReplace) {

                    if(purchase.isPurchasedAtDiscount()) {
                        s = s.replace("%purchase.cost%", String.valueOf(purchase.getProduct().getDiscountedCost()));
                    } else {
                        s = s.replace("%purchase.cost%", String.valueOf(purchase.getCost()));
                    }

                    s = s.replace("%purchase.date%", purchase.getFormattedDate());
                    s = s.replace("%product.cost-formatted%", decimalFormat.format(product.getCost()));
                    s = s.replace("%purchase.date%", purchase.getFormattedDate());
                    if (s.contains("%product.lore%")) {
                        lore.addAll(product.getDescription());
                    }
                    lore.add(s);

                    if (s.contains("%product.lore%")) {
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
                    case CLOSE_MENU, CANCEL -> {
                        player.closeInventory();
                        bcPlayer.getPurchases().clear();
                    }
                    case CONFIRM -> {
                        player.closeInventory();
                        plugin.getServer().getPluginManager().callEvent(new ProductRefundEvent(player, bcPlayer, purchase));
                    }
                }
            }
        };
    }

}

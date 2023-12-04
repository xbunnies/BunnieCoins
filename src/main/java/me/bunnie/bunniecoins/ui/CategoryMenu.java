package me.bunnie.bunniecoins.ui;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.store.category.Category;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.ui.confirmation.PurchaseConfirmationMenu;
import me.bunnie.bunniecoins.ui.insufficient.InsufficientFundsMenu;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import me.bunnie.bunniecoins.utils.ui.menu.Button;
import me.bunnie.bunniecoins.utils.ui.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryMenu extends Menu{

    private final BCPlugin plugin;
    private final BCPlayer bcPlayer;
    private final Category category;
    private final DecimalFormat decimalFormat;
    private final int size;


    public CategoryMenu(int size, Player player, Category category) {
        super(size, player);
        this.size = size;
        this.plugin = BCPlugin.getInstance();
        this.bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
        this.category = category;
        this.decimalFormat = new DecimalFormat("#,###.#");
    }

    @Override
    public String getTitle() {
        return category.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for(Product product : category.getProducts().values()) {
            if(product != null) {
                int slot = product.getMenuSlot();
                buttons.put(slot, getProductButton(product));
            }
        }

        if(plugin.getMenusYML().getBoolean("store.filler.enabled")) {
            Material material = Material.valueOf(plugin.getMenusYML().getString("store.filler.material"));
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

    private Button getProductButton(Product product) {
        return new Button() {
            @Override
            public ItemStack getItem(Player player) {
                List<String> toReplace = product.getDescription();
                ArrayList<String> lore = new ArrayList<>();
                for(String s : toReplace) {
                    s = s.replace("%product.cost%", String.valueOf(product.getCost()));
                    s = s.replace("%product.cost-formatted%", decimalFormat.format(product.getCost()));
                    lore.add(s);
                }
                return new ItemBuilder(product.getIcon())
                        .setName(product.getDisplayName())
                        .setLore(lore)
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                int cost = product.getCost();
                int balance = bcPlayer.getCoins();
                if(balance > cost || balance == cost) {
                    player.closeInventory();
                    new PurchaseConfirmationMenu(plugin.getMenusYML().getInt("purchase-confirmation.size"), player, product).open();
                    return;
                }
                player.closeInventory();
                new InsufficientFundsMenu(player, category, product).open();
            }
        };
    }

}

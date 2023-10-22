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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryMenu extends Menu {

    private final BCPlugin plugin = BCPlugin.getInstance();
    private final BCPlayer bcPlayer;
    private final Category category;
    private final int size;


    public CategoryMenu(int size, Player player, Category category) {
        super(category.getDisplayName(), size, player);
        this.size = size;
        this.bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
        this.category = category;
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
                ArrayList<String> toLore = new ArrayList<>();
                return new ItemBuilder(product.getIcon())
                        .setName(product.getDisplayName())
                        .setLore((ArrayList<String>) product.getDescription())
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                int cost = product.getCost();
                int balance = bcPlayer.getCoins();
                if(balance > cost || balance == cost) {
                    player.closeInventory();
                    new PurchaseConfirmationMenu(plugin.getMenusYML().getInt("confirmation.size"), player, product).open();
                    return;
                }
                player.closeInventory();
                new InsufficientFundsMenu(player, category, product).open();
            }
        };
    }

}

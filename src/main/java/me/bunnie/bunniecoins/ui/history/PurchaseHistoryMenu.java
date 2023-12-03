package me.bunnie.bunniecoins.ui.history;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import me.bunnie.bunniecoins.ui.confirmation.RefundConfirmationMenu;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import me.bunnie.bunniecoins.utils.ui.menu.Button;
import me.bunnie.bunniecoins.utils.ui.menu.page.PageMenu;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseHistoryMenu extends PageMenu {

    private final BCPlayer bcPlayer;
    private final BCPlugin plugin;
    private final OfflinePlayer target;

    public PurchaseHistoryMenu(Player player, OfflinePlayer target) {
        super(36, player);
        this.plugin = BCPlugin.getInstance();
        this.target = target;
        this.bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(target.getUniqueId());

        bcPlayer.loadPurchases();
    }

    private List<Button> getPageableButtons() {
        List<Button> buttons = new ArrayList<>();
        List<Purchase> purchaseList = bcPlayer.getSortedPurchases();
        String path = "purchase-history.buttons.purchase";
        purchaseList.forEach(purchase -> buttons.add(new Button() {
            @Override
            public ItemStack getItem(Player player) {
                String name = plugin.getMenusYML().getString(path + ".name")
                        .replace("%product.name%", purchase.getProduct().getDisplayName());
                Material material = Material.valueOf(plugin.getMenusYML().getString(path + ".material")
                        .replace("%product.icon%", purchase.getProduct().getIcon().name()));
                List<String> toReplace = plugin.getMenusYML().getStringList(path + ".lore");
                ArrayList<String> lore = new ArrayList<>();
                for (String s : toReplace) {
                    s = s.replace("%purchase.id%", purchase.getId());
                    s = s.replace("%purchase.cost%", String.valueOf(purchase.getCost()));
                    s = s.replace("%purchase.date%", purchase.getFormattedDate());
                    s = s.replace("%purchase.refunded%", (purchase.isRefunded() ?
                            plugin.getConfigYML().getString("placeholders.purchase-history.refund-status.true") :
                            plugin.getConfigYML().getString("placeholders.purchase-history.refund-status.false")
                    ));
                    if (s.contains("%product.lore%")) {
                        lore.addAll(purchase.getProduct().getDescription());
                    }
                    lore.add(s);
                    if (s.contains("%product.lore%")) {
                        lore.remove(s);
                    }
                }
                return new ItemBuilder(material)
                        .setName(name)
                        .setLore(lore)
                        .setGlow(purchase.isRefunded())
                        .build();
            }

            @Override
            public void onButtonClick(Player player, int slot, ClickType clickType) {
                if(!purchase.isRefunded())  {
                    player.closeInventory();
                    new RefundConfirmationMenu(BCPlugin.getInstance().getMenusYML().getInt("refund-confirmation.size"), player, bcPlayer, purchase).open();
                    return;
                }
                player.sendMessage(ChatUtils.format("&cThis purchase has already been refunded!"));
            }
        }));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        int counter = 0;

        for(Button button : getPageableButtons()) {
            buttons.put(counter, button);
            counter++;
        }
        return buttons;
    }

    @Override
    public String getTitle() {
        return plugin.getMenusYML().getString("purchase-history.title")
                .replace("%menu.page%", String.valueOf(getPage()))
                .replace("%menu.total-pages%", String.valueOf(getPages()))
                .replace("%player%", target.getName());
    }

}
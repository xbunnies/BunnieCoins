package me.bunnie.bunniecoins.listeners;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.ProductPurchaseEvent;
import me.bunnie.bunniecoins.events.ProductRefundEvent;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PurchaseListener implements Listener {

    private final BCPlugin plugin;

    public PurchaseListener(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPurchase(ProductPurchaseEvent event) {
        Player player = event.getPlayer();
        BCPlayer bcPlayer = event.getBcPlayer();
        Product product = event.getProduct();
        int oldBalance = bcPlayer.getCoins(), newBalance = bcPlayer.getCoins() - product.getCost();
        player.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-purchase.success")
                        .replace("%product.display-name%", product.getDisplayName())
                        .replace("%player.old-balance%", String.valueOf(oldBalance))
                        .replace("%player.balance%", String.valueOf(newBalance))
                        .replace("%prefix%", plugin.getPrefix())
        ));
        for(String command : product.getCommands()) {
            command = command.replace("%player%", player.getName());
            plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }

        bcPlayer.processPurchase(product);
    }

    @EventHandler
    public void onRefund(ProductRefundEvent event) {
        Player player = event.getPlayer();
        BCPlayer bcPlayer = event.getBcPlayer();
        Purchase purchase = event.getPurchase();

        for(String command : purchase.getProduct().getRefundCommands()) {
            command = command.replace("%player%", player.getName());
            plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }

        OfflinePlayer op = Bukkit.getOfflinePlayer(bcPlayer.getUuid());

        player.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-refund.success")
                        .replace("%product.display-name%", purchase.getProduct().getDisplayName())
                        .replace("%player%", op.getName())
        ));
        bcPlayer.refundPurchase(purchase.getId());
        bcPlayer.getPurchases().clear();
    }


}

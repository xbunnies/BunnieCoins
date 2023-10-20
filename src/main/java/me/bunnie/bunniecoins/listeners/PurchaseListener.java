package me.bunnie.bunniecoins.listeners;

import me.bunnie.bunniecoins.events.ProductPurchaseEvent;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PurchaseListener implements Listener {


    @EventHandler
    public void onPurchase(ProductPurchaseEvent event) {
        Player player = event.getPlayer();
        BCPlayer bcPlayer = event.getBcPlayer();
        Product product = event.getProduct();

        player.sendMessage(ChatUtils.format("[*] You have purchased " + product.getDisplayName() + "!"));

        bcPlayer.processPurchase(product);
    }

}

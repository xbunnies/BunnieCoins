package me.bunnie.bunniecoins.events;

import lombok.Getter;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.store.category.product.Product;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ProductRefundEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final BCPlayer bcPlayer;
    private final String purchaseID;

    public ProductRefundEvent(Player player, BCPlayer bcPlayer, String purchaseID) {
        this.player = player;
        this.bcPlayer = bcPlayer;
        this.purchaseID = purchaseID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

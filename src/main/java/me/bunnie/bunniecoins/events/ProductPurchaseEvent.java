package me.bunnie.bunniecoins.events;

import lombok.Getter;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.store.category.product.Product;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ProductPurchaseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final BCPlayer bcPlayer;
    private final Product product;

    public ProductPurchaseEvent(Player player, BCPlayer bcPlayer, Product product) {
        this.player = player;
        this.bcPlayer = bcPlayer;
        this.product = product;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

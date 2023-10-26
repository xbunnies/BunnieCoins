package me.bunnie.bunniecoins.events;

import lombok.Getter;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ProductRefundEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final BCPlayer bcPlayer;
    private final Purchase purchase;

    public ProductRefundEvent(Player player, BCPlayer bcPlayer, Purchase purchase) {
        this.player = player;
        this.bcPlayer = bcPlayer;
        this.purchase = purchase;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

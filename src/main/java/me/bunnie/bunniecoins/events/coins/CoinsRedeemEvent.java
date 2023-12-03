package me.bunnie.bunniecoins.events.coins;

import lombok.Getter;
import me.bunnie.bunniecoins.player.BCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

@Getter
public class CoinsRedeemEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final BCPlayer bcPlayer;
    private final ItemStack itemStack;

    public CoinsRedeemEvent(Player player, BCPlayer bcPlayer, ItemStack itemStack) {
        this.player = player;
        this.bcPlayer = bcPlayer;
        this.itemStack = itemStack;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

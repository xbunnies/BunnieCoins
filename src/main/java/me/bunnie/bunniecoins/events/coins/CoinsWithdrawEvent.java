package me.bunnie.bunniecoins.events.coins;

import lombok.Getter;
import me.bunnie.bunniecoins.player.BCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class CoinsWithdrawEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final BCPlayer bcPlayer;
    private final int amount;


    public CoinsWithdrawEvent(Player player, BCPlayer bcPlayer, int amount) {
        this.player = player;
        this.bcPlayer = bcPlayer;
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

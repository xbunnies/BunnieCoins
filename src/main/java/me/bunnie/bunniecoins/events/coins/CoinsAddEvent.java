package me.bunnie.bunniecoins.events.coins;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class CoinsAddEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final CommandSender sender;
    private final Player player;
    private final int coins;
    private final boolean isDeposit;

    public CoinsAddEvent(CommandSender sender, Player player, int coins, boolean isDeposit) {
        this.sender = sender;
        this.player = player;
        this.coins = coins;
        this.isDeposit = isDeposit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

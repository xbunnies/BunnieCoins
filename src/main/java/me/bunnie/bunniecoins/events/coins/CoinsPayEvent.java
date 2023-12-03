package me.bunnie.bunniecoins.events.coins;

import lombok.Getter;
import me.bunnie.bunniecoins.player.BCPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class CoinsPayEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player, target;
    private final BCPlayer bcPlayer, bcTarget;
    private final int coins;

    public CoinsPayEvent(Player player, Player target, BCPlayer bcPlayer, BCPlayer bcTarget, int coins) {
        this.target = target;
        this.player = player;
        this.bcPlayer = bcPlayer;
        this.bcTarget = bcTarget;
        this.coins = coins;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

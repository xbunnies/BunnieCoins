package me.bunnie.bunniecoins.listeners;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.CoinsAddEvent;
import me.bunnie.bunniecoins.events.CoinsRemoveEvent;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CoinsListener implements Listener {

    private final BCPlugin plugin;

    public CoinsListener(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCoinsAdd(CoinsAddEvent event) {
        Player target = event.getPlayer();
        BCPlayer player = plugin.getBcPlayerManager().findBCPlayerByUUID(target.getUniqueId());

        target.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-coins.receive.target")
                        .replace("%prefix%", plugin.getPrefix())
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%coins%", String.valueOf(event.getCoins()))
                        .replace("%player.old-balance%", String.valueOf(player.getCoins()))
                        .replace("%player.balance%", String.valueOf(player.getCoins() + event.getCoins()))
        ));

        if (event.getSender() != null) {
            CommandSender sender = event.getSender();
            sender.sendMessage(ChatUtils.format(
                    plugin.getConfigYML().getString("messages.on-coins.receive.sender")
                            .replace("%prefix%", plugin.getPrefix())
                            .replace("%coins%", String.valueOf(event.getCoins()))
                            .replace("%currency%", plugin.getCurrencyName())
                            .replace("%player%", target.getName())
            ));
        }

        int balance = player.getCoins();
        int newBalance = balance + event.getCoins();
        player.setCoins(newBalance);
        player.save();
    }

    @EventHandler
    public void onCoinsRemove(CoinsRemoveEvent event) {
        Player target = event.getPlayer();
        BCPlayer player = plugin.getBcPlayerManager().findBCPlayerByUUID(target.getUniqueId());

        target.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-coins.removal.target")
                        .replace("%prefix%", plugin.getPrefix())
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%coins%", String.valueOf(event.getCoins()))
                        .replace("%player.old-balance%", String.valueOf(player.getCoins()))
                        .replace("%player.balance%", String.valueOf(player.getCoins() - event.getCoins()))
        ));

        if (event.getSender() != null) {
            CommandSender sender = event.getSender();
            sender.sendMessage(ChatUtils.format(
                    plugin.getConfigYML().getString("messages.on-coins.removal.sender")
                            .replace("%prefix%", plugin.getPrefix())
                            .replace("%coins%", String.valueOf(event.getCoins()))
                            .replace("%currency%", plugin.getCurrencyName())
                            .replace("%player%", target.getName())
            ));
        }

        int balance = player.getCoins();
        int newBalance = balance - event.getCoins();
        player.setCoins(newBalance);
        player.save();
    }


}

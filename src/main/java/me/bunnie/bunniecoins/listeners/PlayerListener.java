package me.bunnie.bunniecoins.listeners;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.adapters.BCPlayerSQLAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.List;
import java.util.logging.Level;

public class PlayerListener implements Listener {

    private final BCPlugin plugin;

    public PlayerListener(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        switch (plugin.getType()) {
            case "mysql", "sqlite" -> new BCPlayerSQLAdapter(event.getUniqueId());
            default -> plugin.getLogger().log(Level.WARNING,
                    "Player creation has been attempted with an invalid database type!");
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BCPlayer bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
        bcPlayer.save();
        plugin.getBcPlayerManager().removeBCPlayerFromCache(bcPlayer);
    }

    @EventHandler
    public void onServerReload(PluginDisableEvent event) {
        List<BCPlayer> players = plugin.getBcPlayerManager().findAllBCPlayers();
        players.forEach(BCPlayer::save);
    }


}

package me.bunnie.bunniecoins.listeners;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.coins.CoinsRedeemEvent;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.adapters.BCPlayerMongoAdapter;
import me.bunnie.bunniecoins.player.adapters.BCPlayerSQLAdapter;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.UpdateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;

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
            case "mongo", "mongodb" -> new BCPlayerMongoAdapter(event.getUniqueId());
            default -> plugin.getLogger().log(Level.WARNING,
                    "Player creation has been attempted with an invalid database type!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() || player.hasPermission("satchels.commands.admin")) {
            new UpdateUtils(plugin, 113252).getLatestVersion(version -> {
                if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                    player.sendMessage(ChatUtils.format("#ffdadbBunnieCoins is out of date! download the latest version for bug fixes and newly added features! "));
                    player.sendMessage(ChatUtils.format("#c9eff9https://www.spigotmc.org/resources/bunniecoins.113252/"));
                }
            });
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BCPlayer bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
        if (bcPlayer != null) {
            bcPlayer.save();
            plugin.getBcPlayerManager().removeBCPlayerFromCache(bcPlayer);
        }
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            switch (plugin.getType()) {
                case "mysql", "sqlite" -> new BCPlayerSQLAdapter(player.getUniqueId());
                case "mongo", "mongodb" -> new BCPlayerMongoAdapter(player.getUniqueId());
                default -> plugin.getLogger().log(Level.WARNING,
                        "Player creation has been attempted with an invalid database type!");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        BCPlayer bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
        ItemStack itemStack = event.getItem();
        if(itemStack == null) return;

        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            plugin.getServer().getPluginManager().callEvent(new CoinsRedeemEvent(player, bcPlayer, itemStack));
        }


    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        List<BCPlayer> players = plugin.getBcPlayerManager().findAllBCPlayers();
        players.forEach(BCPlayer::save);
    }

}

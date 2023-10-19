package me.bunnie.bunniecoins.listeners;

import me.bunnie.bunniecoins.player.BCPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        new BCPlayer(event.getUniqueId());
    }

}

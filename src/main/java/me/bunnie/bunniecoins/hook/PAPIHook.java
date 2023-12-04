package me.bunnie.bunniecoins.hook;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class PAPIHook extends PlaceholderExpansion {

    private final BCPlugin plugin;

    public PAPIHook(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bunniecoins";
    }

    @Override
    public @NotNull String getAuthor() {
        return "xbunnies";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        BCPlayer bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
        if(bcPlayer == null) return null;

        switch (params.toLowerCase()) {
            case "coins" -> {
                return String.valueOf(bcPlayer.getCoins());
            }
            case "currency" -> {
                return plugin.getCurrencyName();
            }
            case "coins-formatted" -> {
                return new DecimalFormat("#,###.#").format(bcPlayer.getCoins());
            }

        }
        return null;
    }
}

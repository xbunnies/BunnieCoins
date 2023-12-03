package me.bunnie.bunniecoins.commands.player;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.coins.CoinsAddEvent;
import me.bunnie.bunniecoins.events.coins.CoinsPayEvent;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CoinsCommand extends Command {

    private final BCPlugin plugin;

    public CoinsCommand(BCPlugin plugin) {
        super(
                "coins",
                new String[]{},
                "Allows a player to view their coins",
                "bunniecoins.commands.coins"
        );
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            BCPlayer bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
            if (args.length == 0) {
                player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-coins.balance.self")
                        .replace("%player.coins%", String.valueOf(bcPlayer.getCoins()))
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%prefix%", plugin.getPrefix())
                ));
            }
            if (args.length == 1) {
                if (player.hasPermission("bunniecoins.commands.coins-other")) {
                    String playerName = args[0];
                    OfflinePlayer target = Bukkit.getPlayerExact(playerName);
                    if (target == null) return;
                    UUID targetUUID = target.getUniqueId();
                    BCPlayer bcTarget = plugin.getBcPlayerManager().findBCPlayerByUUID(targetUUID);

                    player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-coins.balance.other")
                            .replace("%player.coins%", String.valueOf(bcTarget.getCoins()))
                            .replace("%player%", target.getName())
                            .replace("%currency%", plugin.getCurrencyName())
                            .replace("%prefix%", plugin.getPrefix())
                    ));
                    return;
                }
            }
            if (args.length == 3) {
                if (player.hasPermission("bunniecoins.commands.coins-pay")) {
                    if (args[0].equalsIgnoreCase("pay")) {
                        String targetName = args[1];
                        Player target = Bukkit.getPlayer(targetName);
                        if (target == null) {
                            player.sendMessage(ChatUtils.format("&cPlayer not found!"));
                            return;
                        }
                        try {
                            if(args[2].contains("-")) {
                                sender.sendMessage(ChatUtils.format("&cUnable to pay a negative amount!"));
                                return;
                            }
                            BCPlayer bcTarget = plugin.getBcPlayerManager().findBCPlayerByUUID(target.getUniqueId());
                            int coins = Integer.parseInt(args[2]);
                            plugin.getServer().getPluginManager().callEvent(new CoinsPayEvent(player, target, bcPlayer, bcTarget, coins));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatUtils.format("&cProvided argument &f&o(Coins) &cis not a valid integer."));
                        }
                    }
                }
            }
        } else {
            sender.sendMessage("Only players may execute this command!");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

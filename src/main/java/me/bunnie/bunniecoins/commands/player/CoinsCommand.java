package me.bunnie.bunniecoins.commands.player;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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
            player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-coins.balance")
                    .replace("%player.coins%", String.valueOf(bcPlayer.getCoins()))
                    .replace("%prefix%", plugin.getPrefix())
            ));
            return;
        }
        sender.sendMessage("Only players may execute this command!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

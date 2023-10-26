package me.bunnie.bunniecoins.commands.admin.coins.remove;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.CoinsRemoveEvent;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand extends SubCommand {

    private final BCPlugin plugin;

    public RemoveCommand(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Removes provided amount from players coin balance!";
    }

    @Override
    public String getSyntax() {
        return "/coinsadmin remove <player> <coins>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            List<String> message = new ArrayList<>();
            message.add("&cUh Oh! It appears you have not provided the correct amount of arguments!");
            message.add("&cUsage: &f" + getSyntax());
            message.forEach(msg -> sender.sendMessage(ChatUtils.format(msg)));
            return;
        }
        String playerName = args[1];
        try {
            int coins = Integer.parseInt(args[2]);
            Player op = Bukkit.getPlayerExact(playerName);
            plugin.getServer().getPluginManager().callEvent(new CoinsRemoveEvent(sender, op, coins));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatUtils.format("&cProvided argument &f&o(Coins) &cis not a valid integer."));
        }

    }
}

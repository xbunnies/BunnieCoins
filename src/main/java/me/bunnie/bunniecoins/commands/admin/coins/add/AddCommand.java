package me.bunnie.bunniecoins.commands.admin.coins.add;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.coins.CoinsAddEvent;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddCommand extends SubCommand {

    private final BCPlugin plugin;

    public AddCommand(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Adds provided amount to players coin balance!";
    }

    @Override
    public String getSyntax() {
        return "/coinsadmin add <player> <coins>";
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
            if(args[2].contains("-")) {
                sender.sendMessage(ChatUtils.format("&cUnable to add a negative amount!"));
                return;
            }
            int coins = Integer.parseInt(args[2]);
            Player op = Bukkit.getPlayerExact(playerName);
            plugin.getServer().getPluginManager().callEvent(new CoinsAddEvent(sender, op, coins, true));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatUtils.format("&cProvided argument &f&o(Coins) &cis not a valid integer."));
        }

    }
}

package me.bunnie.bunniecoins.commands.admin;

import me.bunnie.bunniecoins.utils.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CoinsAdminCommand extends Command {

    public CoinsAdminCommand() {
        super(
                "coinsadmin",
                new String[]{},
                "Command to manage players Coins",
                "bunniecoins.commands.coinsadmin"
        );

    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

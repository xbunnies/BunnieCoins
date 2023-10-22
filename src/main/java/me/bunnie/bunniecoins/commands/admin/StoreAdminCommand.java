package me.bunnie.bunniecoins.commands.admin;

import me.bunnie.bunniecoins.utils.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StoreAdminCommand extends Command {

    /*
    /storeadmin close - Closes the server store
    /storeadmin open - Open the server store
    /storeadmin history <player> - opens a menu if the admin clicks on a purchase they have an option to refund
    /storeadmin refund <player> <id> - Refund a players transaction
    /storeadmin reload - Reloads config files
     */

    public StoreAdminCommand() {
        super(
                "storeadmin",
                new String[]{},
                "Command to manage the BunnieCoins store",
                "bunniecoins.commands.storeadmin"
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

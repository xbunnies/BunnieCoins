package me.bunnie.bunniecoins.commands.admin.store;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.commands.admin.store.close.CloseCommand;
import me.bunnie.bunniecoins.commands.admin.store.help.HelpCommand;
import me.bunnie.bunniecoins.commands.admin.store.history.HistoryCommand;
import me.bunnie.bunniecoins.commands.admin.store.open.OpenCommand;
import me.bunnie.bunniecoins.commands.admin.store.reload.ReloadCommand;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.Command;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StoreAdminCommand extends Command {

    private final List<SubCommand> subCommands;

    public StoreAdminCommand(BCPlugin plugin) {
        super(
                "storeadmin",
                new String[]{},
                "Command to manage servers BunnieCoins store",
                "bunniecoins.commands.storeadmin"
        );

        this.subCommands = new ArrayList<>();
        this.subCommands.add(new OpenCommand(plugin));
        this.subCommands.add(new CloseCommand(plugin));
        this.subCommands.add(new HistoryCommand(plugin));
        this.subCommands.add(new ReloadCommand(plugin));
        this.subCommands.add(new HelpCommand(subCommands));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            new HelpCommand(subCommands).execute(sender, args);
        } else {
            boolean validSubCommand = false;
            for (int i = 0; i < subCommands.size(); i++) {
                if (args[0].equalsIgnoreCase(subCommands.get(i).getName())) {
                    validSubCommand = true;
                    subCommands.get(i).execute(sender, args);
                    break;
                }
            }
            if (!validSubCommand) {
                List<String> message = new ArrayList<>();
                message.add("&cUh Oh! You have entered an invalid command!");
                message.add("&cRefer to &f/" + getName() + " help &cfor reference!");
                message.forEach(msg -> sender.sendMessage(ChatUtils.format(msg)));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}


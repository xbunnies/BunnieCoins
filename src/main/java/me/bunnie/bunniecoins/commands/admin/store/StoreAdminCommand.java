package me.bunnie.bunniecoins.commands.admin.store;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.commands.admin.store.close.CloseCommand;
import me.bunnie.bunniecoins.commands.admin.store.help.HelpCommand;
import me.bunnie.bunniecoins.commands.admin.store.history.DepositHistoryCommand;
import me.bunnie.bunniecoins.commands.admin.store.history.PurchaseHistoryCommand;
import me.bunnie.bunniecoins.commands.admin.store.history.WithdrawHistoryCommand;
import me.bunnie.bunniecoins.commands.admin.store.open.OpenCommand;
import me.bunnie.bunniecoins.commands.admin.store.reload.ReloadCommand;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.Command;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        this.subCommands.add(new PurchaseHistoryCommand(plugin));
        this.subCommands.add(new WithdrawHistoryCommand(plugin));
        this.subCommands.add(new DepositHistoryCommand(plugin));
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
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            for (SubCommand command : subCommands) {
                options.add(command.getName());
            }
            String argument = args[0];
            StringUtil.copyPartialMatches(argument, options, completions);
            Collections.sort(completions);
        }

        if (args.length == 2) {
           List<String> cmdList = Arrays.asList("history", "withdraws", "deposits");
            if (cmdList.contains(args[0])) {
                List<String> options = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    options.add(player.getName());
                }
                String argument = args[1];
                StringUtil.copyPartialMatches(argument, options, completions);
                Collections.sort(completions);
            }
        }
        return completions;
    }
}


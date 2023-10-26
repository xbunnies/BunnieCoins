package me.bunnie.bunniecoins.commands.admin.store.close;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

public class CloseCommand extends SubCommand {

    private final BCPlugin plugin;

    public CloseCommand(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "Close the servers' store if opened!";
    }

    @Override
    public String getSyntax() {
        return "/storeadmin close";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!plugin.isStoreOpen()) {
            sender.sendMessage(ChatUtils.format(
                    plugin.getConfigYML().getString("messages.on-store.fail.close")
                            .replace("%prefix%", plugin.getPrefix())
            ));
            return;
        }
        plugin.setStoreStatus(false);
        sender.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-store.status.close")
                        .replace("%prefix%", plugin.getPrefix())
        ));
    }
}

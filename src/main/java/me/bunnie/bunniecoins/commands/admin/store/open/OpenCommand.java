package me.bunnie.bunniecoins.commands.admin.store.open;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

public class OpenCommand extends SubCommand {

    private final BCPlugin plugin;

    public OpenCommand(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public String getDescription() {
        return "Open the servers' store if closed!";
    }

    @Override
    public String getSyntax() {
        return "/storeadmin open";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(plugin.isStoreOpen()) {
            sender.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-store.fail.open")
                    .replace("%prefix%", plugin.getPrefix())
            ));
            return;
        }
        plugin.setStoreStatus(true);
        sender.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-store.status.open")
                .replace("%prefix%", plugin.getPrefix())
        ));
    }
}

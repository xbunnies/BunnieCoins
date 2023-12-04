package me.bunnie.bunniecoins.commands.admin.store.reload;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.Config;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ReloadCommand extends SubCommand {

    private final BCPlugin plugin;

    public ReloadCommand(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload all the plugin configuration files!";
    }

    @Override
    public String getSyntax() {
        return "/storeadmin reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<Config> toReload = Arrays.asList(plugin.getConfigYML(), plugin.getMenusYML(), plugin.getProductsYML());
        toReload.forEach(config -> {
            config.load();
            config.save();
        });
        sender.sendMessage(ChatUtils.format("&aReloaded BunneCoins configuration files!"));
    }
}

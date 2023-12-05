package me.bunnie.bunniecoins.commands.player;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.ui.ShopMenu;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StoreCommand extends Command {

    private final BCPlugin plugin;

    public StoreCommand(BCPlugin plugin) {
        super(
                "store",
                new String[]{"bcstore"},
                "Access the servers in-game premium currency store"
        );
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player player) {
            if(!plugin.isStoreOpen()) {
                player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-store.fail.player-open")
                        .replace("%prefix%", plugin.getPrefix())
                ));
                return;
            }
            if(!plugin.getMenusYML().contains("store.title")) {
                player.sendMessage(ChatUtils.format("&cUnable to process request to open up the ShopMenu. store.title has returned null!"));
                return;
            }
            int size = plugin.getMenusYML().getInt("store.size");
            new ShopMenu(size, player).open();

            return;
        }
        sender.sendMessage("Only players may execute this command!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

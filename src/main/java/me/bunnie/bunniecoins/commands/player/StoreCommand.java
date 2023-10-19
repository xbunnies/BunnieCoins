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
                new String[]{},
                "Access the servers in-game premium currency store"
        );
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player player) {
            if(!plugin.getMenusYML().contains("store.title")) {
                player.sendMessage(ChatUtils.format("&cUnable to process request to open up the ShopMenu. store.title has returned null!"));
                return;
            }
            String title = plugin.getMenusYML().getString("store.title")
                    .replace("%currency%", plugin.getCurrencyName());
            int size = plugin.getMenusYML().getInt("store.size");
            new ShopMenu(title, size, player).open();
            return;
        }
        sender.sendMessage("Only players may execute this command!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

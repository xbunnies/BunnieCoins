package me.bunnie.bunniecoins.commands.admin.store.history;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.ui.history.PurchaseHistoryMenu;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryCommand extends SubCommand {

    private final BCPlugin plugin;

    public PurchaseHistoryCommand(BCPlugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return "history";
    }

    @Override
    public String getDescription() {
        return "Open the purchase history of the provided player!";
    }

    @Override
    public String getSyntax() {
        return "/storeadmin history <player>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if(plugin.getShopManager().findAllProducts().size() == 0) {
                player.sendMessage(ChatUtils.format("&cNo Products Found."));
                return;
            }

            if(args.length < 2) {
                List<String> message = new ArrayList<>();
                message.add("&cUh Oh! It appears you have not provided the correct amount of arguments!");
                message.add("&cUsage: &f" + getSyntax());
                message.forEach(msg -> sender.sendMessage(ChatUtils.format(msg)));
                return;
            }
            String playerName = args[1];
            OfflinePlayer target = Bukkit.getPlayerExact(playerName);

            player.sendMessage(ChatUtils.format(
                    plugin.getConfigYML().getString("messages.on-history.load")
                            .replace("%history.type%", plugin.getConfigYML().getString("placeholders.history-types.purchase"))
                            .replace("%player%", target.getName())
                            .replace("%prefix%", plugin.getPrefix())
            ));


            PurchaseHistoryMenu his = new PurchaseHistoryMenu(player, target);
            new BukkitRunnable() {
                @Override
                public void run() {
                    his.open();
                }
            }.runTaskLater(plugin, 20L * 3);
        }
    }

}

package me.bunnie.bunniecoins.commands.admin.store.history;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.ui.history.PurchaseHistoryMenu;
import me.bunnie.bunniecoins.ui.history.WithdrawHistoryMenu;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WithdrawHistoryCommand extends SubCommand {

    private final BCPlugin plugin;

    public WithdrawHistoryCommand(BCPlugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return "withdraws";
    }

    @Override
    public String getDescription() {
        return "Open the withdraw history of the provided player!";
    }

    @Override
    public String getSyntax() {
        return "/storeadmin withdraws <player>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
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
                            .replace("%history.type%", plugin.getConfigYML().getString("placeholders.history-types.withdraw"))
                            .replace("%player%", target.getName())
                            .replace("%prefix%", plugin.getPrefix())
            ));


            WithdrawHistoryMenu his = new WithdrawHistoryMenu(player, target);
            new BukkitRunnable() {
                @Override
                public void run() {
                    his.open();
                }
            }.runTaskLater(plugin, 20L * 3);
        }
    }

}

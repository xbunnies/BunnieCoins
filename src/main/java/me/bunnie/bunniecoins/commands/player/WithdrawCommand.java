package me.bunnie.bunniecoins.commands.player;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.coins.CoinsWithdrawEvent;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WithdrawCommand extends Command {

    private final BCPlugin plugin;

    public WithdrawCommand(BCPlugin plugin) {
        super(
                "withdraw",
                new String[]{"withdrawcoins"},
                "Withdraw provided amount to redeemable note!",
                "bunniecoins.commands.withdraw"
        );
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof Player player) {
            if(args.length == 0) {
                List<String> message = new ArrayList<>();
                message.add("&cUh Oh! It appears you have not provided the correct amount of arguments!");
                message.add("&cUsage: &f/withdraw <amount>");
                message.forEach(msg -> sender.sendMessage(ChatUtils.format(msg)));
                return;
            }
            if(args.length == 1) {
                try {
                    if(args[0].contains("-")) {
                        player.sendMessage(ChatUtils.format("&cUnable to withdraw a negative amount!"));
                        return;
                    }
                    int coins = Integer.parseInt(args[0]);
                    BCPlayer bcPlayer = plugin.getBcPlayerManager().findBCPlayerByUUID(player.getUniqueId());
                    plugin.getServer().getPluginManager().callEvent(new CoinsWithdrawEvent(player, bcPlayer, coins));
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatUtils.format("&cProvided argument &f&o(Coins) &cis not a valid integer."));
                }
            }
            return;
        }
        sender.sendMessage("Only players may execute this command!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

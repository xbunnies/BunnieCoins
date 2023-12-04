package me.bunnie.bunniecoins.listeners;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.events.coins.*;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.deposit.Deposit;
import me.bunnie.bunniecoins.player.withdraw.Withdraw;
import me.bunnie.bunniecoins.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class CoinsListener implements Listener {

    private final BCPlugin plugin;

    public CoinsListener(BCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCoinsAdd(CoinsAddEvent event) {
        Player target = event.getPlayer();
        BCPlayer player = plugin.getBcPlayerManager().findBCPlayerByUUID(target.getUniqueId());

        target.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-coins.receive.target")
                        .replace("%prefix%", plugin.getPrefix())
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%coins%", String.valueOf(event.getCoins()))
                        .replace("%player.old-balance%", String.valueOf(player.getCoins()))
                        .replace("%player.balance%", String.valueOf(player.getCoins() + event.getCoins()))
        ));

        CommandSender sender = event.getSender();
        sender.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-coins.receive.sender")
                        .replace("%prefix%", plugin.getPrefix())
                        .replace("%coins%", String.valueOf(event.getCoins()))
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%player%", target.getName())
        ));

        int balance = player.getCoins();
        int newBalance = balance + event.getCoins();

        if(event.isDeposit()) {
            String withdrawer;
            if(sender instanceof ConsoleCommandSender) {
                withdrawer = "CONSOLE";
            } else {
                withdrawer = ((Player) sender).getUniqueId().toString();
            }
            Deposit deposit = new Deposit(UUID.randomUUID(), player.getUuid(), withdrawer, event.getCoins());
            player.createDeposit(deposit);
        }
        player.setCoins(newBalance);
        player.save();
    }

    @EventHandler
    public void onCoinsPay(CoinsPayEvent event) {
        Player player = event.getPlayer();
        Player target = event.getTarget();
        BCPlayer bcPlayer = event.getBcPlayer();
        BCPlayer bcTarget = event.getBcTarget();
        int coins = event.getCoins();

        if(player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-coins.pay.self")
                    .replace("%currency%", plugin.getCurrencyName())
                    .replace("%prefix%", plugin.getPrefix())));
            return;
        }

        player.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-coins.pay.sender")
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%coins%", String.valueOf(event.getCoins()))
                        .replace("%target%", target.getName())
                        .replace("%target.balance%", String.valueOf(bcTarget.getCoins()))
                        .replace("%player.balance%", String.valueOf(bcPlayer.getCoins()))
                        .replace("%prefix%", plugin.getPrefix())
        ));

        target.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-coins.pay.target")
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%coins%", String.valueOf(event.getCoins()))
                        .replace("%sender%", player.getName())
                        .replace("%player.balance%", String.valueOf(bcTarget.getCoins()))
                        .replace("%sender.balance%", String.valueOf(bcPlayer.getCoins()))
                        .replace("%prefix%", plugin.getPrefix())
        ));

        bcPlayer.setCoins(bcPlayer.getCoins() - coins);
        bcTarget.setCoins(bcTarget.getCoins() + coins);
        bcPlayer.save();
        bcTarget.save();
    }

    @EventHandler
    public void onCoinsRemove(CoinsRemoveEvent event) {
        Player target = event.getPlayer();
        BCPlayer player = plugin.getBcPlayerManager().findBCPlayerByUUID(target.getUniqueId());

        target.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-coins.removal.target")
                        .replace("%prefix%", plugin.getPrefix())
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%coins%", String.valueOf(event.getCoins()))
                        .replace("%player.old-balance%", String.valueOf(player.getCoins()))
                        .replace("%player.balance%", String.valueOf(player.getCoins() - event.getCoins()))
        ));

        CommandSender sender = event.getSender();
        sender.sendMessage(ChatUtils.format(
                plugin.getConfigYML().getString("messages.on-coins.removal.sender")
                        .replace("%prefix%", plugin.getPrefix())
                        .replace("%coins%", String.valueOf(event.getCoins()))
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%player%", target.getName())
        ));

        int balance = player.getCoins();
        int newBalance = balance - event.getCoins();
        player.setCoins(newBalance);
        player.save();
    }

    @EventHandler
    public void onWithdraw(CoinsWithdrawEvent event) {
        Player player = event.getPlayer();
        BCPlayer bcPlayer = event.getBcPlayer();
        int amount = event.getAmount();
        int balance = bcPlayer.getCoins();

        Withdraw withdraw = new Withdraw(UUID.randomUUID(), player.getUniqueId(), amount);

        bcPlayer.createWithdrawal(withdraw);

        if(balance < amount) {
            player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-withdraw.invalid")
                    .replace("%coins%", plugin.getCurrencyName())
                    .replace("%currency%", plugin.getCurrencyName())
                    .replace("%prefix%", plugin.getPrefix())));
            return;
        }

        int newBalance = balance - amount;
        bcPlayer.setCoins(newBalance);
        bcPlayer.save();

        player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-withdraw.success")
                .replace("%coins%", String.valueOf(amount))
                .replace("%currency%", plugin.getCurrencyName())
                .replace("%prefix%", plugin.getPrefix())));

        player.getInventory().addItem(withdraw.getItem());
    }

    @EventHandler
    public void onRedeem(CoinsRedeemEvent event) {
        Player player = event.getPlayer();
        BCPlayer bcPlayer = event.getBcPlayer();
        ItemStack itemStack = event.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) return;
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

        String noteID = pdc.get(new NamespacedKey(plugin, "noteID"), PersistentDataType.STRING);

        if (noteID != null) {
            Withdraw withdraw = bcPlayer.loadWithdraw(UUID.fromString(noteID));

            if (withdraw == null) {
                player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-redeem.not-found")
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%prefix%", plugin.getPrefix())));
                return;
            }

            if (withdraw.isWithdrawn()) {
                player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-redeem.already-redeemed")
                        .replace("%currency%", plugin.getCurrencyName())
                        .replace("%prefix%", plugin.getPrefix())));

                for (Player notify : Bukkit.getOnlinePlayers()) {
                    if (notify.hasPermission("bunniecoins.notify")) {
                        notify.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-redeem.notify")
                                .replace("%currency%", plugin.getCurrencyName())
                                .replace("%player%", player.getName())
                                .replace("%prefix%", plugin.getPrefix())));
                    }
                }
                return;
            }

            int balance = bcPlayer.getCoins();
            int newBalance = balance + withdraw.getAmount();

            bcPlayer.setCoins(newBalance);
            bcPlayer.save();

            player.sendMessage(ChatUtils.format(plugin.getConfigYML().getString("messages.on-redeem.success")
                    .replace("%currency%", plugin.getCurrencyName())
                    .replace("%coins%", String.valueOf(withdraw.getAmount()))
                    .replace("%player.old-balance%", String.valueOf(balance))
                    .replace("%player.new-balance%", String.valueOf(newBalance))
                    .replace("%prefix%", plugin.getPrefix())));

            withdraw.setWithdrawn(true);
            bcPlayer.saveWithdrawal(withdraw);

            player.getInventory().remove(itemStack);
        }
    }
}

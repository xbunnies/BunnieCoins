package me.bunnie.bunniecoins.player.deposit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Deposit {

    private final UUID id, playerUUID;
    private final String withdrawer;
    private final int amount;
    private long depositedAt;

    public Deposit(UUID id, UUID playerUUID, String withdrawer, int amount) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.withdrawer = withdrawer;
        this.amount = amount;
        this.depositedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getWithdrawer() {
        return withdrawer;
    }

    public int getAmount() {
        return amount;
    }

    public long getDepositedAt() {
        return depositedAt;
    }

    public void setDepositedAt(long depositedAt) {
        this.depositedAt = depositedAt;
    }

    /**
     * Get the formatted purchase date as a readable string.
     *
     * @return A formatted date string.
     */
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(this.depositedAt);
        return dateFormat.format(date);
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(getPlayerUUID());
    }
}

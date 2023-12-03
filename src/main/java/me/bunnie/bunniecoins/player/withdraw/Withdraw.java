package me.bunnie.bunniecoins.player.withdraw;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.utils.Config;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Withdraw {

    private final UUID id;
    private final UUID playerUUID;
    private final int amount;
    private long withdrewAt;
    private boolean withdrawn;

    public Withdraw(UUID id, UUID playerUUID, int amount) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.amount = amount;
        this.withdrewAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public long getWithdrewAt() {
        return withdrewAt;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public boolean isWithdrawn() {
        return withdrawn;
    }

    public ItemStack getItem() {
        Config config = BCPlugin.getInstance().getConfigYML();
        List<String> toReplace = config.getStringList("settings.withdraw-note.lore");
        ArrayList<String> lore = new ArrayList<>();
        for(String s : toReplace) {
            s = s.replace("%currency%", BCPlugin.getInstance().getCurrencyName());
            s = s.replace("%coins%", String.valueOf(amount));
            s = s.replace("%player%", getWithdrawer().getName());
            s = s.replace("%withdraw.date%", getFormattedDate());
            lore.add(s);
        }
        return new ItemBuilder(Material.valueOf(config.getString("settings.withdraw-note.material")))
                .setName(config.getString("settings.withdraw-note.name")
                        .replace("%coins%", String.valueOf(amount))
                        .replace("%currency%", BCPlugin.getInstance().getCurrencyName()))
                .setLore(lore)
                .addPDC("noteID", PersistentDataType.STRING, id.toString())
                .addPDC("noteAmount", PersistentDataType.INTEGER, amount)
                .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                .addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
                .setGlow(config.getBoolean("settings.withdraw-note.enchanted"))
                .build();
    }

    public void setWithdrewAt(long withdrewAt) {
        this.withdrewAt = withdrewAt;
    }

    public void setWithdrawn(boolean withdrawn) {
        this.withdrawn = withdrawn;
    }

    /**
     * Get the formatted purchase date as a readable string.
     *
     * @return A formatted date string.
     */
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(this.withdrewAt);
        return dateFormat.format(date);
    }

    public OfflinePlayer getWithdrawer() {
        return Bukkit.getPlayer(getPlayerUUID());
    }
}


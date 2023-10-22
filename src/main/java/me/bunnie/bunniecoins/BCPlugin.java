package me.bunnie.bunniecoins;

import lombok.Getter;
import me.bunnie.bunniecoins.commands.admin.CoinsAdminCommand;
import me.bunnie.bunniecoins.commands.player.StoreCommand;
import me.bunnie.bunniecoins.database.SQLManager;
import me.bunnie.bunniecoins.listeners.CoinsListener;
import me.bunnie.bunniecoins.listeners.PlayerListener;
import me.bunnie.bunniecoins.listeners.PurchaseListener;
import me.bunnie.bunniecoins.player.BCPlayerManager;
import me.bunnie.bunniecoins.store.ShopManager;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.Config;
import me.bunnie.bunniecoins.utils.ui.listener.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class BCPlugin extends JavaPlugin {

    @Getter private static BCPlugin instance;
    @Getter private Config configYML, menusYML, productsYML;
    @Getter private SQLManager sqlManager;
    @Getter private ShopManager shopManager;
    @Getter private BCPlayerManager bcPlayerManager;

    @Override
    public void onEnable() {
        instance = this;
        this.registerConfigurations();
        this.registerManagers();
        this.registerListeners();
        this.registerCommands();
    }

    @Override
    public void onDisable() {

        switch (getType()) {
            case "mysql", "sqlite" -> {
                sqlManager.disconnect();
            }
            default -> getLogger().log(Level.WARNING, "Unknown database type Player data will NOT save and will cause errors.");
        }

        instance = null;
    }

    private void registerConfigurations() {
        this.configYML = new Config(this, "config", getDataFolder().getAbsolutePath());
        this.menusYML = new Config(this, "menus", getDataFolder().getAbsolutePath());
        this.productsYML = new Config(this, "products", getDataFolder().getAbsolutePath());
    }

    private void registerManagers() {
       shopManager = new ShopManager(this);
       bcPlayerManager = new BCPlayerManager();

       switch (getType()) {
           case "mysql", "sqlite" -> {
               sqlManager = new SQLManager(this);
               getLogger().log(Level.INFO, "Using " + getType() + " as database! (SQLAdapter)");
           }
           default -> getLogger().log(Level.WARNING, "Unknown database type Player data will NOT save and will cause errors.");
       }

    }

    private void registerListeners() {
        Arrays.asList(new PlayerListener(this), new CoinsListener(this),
                new PurchaseListener(), new MenuListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        new CoinsAdminCommand(this);
        new StoreCommand(this);
    }

    public String getPrefix() {
        return ChatUtils.format(configYML.getString("settings.prefix"));
    }

    public String getCurrencyName() {
        return configYML.getString("settings.currency-name");
    }

    public String getType() {
        return configYML.getString("settings.database.type").toLowerCase();
    }

}

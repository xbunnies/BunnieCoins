package me.bunnie.bunniecoins;

import lombok.Getter;
import me.bunnie.bunniecoins.commands.admin.coins.CoinsAdminCommand;
import me.bunnie.bunniecoins.commands.admin.store.StoreAdminCommand;
import me.bunnie.bunniecoins.commands.player.CoinsCommand;
import me.bunnie.bunniecoins.commands.player.StoreCommand;
import me.bunnie.bunniecoins.commands.player.WithdrawCommand;
import me.bunnie.bunniecoins.database.MongoManager;
import me.bunnie.bunniecoins.database.SQLManager;
import me.bunnie.bunniecoins.hook.PAPIHook;
import me.bunnie.bunniecoins.listeners.CoinsListener;
import me.bunnie.bunniecoins.listeners.PlayerListener;
import me.bunnie.bunniecoins.listeners.PurchaseListener;
import me.bunnie.bunniecoins.player.BCPlayerManager;
import me.bunnie.bunniecoins.store.ShopManager;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.Config;
import me.bunnie.bunniecoins.utils.Metrics;
import me.bunnie.bunniecoins.utils.UpdateUtils;
import me.bunnie.bunniecoins.utils.ui.listener.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class BCPlugin extends JavaPlugin {

    @Getter private static BCPlugin instance;
    @Getter private Config configYML, menusYML, productsYML;
    @Getter private SQLManager sqlManager;
    @Getter private MongoManager mongoManager;
    @Getter private ShopManager shopManager;
    @Getter private BCPlayerManager bcPlayerManager;

    @Override
    public void onEnable() {
        instance = this;
        this.registerConfigurations();
        this.registerManagers();
        this.registerListeners();
        this.registerCommands();

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPIHook(this).register();
        }

        this.init();
    }

    @Override
    public void onDisable() {
        switch (getType()) {
            case "mysql", "sqlite" -> sqlManager.disconnect();
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

           case "mongo", "mongodb" -> {
               mongoManager = new MongoManager(this);
               getLogger().log(Level.INFO, "Using " + getType() + " as database! (MongoAdapter)");
           }

           default -> getLogger().log(Level.WARNING, "Unknown database type Player and Purchase data will NOT save and will cause errors.");
       }

    }

    private void registerListeners() {
        Arrays.asList(new PlayerListener(this), new CoinsListener(this),
                new PurchaseListener(this), new MenuListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        new CoinsCommand(this);
        new StoreCommand(this);
        new WithdrawCommand(this);

        new CoinsAdminCommand(this);
        new StoreAdminCommand(this);
    }

    private void init() {
        new UpdateUtils(this, 113252).getLatestVersion(version -> {
            if(!getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().log(Level.SEVERE, "You are using an outdated version! (" + getDescription().getVersion() + ")");
                getLogger().log(Level.SEVERE, "Please update to the newest version for bug patches and newly added features! (" + version + ")" );
            }
        });
        new Metrics(this, 20446);
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

    public boolean isStoreOpen() {
        return configYML.getBoolean("settings.store-open");
    }

    public void setStoreStatus(boolean status) {
        configYML.set("settings.store-open", status);
        configYML.save();
    }

}

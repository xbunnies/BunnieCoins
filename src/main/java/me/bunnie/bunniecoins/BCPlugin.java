package me.bunnie.bunniecoins;

import lombok.Getter;
import me.bunnie.bunniecoins.commands.player.StoreCommand;
import me.bunnie.bunniecoins.listeners.PlayerListener;
import me.bunnie.bunniecoins.player.BCManager;
import me.bunnie.bunniecoins.store.ShopManager;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.Config;
import me.bunnie.bunniecoins.utils.ui.listener.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class BCPlugin extends JavaPlugin {

    @Getter private static BCPlugin instance;
    @Getter private Config configYML, menusYML, productsYML;
    @Getter private ShopManager shopManager;
    @Getter private BCManager bcManager;

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
        instance = null;
    }

    private void registerConfigurations() {
        this.configYML = new Config(this, "config", getDataFolder().getAbsolutePath());
        this.menusYML = new Config(this, "menus", getDataFolder().getAbsolutePath());
        this.productsYML = new Config(this, "products", getDataFolder().getAbsolutePath());
    }

    private void registerManagers() {
       shopManager = new ShopManager(this);
       bcManager = new BCManager();
    }

    private void registerListeners() {
        Arrays.asList(new PlayerListener(), new MenuListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        new StoreCommand(this);
    }

    public String getPrefix() {
        return ChatUtils.format(configYML.getString("settings.prefix"));
    }

    public String getCurrencyName() {
        return ChatUtils.format(configYML.getString("settings.currency_name"));
    }

}

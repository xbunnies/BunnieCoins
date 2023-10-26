package me.bunnie.bunniecoins.utils.ui.menu;

import lombok.Getter;
import me.bunnie.bunniecoins.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class Menu {

    @Getter private static final Map<UUID, Menu> menuMap = new HashMap<>();

    private final int size;
    private final Player player;
    private Inventory inventory;

    public Menu(int size, Player player) {
        this.size = size;
        this.player = player;
    }

    public abstract String getTitle();

    public abstract Map<Integer, Button> getButtons();

    public void createInventory() {
        Map<Integer, Button> buttonMap = getButtons();

        String title = getTitle();
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        inventory = Bukkit.createInventory(player, size, ChatUtils.format(title));
        Menu previousMenu = menuMap.get(player.getUniqueId());

        if (previousMenu == null) {
            player.closeInventory();
        } else {
            int previousSize = player.getOpenInventory().getTopInventory().getSize();
            String previousTitle = player.getOpenInventory().getTitle();

            if (previousSize == size && previousTitle.equals(title)) {
                inventory = player.getOpenInventory().getTopInventory();
            } else {
                player.closeInventory();
            }
        }

        for (Map.Entry<Integer, Button> buttonEntry : buttonMap.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getItem(player));
        }

        player.openInventory(inventory);
    }


    public void open() {
        this.createInventory();
        menuMap.put(player.getUniqueId(), this);
    }


    public void close() {
        menuMap.remove(player.getUniqueId());
    }

    public Inventory getInventory() {
        return inventory;
    }


}
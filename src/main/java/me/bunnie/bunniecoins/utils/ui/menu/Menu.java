package me.bunnie.bunniecoins.utils.ui.menu;

import lombok.Getter;
import me.bunnie.bunniecoins.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Menu {

    @Getter private static final Map<UUID, Menu> menuMap = new HashMap<>();

    private final String name;
    private final int size;
    private final Player player;
    private Inventory inventory;

    public Menu(String name, int size, Player player) {
        this.name = name;
        this.size = size;
        this.player = player;

    }

    public abstract Map<Integer, Button> getButtons();

    private void createInventory() {
        Map<Integer, Button> buttonMap = getButtons();
        int inventorySize = size == -1 ? this.calculateSize() : size;

        String title = name;
        if(title == null) {
            title = "No Title Provided!";
        }
        if(title.length() > 32) {
            title = title.substring(0, 32);
        }

        inventory = Bukkit.createInventory(player, inventorySize, ChatUtils.format(title));

        for (Map.Entry<Integer, Button> buttonEntry : buttonMap.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getItem(player));
        }

        player.openInventory(inventory);
    }

    public void open() {
        this.createInventory();
        menuMap.put(player.getUniqueId(), this);
    }


    public void onClose() {
        menuMap.remove(player.getUniqueId());
    }

    private int calculateSize() {
        int highest = 0;

        for (int buttonValue : getButtons().keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public Inventory getInventory() {
        return inventory;
    }

}
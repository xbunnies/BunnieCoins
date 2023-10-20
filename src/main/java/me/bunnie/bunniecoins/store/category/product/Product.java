package me.bunnie.bunniecoins.store.category.product;

import lombok.Data;
import me.bunnie.bunniecoins.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
public class Product {

    private final String name;
    private String displayName;
    private List<String> description;
    private List<String> commands;
    private Material icon;
    private int cost;
    private int menuSlot;

    public Product(String name) {
        this.name = name;
        this.displayName = ChatColor.GRAY + name;
        this.description = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.icon = Material.BOOK;
        this.cost = 500;
        this.menuSlot = 0;
    }
}
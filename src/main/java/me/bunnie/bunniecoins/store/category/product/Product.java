package me.bunnie.bunniecoins.store.category.product;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Data
public class Product {

    private final String name;
    private String displayName, previousName;
    private List<String> description;
    private List<String> commands, refundCommands;
    private Material icon;
    private int cost, menuSlot, discountAmount;
    private boolean multi, discountingPrevious;

    public Product(String name) {
        this.name = name;
        this.displayName = ChatColor.GRAY + name;
        this.description = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.refundCommands = new ArrayList<>();
        this.icon = Material.BOOK;
        this.cost = 500;
        this.menuSlot = 0;
        this.multi = false;

        this.discountingPrevious = false;
        this.previousName = "";
        this.discountAmount = 0;
    }

    public int getDiscountedCost() {
        int cost = getCost();
        int discountPercentage = getDiscountAmount();
        int discount = (cost * discountPercentage) / 100;
        return cost - discount;
    }

}
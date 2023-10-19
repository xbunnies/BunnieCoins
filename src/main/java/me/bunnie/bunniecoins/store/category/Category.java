package me.bunnie.bunniecoins.store.category;

import lombok.Data;
import me.bunnie.bunniecoins.store.category.product.Product;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Category {

    private String name, displayName;
    private List<String> description;
    private Map<String, Product> products;
    private Material icon;
    private int menuSlot;

    public Category(String name) {
        this.name = name;
        this.displayName = "&d" + name;
        this.description = new ArrayList<>();
        this.products = new HashMap<>();
        this.icon = Material.BOOK;
        this.menuSlot = 0;
    }

    public Product findProductByName(String s) {
        return products
                .values()
                .stream()
                .filter(product -> product.getName().equalsIgnoreCase(s))
                .findFirst()
                .orElse(null);
    }

}
package me.bunnie.bunniecoins.store;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.store.category.Category;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.Config;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopManager {

    private final BCPlugin plugin;
    private final Config configYML, menusYML;
    private Map<String, Category> categoryMap;

    public ShopManager(BCPlugin plugin) {
        this.plugin = plugin;
        this.configYML = plugin.getConfigYML();
        this.menusYML = plugin.getMenusYML();
        this.categoryMap = new HashMap<>();

        this.setupShop();
    }


    private void setupShop() {
        Map<String, Product> productMap = new HashMap<>();
        String path = "store.categories";
        for (String s : menusYML.getConfigurationSection(path).getKeys(false)) {
            String categoryName = ChatUtils.fixCapitalisation(s);
            String displayName = menusYML.getString(path + "." + s + ".name");
            List<String> description = menusYML.getStringList(path + "." + s + ".lore");
            Material material = Material.valueOf(menusYML.getString(path + "." + s + ".material"));
            int menuSlot = menusYML.getInt(path + "." + s + ".slot");

            Category category = new Category(categoryName);
            category.setDisplayName(displayName);
            category.setDescription(description);
            category.setIcon(material);
            category.setMenuSlot(menuSlot);

            if (categoryMap.containsKey(categoryName)) {
                continue;
            }
            categoryMap.put(categoryName, category);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String s : menusYML.getConfigurationSection("products").getKeys(false)) {
                    String productName = ChatUtils.fixCapitalisation(s);
                    String categoryName = menusYML.getString("products." + s + ".category");
                    List<String> description = menusYML.getStringList("products." + s + ".lore");
                    List<String> commands = menusYML.getStringList("products." + s + ".commands");
                    Material material = Material.valueOf(menusYML.getString("products." + s + ".material"));
                    int price = menusYML.getInt("products." + s + ".price");
                    int menuSlot = menusYML.getInt("products." + s + ".slot");

                    Product product = new Product(productName);
                    product.setDescription(description);
                    product.setCommands(commands);
                    product.setIcon(material);
                    product.setCost(price);
                    product.setMenuSlot(menuSlot);

                    Category category = categoryMap.get(ChatUtils.fixCapitalisation(categoryName));
                    if(category == null) {
                        continue;
                    }
                    //product.setCategory(category);
                    if (productMap.containsKey(productName)) {
                        continue;
                    }
                    productMap.put(productName, product);
                    category.setProducts(productMap);
                }
            }
        }.runTaskLater(plugin, 20 * 5);

    }

    public List<Category> findAllCategories() {
        return categoryMap
                .values()
                .stream()
                .toList();
    }

    public List<Product> findProductsByCategory(Category category) {
        return category.getProducts().values().stream().toList();
    }

}

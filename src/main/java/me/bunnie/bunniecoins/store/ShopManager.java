package me.bunnie.bunniecoins.store;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.store.category.Category;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.utils.ChatUtils;
import me.bunnie.bunniecoins.utils.Config;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ShopManager {

    private final BCPlugin plugin;
    private final Config menusYML, productsYML;
    private final Map<String, Category> categoryMap;

    public ShopManager(BCPlugin plugin) {
        this.plugin = plugin;
        this.menusYML = plugin.getMenusYML();
        this.productsYML = plugin.getProductsYML();
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
        plugin.getLogger().log(Level.INFO, "Loaded " + categoryMap.size() + " categories!");

        plugin.getLogger().log(Level.INFO, "Loading Products...");
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String s : productsYML.getConfigurationSection("products").getKeys(false)) {
                    String productName = ChatUtils.fixCapitalisation(s);
                    String displayName = productsYML.getString("products." + s  +".name");
                    String categoryName = productsYML.getString("products." + s + ".category");
                    List<String> description = productsYML.getStringList("products." + s + ".lore");
                    List<String> commands = productsYML.getStringList("products." + s + ".purchase-commands");
                    List<String> refundCommands = productsYML.getStringList("products." + s + ".refund-commands");
                    Material material = Material.valueOf(productsYML.getString("products." + s + ".material"));
                    int price = productsYML.getInt("products." + s + ".price");
                    int menuSlot = productsYML.getInt("products." + s + ".slot");

                    Product product = new Product(productName);
                    product.setDisplayName(displayName);
                    product.setDescription(description);
                    product.setCommands(commands);
                    product.setRefundCommands(refundCommands);
                    product.setIcon(material);
                    product.setCost(price);
                    product.setMenuSlot(menuSlot);

                    if(categoryName == null) continue;
                    Category category = categoryMap.get(ChatUtils.fixCapitalisation(categoryName));
                    if(category == null) {
                        continue;
                    }
                    if (productMap.containsKey(productName)) {
                        continue;
                    }
                    productMap.put(productName, product);
                    category.setProducts(productMap);

                    plugin.getLogger().log(Level.INFO, "Loaded " + productMap.size() + " products in " + category.getName() + "!");
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
    public List<Product> findAllProducts() {
        List<Product> toReturn = new ArrayList<>();
        for(Category category : findAllCategories()) {
            toReturn.addAll(category.getProducts().values());
        }
        return toReturn;
    }

    public Product findProductByName(String name) {
        Product product = null;
        for(Category category : categoryMap.values()) {
            for(Product products : category.getProducts().values()) {
                if(products.getName().equalsIgnoreCase(name)) {
                    product = products;
                    break;
                }
            }
        }
        return product;
    }

}

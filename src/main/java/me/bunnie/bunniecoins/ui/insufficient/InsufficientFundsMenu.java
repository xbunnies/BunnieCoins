package me.bunnie.bunniecoins.ui.insufficient;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.store.category.Category;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.utils.ui.menu.BookMenu;
import org.bukkit.entity.Player;

public class InsufficientFundsMenu extends BookMenu {

    private final Category category;
    private final Product product;

    public InsufficientFundsMenu(Player player, Category category, Product product) {
        super("Insufficient Funds", "Server", player);
        this.category = category;
        this.product = product;
    }

    @Override
    public String getLines() {
        return BCPlugin.getInstance().getMenusYML().getString("insufficient-funds.text")
                .replace("%product.name%", product.getName())
                .replace("%category.name%", category.getName())
                .replace("%product.cost%", String.valueOf(product.getCost())
                );
    }
}

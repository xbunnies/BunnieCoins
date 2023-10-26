package me.bunnie.bunniecoins.player.purchase;

import lombok.Data;
import me.bunnie.bunniecoins.store.category.product.Product;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The `Purchase` class represents a record of a player's purchase in the BunnieCoins system. It contains information about
 * the purchase, including a unique identifier, the product purchased, its cost, the purchase timestamp, and whether it has been refunded.
 */
@Data
public class Purchase {

    private final String id;
    private final Product product;
    private int cost;
    private long purchasedAt;
    private boolean refunded;

    /**
     * Initializes the purchase with the provided parameters and sets
     * the `refunded` flag to `false` by default.
     *
     * @param id The unique identifier of the purchase.
     * @param product The product that was purchased.
     */
    public Purchase(String id, Product product) {
        this.id = id;
        this.product = product;
        this.cost = product.getCost();
        this.purchasedAt = System.currentTimeMillis();
        this.refunded = false;
    }

    /**
     * Get the formatted purchase date as a readable string.
     *
     * @return A formatted date string.
     */
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(this.purchasedAt);
        return dateFormat.format(date);
    }
}
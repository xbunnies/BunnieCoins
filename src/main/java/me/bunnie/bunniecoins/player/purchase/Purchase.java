package me.bunnie.bunniecoins.player.purchase;

import lombok.Data;
import me.bunnie.bunniecoins.store.category.product.Product;

@Data
public class Purchase {

    private String id;
    private Product product;
    private int cost;
    private long purchasedAt;
    private boolean refunded;

    public Purchase(String id, Product product) {
        this.id = id;
        this.product = product;
        this.cost = product.getCost();
        this.purchasedAt = (System.currentTimeMillis());
        this.refunded = false;
    }

}

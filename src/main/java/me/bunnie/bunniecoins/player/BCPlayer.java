package me.bunnie.bunniecoins.player;

import lombok.Data;
import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import me.bunnie.bunniecoins.store.category.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class BCPlayer {

    private UUID uuid;
    private int coins;
    private List<Purchase> purchases;

    public BCPlayer(UUID uuid) {
        this.uuid = uuid;
        this.coins = 0;
        this.purchases = new ArrayList<>();

        BCPlugin.getInstance().getBcManager().addBCPlayerToCache(this);
    }

    public void processPurchase(Product product) {
        this.purchases.add(new Purchase(UUID.randomUUID().toString().substring(1, 6), product));
        coins = getCoins() - product.getCost();
    }

    public void refundPurchase(String id) {
        Purchase purchase = findPurchaseByID(id);
        coins = getCoins() + purchase.getCost();
        purchase.setRefunded(true);
    }

    public Purchase findPurchaseByID(String id) {
        return purchases
                .stream()
                .filter(purchase -> purchase.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public int getTotalPurchases() {
        return purchases.size();
    }

}

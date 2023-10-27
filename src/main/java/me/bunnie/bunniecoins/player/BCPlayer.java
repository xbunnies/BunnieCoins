package me.bunnie.bunniecoins.player;

import lombok.Data;
import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import me.bunnie.bunniecoins.store.category.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The `BCPlayer` class represents a player in the BunnieCoins system, tracking their UUID, coins, and purchases.
 * This class is designed as an abstract class to allow for easy integration of different database support in the future.
 * It provides a common interface for creating, loading, and saving player data, which can be implemented by specific database
 * support classes such as MySQL or SQLite.
 */
@Data
public abstract class BCPlayer {

    private UUID uuid;
    private int coins;
    private List<Purchase> purchases;

    /**
     * Initializes the player with default values,
     * adds the player to the cache, and attempts to load existing data.
     *
     * @param uuid The UUID of the player.
     */
    public BCPlayer(UUID uuid) {
        this.uuid = uuid;
        this.coins = 0;
        this.purchases = new ArrayList<>();

        this.load();
        BCPlugin.getInstance().getBcPlayerManager().addBCPlayerToCache(this);
    }

    /**
     * Creates a new player data entry in the database.
     */
    public abstract void create();

    /**
     * Loads player data from the database.
     */
    public abstract void load();

    /**
     * Saves the player data to the database.
     */
    public abstract void save();

    /**
     * Creates a purchase entry for the player in the database.
     * Implementations of this method should handle the insertion of purchase information, including the provided purchase object,
     * into the database.
     *
     * @param purchase The purchase to be created in the database.
     */
    public abstract void createPurchase(Purchase purchase);

    /**
     * Loads a player's purchase history from the database.
     * Implementations of this method should retrieve and populate the player's purchase history, including details
     * of each purchase, from the database into the `purchases` list or data structure in the implementing class.
     */
    public abstract void loadPurchases();

    /**
     * Saves a purchase entry to the database.
     * Implementations of this method should handle the update or insertion of a purchase record, as specified by the provided purchase object,
     * in the database. This method is typically called when a new purchase is made or an existing one is updated.
     *
     * @param purchase The purchase to be saved or updated in the database.
     */
    public abstract void savePurchase(Purchase purchase);
    /**
     * Process a purchase by adding it to the list of purchases and deducting the cost from the player's coins.
     *
     * @param product The product being purchased.
     */
    public void processPurchase(Product product) {
        Purchase purchase = new Purchase(UUID.randomUUID().toString(), product);
        this.purchases.add(purchase);
        coins = coins - product.getCost();

        createPurchase(purchase);
        save();
    }

    /**
     * Refunds a purchase by adding back the coins and marking the purchase as refunded.
     *
     * @param id The unique identifier of the purchase to be refunded.
     */
    public void refundPurchase(String id) {
        Purchase purchase = findPurchaseByID(id);
        coins = coins + purchase.getCost();
        purchase.setRefunded(true);

        savePurchase(purchase);
        save();
    }

    /**
     * Finds a purchase by its unique identifier.
     *
     * @param id The unique identifier of the purchase to search for.
     * @return The found purchase, or null if not found.
     */
    public Purchase findPurchaseByID(String id) {
        return purchases
                .stream()
                .filter(purchase -> purchase.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the total number of purchases made by the player.
     *
     * @return The total number of purchases.
     */
    public int getTotalPurchases() {
        return purchases.size();
    }

    /**
     * Sorts the Purchases by their timestamps by Newest -> Oldest
     *
     * @return Sorted List.
     */
    public List<Purchase> getSortedPurchases() {
        List<Purchase> sortedPurchases = new ArrayList<>(purchases);

        sortedPurchases.sort((purchase1, purchase2) -> {
            long date1 = purchase1.getPurchasedAt();
            long date2 = purchase2.getPurchasedAt();

            return Long.compare(date2, date1);
        });
        return sortedPurchases;
    }
}

package me.bunnie.bunniecoins.player;

import lombok.Data;
import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.deposit.Deposit;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import me.bunnie.bunniecoins.store.category.product.Product;
import me.bunnie.bunniecoins.player.withdraw.Withdraw;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The `BCPlayer` class represents a player in the BunnieCoins system, tracking their UUID, coins, and purchases.
 * This class is designed as an abstract class to allow for easy integration of different database support in the future.
 * It provides a common interface for creating, loading, and saving player data, which can be implemented by specific database
 * support classes such as SQL and MongoDB.
 */
@Data
public abstract class BCPlayer {

    private UUID uuid;
    private int coins;
    private List<Deposit> deposits;
    private List<Withdraw> withdrawals;
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
        this.deposits = new ArrayList<>();
        this.withdrawals = new ArrayList<>();
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
     * Creates a withdrawal entry for the player in the database.
     * Implementations of this method should handle the insertion of withdraw information, including the provided amount,
     * into the database.
     *
     * @param withdraw The withdrawal to be created in the database.
     */
    public abstract void createWithdrawal(Withdraw withdraw);

    /**
     * Loads a player's withdraw history from the database.
     * Implementations of this method should retrieve and populate the player's withdraw history, including details
     * of each withdraw, from the database into the `withdrawals` list or data structure in the implementing class.
     */
    public abstract void loadWithdrawals();

    /**
     * Loads a withdrawal from the database.
     * Implementations of this method should retrieve and populate the withdrawal.
     */
    public abstract Withdraw loadWithdraw(UUID id);

    /**
     * Saves a withdrawal entry to the database.
     * Implementations of this method should handle the update or insertion of a withdrawal record, as specified by the provided withdraw object,
     * in the database. This method is typically called when an existing withdrawal is updated.
     *
     * @param withdraw The purchase to be saved or updated in the database.
     */
    public abstract void saveWithdrawal(Withdraw withdraw);

    /**
     * Creates a deposit entry for the player in the database.
     * Implementations of this method should handle the insertion of deposit information into the database.
     *
     * @param deposit The deposit to be created in the database.
     */
    public abstract void createDeposit(Deposit deposit);

    /**
     * Loads a player's deposit history from the database.
     * Implementations of this method should retrieve and populate the player's deposit history, including details
     * of each deposit, from the database into the `deposits` list or data structure in the implementing class.
     */
    public abstract void loadDeposits();

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
    public void processPurchase(Product product, boolean purchasedAtDiscount) {
        Purchase purchase = new Purchase(UUID.randomUUID().toString(), product);
        this.purchases.add(purchase);
        coins = coins - product.getCost();
        purchase.setPurchasedAtDiscount(purchasedAtDiscount);
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

    /**
     * Sorts Withdraws by their timestamps by Newest -> Oldest
     *
     * @return Sorted List.
     */
    public List<Withdraw> getSortedWithdrawals() {
        List<Withdraw> sortedWithdrawals = new ArrayList<>(withdrawals);

        sortedWithdrawals.sort((withdraw, withdrawal) -> {
            long date1 = withdraw.getWithdrewAt();
            long date2 = withdrawal.getWithdrewAt();

            return Long.compare(date2, date1);
        });
        return sortedWithdrawals;
    }

    /**
     * Sorts Withdraws by their timestamps by Newest -> Oldest
     *
     * @return Sorted List.
     */
    public List<Deposit> getSortedDeposits() {
        List<Deposit> sortedDeposits = new ArrayList<>(deposits);

        sortedDeposits.sort((deposit, depos) -> {
            long date1 = deposit.getDepositedAt();
            long date2 = depos.getDepositedAt();

            return Long.compare(date2, date1);
        });
        return sortedDeposits;
    }
}

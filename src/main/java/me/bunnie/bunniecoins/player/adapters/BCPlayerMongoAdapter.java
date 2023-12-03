package me.bunnie.bunniecoins.player.adapters;

import com.mongodb.client.model.Filters;
import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.deposit.Deposit;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import me.bunnie.bunniecoins.player.withdraw.Withdraw;
import org.bson.Document;

import java.util.UUID;

public class BCPlayerMongoAdapter extends BCPlayer {

    public BCPlayerMongoAdapter(UUID uuid) {
        super(uuid);
    }

    @Override
    public void create() {
        Document document = new Document("uuid", getUuid().toString());
        document.append("coins", getCoins());
        BCPlugin.getInstance().getMongoManager().getProfiles().insertOne(document);
    }

    @Override
    public void load() {
        Document document = BCPlugin.getInstance().getMongoManager().getProfiles().find(Filters.eq("uuid", getUuid().toString())).first();
        if(document == null) {
            this.create();
            return;
        }
        setCoins(document.getInteger("coins"));
    }

    @Override
    public void save() {
        Document document = new Document("uuid", getUuid().toString());
        document.append("coins", getCoins());

        Document document1 =  BCPlugin.getInstance().getMongoManager().getProfiles().find(
                Filters.eq("uuid", getUuid().toString())).first();
        if (document1 == null) {
            return;
        }
        BCPlugin.getInstance().getMongoManager().getProfiles().replaceOne(document1, document);
    }

    @Override
    public void createPurchase(Purchase purchase) {
        Document document = new Document("id", purchase.getId());
        document.append("player_uuid", getUuid().toString());
        document.append("product", purchase.getProduct().getName());
        document.append("cost", purchase.getCost());
        document.append("purchase_timestamp", purchase.getPurchasedAt());
        document.append("refunded", purchase.isRefunded());
        BCPlugin.getInstance().getMongoManager().getPurchases().insertOne(document);
    }

    @Override
    public void loadPurchases() {
        for(Document document : BCPlugin.getInstance().getMongoManager().getPurchases().find(Filters.eq("player_uuid", getUuid().toString()))) {
            Purchase purchase = new Purchase(document.getString("id"), BCPlugin.getInstance().getShopManager().findProductByName(document.getString("product")));
            purchase.setCost(document.getInteger("cost"));
            purchase.setPurchasedAt(document.getLong("purchase_timestamp"));
            purchase.setRefunded(document.getBoolean("refunded"));
            if (getPurchases().contains(purchase)) continue;
            getPurchases().add(purchase);
        }
    }

    @Override
    public void createWithdrawal(Withdraw withdraw) {
        Document document = new Document("id", withdraw.getId().toString());
        document.append("player_uuid", withdraw.getPlayerUUID().toString());
        document.append("amount", withdraw.getAmount());
        document.append("withdrew_timestamp", withdraw.getWithdrewAt());
        document.append("withdrawn", withdraw.isWithdrawn());
        BCPlugin.getInstance().getMongoManager().getWithdrawals().insertOne(document);
    }

    @Override
    public void loadWithdrawals() {
        for(Document document : BCPlugin.getInstance().getMongoManager().getWithdrawals().find(Filters.eq("player_uuid", getUuid().toString()))) {
            Withdraw withdraw = new Withdraw(UUID.fromString(document.getString("id")), getUuid(), document.getInteger("amount"));
            withdraw.setWithdrewAt(document.getLong("withdrew_timestamp"));
            withdraw.setWithdrawn(document.getBoolean("withdrawn"));
            if (getWithdrawals().contains(withdraw)) continue;
            getWithdrawals().add(withdraw);
        }
    }

    @Override
    public Withdraw loadWithdraw(UUID id) {
        for(Document document : BCPlugin.getInstance().getMongoManager().getWithdrawals().find(Filters.eq("id", id.toString()))) {
            Withdraw withdraw = new Withdraw(UUID.fromString(document.getString("id")), UUID.fromString(document.getString("player_uuid")), document.getInteger("amount"));
            withdraw.setWithdrewAt(document.getLong("withdrew_timestamp"));
            withdraw.setWithdrawn(document.getBoolean("withdrawn"));
            return withdraw;
        }
        return null;
    }

    @Override
    public void saveWithdrawal(Withdraw withdraw) {
        Document document = new Document("id", withdraw.getId().toString());
        document.append("player_uuid", withdraw.getPlayerUUID().toString());
        document.append("amount", withdraw.getAmount());
        document.append("withdrew_timestamp", withdraw.getWithdrewAt());
        document.append("withdrawn", withdraw.isWithdrawn());

        Document document1 =  BCPlugin.getInstance().getMongoManager().getWithdrawals().find(
                Filters.eq("id", withdraw.getId().toString())).first();
        if (document1 == null) {
            return;
        }
        BCPlugin.getInstance().getMongoManager().getWithdrawals().replaceOne(document1, document);
    }

    @Override
    public void createDeposit(Deposit deposit) {
        Document document = new Document("id", deposit.getId().toString());
        document.append("player_uuid", getUuid().toString());
        document.append("withdrawer", deposit.getWithdrawer());
        document.append("amount", deposit.getAmount());
        document.append("deposit_timestamp", deposit.getDepositedAt());
        BCPlugin.getInstance().getMongoManager().getDeposits().insertOne(document);
    }

    @Override
    public void loadDeposits() {
        for(Document document : BCPlugin.getInstance().getMongoManager().getDeposits().find(Filters.eq("player_uuid", getUuid().toString()))) {
            Deposit deposit = new Deposit(UUID.fromString(document.getString("id")), getUuid(), document.getString("withdrawer"), document.getInteger("amount"));
            if (getDeposits().contains(deposit)) continue;
            getDeposits().add(deposit);
        }
    }

    @Override
    public void savePurchase(Purchase purchase) {
        Document document = new Document("id", purchase.getId());
        document.append("player_uuid", getUuid().toString());
        document.append("product", purchase.getProduct().getName());
        document.append("cost", purchase.getCost());
        document.append("purchase_timestamp", purchase.getPurchasedAt());
        document.append("refunded", purchase.isRefunded());

        Document document1 =  BCPlugin.getInstance().getMongoManager().getPurchases().find(
                Filters.eq("id", purchase.getId())).first();
        if (document1 == null) {
            return;
        }
        BCPlugin.getInstance().getMongoManager().getPurchases().replaceOne(document1, document);
    }
}

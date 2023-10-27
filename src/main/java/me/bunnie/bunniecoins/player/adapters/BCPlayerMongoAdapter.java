package me.bunnie.bunniecoins.player.adapters;

import com.mongodb.client.model.Filters;
import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.purchase.Purchase;
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

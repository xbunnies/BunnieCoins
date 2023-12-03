package me.bunnie.bunniecoins.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import lombok.Getter;
import me.bunnie.bunniecoins.BCPlugin;
import org.bson.Document;

import java.util.Objects;

public class MongoManager {

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> profiles, purchases, withdrawals, deposits;

    public MongoManager(BCPlugin plugin) {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(700);
        ServerAddress address = new ServerAddress(
                plugin.getConfigYML().getString("settings.database.mongo.address"),
                plugin.getConfigYML().getInt("settings.database.mongo.port"));
        if(plugin.getConfigYML().getBoolean("settings.database.mongo.auth.enabled")) {
            client = new MongoClient(
                    address,
                    MongoCredential.createCredential(
                            plugin.getConfigYML().getString("settings.database.auth.username"),
                            Objects.requireNonNull(plugin.getConfigYML().getString("settings.database.auth.database")),
                            plugin.getConfigYML().getString("settings.database.auth.password").toCharArray()
                    ),
                    builder.build()
            );
        } else {
            client = new MongoClient(address, builder.build());
        }

        database = client.getDatabase("bunniecoins");
        profiles = database.getCollection("profiles");
        profiles.createIndex(Indexes.ascending("uuid"));
        purchases = database.getCollection("purchases");
        purchases.createIndex(Indexes.ascending("id"));
        withdrawals = database.getCollection("withdrawals");
        withdrawals.createIndex(Indexes.ascending("id"));
        deposits = database.getCollection("deposits");
        deposits.createIndex(Indexes.ascending("id"));

    }

    public MongoCollection<Document> getProfiles() {
        return profiles;
    }

    public MongoCollection<Document> getPurchases() {
        return purchases;
    }

    public MongoCollection<Document> getWithdrawals() {
        return withdrawals;
    }

    public MongoCollection<Document> getDeposits() {
        return deposits;
    }
}

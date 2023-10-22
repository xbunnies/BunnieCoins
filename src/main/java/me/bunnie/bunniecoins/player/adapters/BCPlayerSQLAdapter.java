package me.bunnie.bunniecoins.player.adapters;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.purchase.Purchase;

import java.sql.*;
import java.util.UUID;

public class BCPlayerSQLAdapter extends BCPlayer {

    public BCPlayerSQLAdapter(UUID uuid) {
        super(uuid);
        BCPlugin.getInstance().getBcPlayerManager().addBCPlayerToCache(this);
    }

    @Override
    public void create() {
        Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
        String query = "INSERT INTO bc_players (UUID, COINS) VALUES (?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, getUuid().toString());
            ps.setInt(2, getCoins());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
        String query = "SELECT * FROM bc_players WHERE UUID='" + getUuid().toString() + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(!rs.next()) {
                this.create();
                return;
            }
            while(rs.next()) {
                int coins = rs.getInt("COINS");
                setCoins(coins);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
        String query = "UPDATE bc_players SET COINS=? WHERE UUID=?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, getCoins());
            ps.setString(2, getUuid().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createPurchase(Purchase purchase) {
        Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
        String query = "INSERT INTO bc_purchases (ID,PLAYER_UUID,PRODUCT,COST,PURCHASE_TIMESTAMP,REFUNDED) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, purchase.getId());
            ps.setString(2, getUuid().toString());
            ps.setString(3, purchase.getProduct().getName());
            ps.setInt(4, purchase.getCost());
            ps.setLong(6, purchase.getPurchasedAt());
            ps.setBoolean(7, purchase.isRefunded());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadPurchases() {
        Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
        String query = "SELECT * FROM bc_purchases WHERE UUID='" + getUuid().toString() + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(!rs.next()) return;

            while(rs.next()) {
                Purchase purchase = new Purchase(rs.getString("ID"), BCPlugin.getInstance().getShopManager().findProductByName(rs.getString("product")));
                purchase.setCost(rs.getInt("COST"));
                purchase.setPurchasedAt(rs.getLong("PURCHASE_TIMESTAMP"));
                purchase.setRefunded(rs.getBoolean("REFUNDED"));
                getPurchases().add(purchase);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePurchase(Purchase purchase) {
        Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
        String query = "UPDATE bc_purchases SET REFUNDED=? WHERE ID=?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setBoolean(1, purchase.isRefunded());
            ps.setString(2, purchase.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

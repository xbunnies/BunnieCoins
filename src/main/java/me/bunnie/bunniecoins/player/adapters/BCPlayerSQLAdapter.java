package me.bunnie.bunniecoins.player.adapters;

import me.bunnie.bunniecoins.BCPlugin;
import me.bunnie.bunniecoins.player.BCPlayer;
import me.bunnie.bunniecoins.player.deposit.Deposit;
import me.bunnie.bunniecoins.player.purchase.Purchase;
import me.bunnie.bunniecoins.player.withdraw.Withdraw;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.UUID;

public class BCPlayerSQLAdapter extends BCPlayer {

    public BCPlayerSQLAdapter(UUID uuid) {
        super(uuid);
    }

    @Override
    public void create() {
        new BukkitRunnable() {
            @Override
            public void run() {
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
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void load() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "SELECT * FROM bc_players WHERE UUID='" + getUuid().toString() + "'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(query);
                    if (!rs.next()) {
                        create();
                    } else {
                        int coins = rs.getInt("COINS");
                        setCoins(coins);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void save() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "UPDATE bc_players SET COINS=? WHERE UUID=?";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, getCoins());
                    ps.setString(2, getUuid().toString());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void createPurchase(Purchase purchase) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "INSERT INTO bc_purchases (ID,PLAYER_UUID,PRODUCT,COST,PURCHASE_TIMESTAMP,REFUNDED) VALUES (?,?,?,?,?,?)";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, purchase.getId());
                    ps.setString(2, getUuid().toString());
                    ps.setString(3, purchase.getProduct().getName());
                    ps.setInt(4, purchase.getCost());
                    ps.setLong(5, purchase.getPurchasedAt());
                    ps.setBoolean(6, purchase.isRefunded());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void loadPurchases() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "SELECT * FROM bc_purchases WHERE PLAYER_UUID='" + getUuid().toString() + "'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(query);
                    while (rs.next()) {
                        Purchase purchase = new Purchase(rs.getString("ID"), BCPlugin.getInstance().getShopManager().findProductByName(rs.getString("product")));
                        purchase.setCost(rs.getInt("COST"));
                        purchase.setPurchasedAt(rs.getLong("PURCHASE_TIMESTAMP"));
                        purchase.setRefunded(rs.getBoolean("REFUNDED"));
                        if (getPurchases().contains(purchase)) continue;
                        getPurchases().add(purchase);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void createWithdrawal(Withdraw withdraw) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "INSERT INTO bc_withdrawals (ID,PLAYER_UUID,AMOUNT,WITHDREW_TIMESTAMP,WITHDRAWN) VALUES (?,?,?,?,?)";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, withdraw.getId().toString());
                    ps.setString(2, getUuid().toString());
                    ps.setInt(3, withdraw.getAmount());
                    ps.setLong(4, withdraw.getWithdrewAt());
                    ps.setBoolean(5, withdraw.isWithdrawn());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void loadWithdrawals() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "SELECT * FROM bc_withdrawals WHERE PLAYER_UUID='" + getUuid().toString() + "'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(query);
                    while (rs.next()) {
                        Withdraw withdraw = new Withdraw(UUID.fromString(rs.getString("ID")), getUuid(), rs.getInt("AMOUNT"));
                        withdraw.setWithdrewAt(rs.getLong("WITHDREW_TIMESTAMP"));
                        withdraw.setWithdrawn(rs.getBoolean("WITHDRAWN"));
                        if (getWithdrawals().contains(withdraw)) continue;
                        getWithdrawals().add(withdraw);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public Withdraw loadWithdraw(UUID id) {
        Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM bc_withdrawals WHERE ID='" + id.toString() + "'";
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                Withdraw withdraw = new Withdraw(id, UUID.fromString(rs.getString("PLAYER_UUID")), rs.getInt("AMOUNT"));
                withdraw.setWithdrewAt(rs.getLong("WITHDREW_TIMESTAMP"));
                withdraw.setWithdrawn(rs.getBoolean("WITHDRAWN"));
                return withdraw;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public void saveWithdrawal(Withdraw withdraw) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "UPDATE bc_withdrawals SET WITHDRAWN=? WHERE ID=?";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setBoolean(1, withdraw.isWithdrawn());
                    ps.setString(2, withdraw.getId().toString());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void createDeposit(Deposit deposit) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "INSERT INTO bc_deposits (ID,PLAYER_UUID,AMOUNT,WITHDRAWER,DEPOSIT_TIMESTAMP) VALUES (?,?,?,?,?)";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, deposit.getId().toString());
                    ps.setString(2, getUuid().toString());
                    ps.setInt(3, deposit.getAmount());
                    ps.setString(4, deposit.getWithdrawer());
                    ps.setLong(5, deposit.getDepositedAt());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void loadDeposits() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "SELECT * FROM bc_deposits WHERE PLAYER_UUID='" + getUuid().toString() + "'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(query);
                    while (rs.next()) {
                        Deposit deposit = new Deposit(UUID.fromString(rs.getString("ID")), getUuid(), rs.getString("WITHDRAWER"), rs.getInt("AMOUNT"));
                        deposit.setDepositedAt(rs.getLong("DEPOSIT_TIMESTAMP"));
                        if (getDeposits().contains(deposit)) continue;
                        getDeposits().add(deposit);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

    @Override
    public void savePurchase(Purchase purchase) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = BCPlugin.getInstance().getSqlManager().getConnection();
                String query = "UPDATE bc_purchases SET REFUNDED=? WHERE ID=?";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setBoolean(1, purchase.isRefunded());
                    ps.setString(2, purchase.getId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(BCPlugin.getInstance());
    }

}

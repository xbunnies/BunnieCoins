package me.bunnie.bunniecoins.database;

import me.bunnie.bunniecoins.BCPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class SQLManager {

    private final BCPlugin plugin;
    private final String type;
    private Connection connection;
    private File file;

    public SQLManager(BCPlugin plugin) {
        this.plugin = plugin;
        this.type = plugin.getType();

        this.connect();

        this.createTables();
    }

    private void connect() {
        try {
            if(type.equalsIgnoreCase("sqlite")) {
                file = new File(this.plugin.getDataFolder().getAbsolutePath(), "data.db");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.plugin.getDataFolder().getAbsolutePath() + "/data.db");
            } else if(type.equalsIgnoreCase("mysql")) {
                String address = plugin.getConfigYML().getString("settings.database.mysql.address"),
                        username = plugin.getConfigYML().getString("settings.database.mysql.username"),
                        password = plugin.getConfigYML().getString("settings.database.mysql.password"),
                        database= plugin.getConfigYML().getString("settings.database.mysql.database") ;
                int port = plugin.getConfigYML().getInt("settings.database.mysql.port");
                this.connection = DriverManager.getConnection("jdbc:mysql://" +
                                address + ":" + port + "/" + database + "?characterEncoding=utf8",
                        username, password);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void createTables() {
        List<String> tables = Arrays.asList(
                "CREATE TABLE IF NOT EXISTS bc_players(" +
                        "UUID VARCHAR(36), " +
                        "COINS INTEGER, " +
                        "PRIMARY KEY(UUID)" +
                        ")",
                "CREATE TABLE IF NOT EXISTS bc_purchases(" +
                        "ID VARCHAR(36), " +
                        "PLAYER_UUID VARCHAR(36), " +
                        "PRODUCT TEXT, " +
                        "COST INTEGER, " +
                        "PURCHASE_TIMESTAMP LONG, " +
                        "REFUNDED BOOLEAN, " +
                        "PURCHASED_AT_DISCOUNT BOOLEAN, " +
                        "PRIMARY KEY (ID)" +
                        ")",
                "CREATE TABLE IF NOT EXISTS bc_withdrawals(" +
                        "ID VARCHAR(36), " +
                        "PLAYER_UUID VARCHAR(36), " +
                        "AMOUNT INTEGER, " +
                        "WITHDREW_TIMESTAMP LONG, " +
                        "WITHDRAWN BOOLEAN, " +
                        "PRIMARY KEY (ID)" +
                        ")",
                "CREATE TABLE IF NOT EXISTS bc_deposits(" +
                        "ID VARCHAR(36), " +
                        "PLAYER_UUID VARCHAR(36), " +
                        "WITHDRAWER VARCHAR(36), " +
                        "AMOUNT INTEGER, " +
                        "DEPOSIT_TIMESTAMP LONG, " +
                        "PRIMARY KEY (ID)" +
                        ")"
        );
        if (type.equalsIgnoreCase("sqlite")) {
            if(file.exists()) {
                try {
                    Statement statement = connection.createStatement();
                    for(String s : tables) {
                        statement.execute(s);
                    }
                    updateTables(connection, "bc_purchases", "PURCHASED_AT_DISCOUNT", "BOOLEAN");
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else if (type.equalsIgnoreCase("mysql")) {
            try {
                Statement statement = connection.createStatement();
                for(String s : tables) {
                    statement.execute(s);
                }

                updateTables(connection, "bc_purchases", "PURCHASED_AT_DISCOUNT", "BOOLEAN");
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void updateTables(Connection connection, String table, String columnToAdd, String type) {
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet columns = meta.getColumns(null, null, table, columnToAdd);
            if(!columns.next()) {
                String query = "ALTER TABLE " + table + " ADD COLUMN " + columnToAdd + " " + type + " DEFAULT FALSE";
                connection.createStatement().executeUpdate(query);
                plugin.getLogger().info("Your Database has been updated to contain the " + columnToAdd + " column!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            if (this.connection != null)
                this.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

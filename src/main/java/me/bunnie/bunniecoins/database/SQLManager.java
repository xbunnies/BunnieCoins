package me.bunnie.bunniecoins.database;

import me.bunnie.bunniecoins.BCPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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

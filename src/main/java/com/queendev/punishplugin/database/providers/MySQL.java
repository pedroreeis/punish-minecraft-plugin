package com.queendev.punishplugin.database.providers;

import com.queendev.punishplugin.Punish;
import com.queendev.punishplugin.database.Database;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL implements Database {

    private Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void open() {
        String host = Punish.getPlugin().getPunishConfig().getString("MySQL.host");
        String user = Punish.getPlugin().getPunishConfig().getString("MySQL.usuario");
        String password = Punish.getPlugin().getPunishConfig().getString("MySQL.senha");
        String database = Punish.getPlugin().getPunishConfig().getString("MySQL.database");
        String url = "jdbc:mysql://" + host + "/" + database + "?autoReconnect=true";

        try {
            connection = DriverManager.getConnection(url, user, password);
            createTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(Punish.getPlugin());
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try {
            PreparedStatement stm = this.connection.prepareStatement(
                    "create table if not exists punishplugin(`player` TEXT NOT NULL, " +
                            "`author` TEXT NOT NULL, " +
                            "`type` TEXT NOT NULL, " +
                            "`reason` TEXT NOT NULL, " +
                            "`prova` TEXT NOT NULL, " +
                            "`time` INT NOT NULL)"
            );
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
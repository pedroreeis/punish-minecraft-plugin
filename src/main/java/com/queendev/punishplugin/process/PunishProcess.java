package com.queendev.punishplugin.process;

import com.queendev.punishplugin.Punish;
import com.queendev.punishplugin.managers.PunishManager;
import com.queendev.punishplugin.models.PunishModel;
import com.queendev.punishplugin.repository.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PunishProcess {

    private static Database database = Punish.getPlugin().getPunishDatabase();
    private static PunishManager manager = Punish.getPlugin().getPunishManager();

    public static void loadPunishs() {
        try {
            PreparedStatement stm = database.getConnection().prepareStatement(
                    "select * from punishplugin"
            );

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String player = rs.getString("player");
                String author = rs.getString("author");
                String type = rs.getString("type");
                String reason = rs.getString("reason");
                String prova = rs.getString("prova");
                Integer time = rs.getInt("time");

                manager.createPunish(player, author, type, reason, prova, time);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean containsPunishSQL(String player) {
        try {
            PreparedStatement stm = database.getConnection().prepareStatement(
                    "select * from punishplugin where player = ?"
            );
            stm.setString(1, player);
            return stm.executeQuery().next();
        }catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void savePunishs() {
        PreparedStatement stm;
        try {
            for(PunishModel punish : manager.punishs.values()) {
                if(containsPunishSQL(punish.getPlayer())) {
                    stm = database.getConnection().prepareStatement(
                            "update punishplugin set author = ?, type = ?, reason = ?, prova = ?, time = ? where player = ?"
                    );

                    stm.setString(1, punish.getAuthor());
                    stm.setString(2, punish.getType());
                    stm.setString(3, punish.getReason());
                    stm.setString(4, punish.getProva());
                    stm.setInt(5, punish.getTime());
                    stm.setString(6, punish.getPlayer());

                    stm.executeUpdate();
                }else {
                    stm = database.getConnection().prepareStatement(
                            "insert into punishplugin(player,author,type,reason,prova,time) VALUES(?,?,?,?,?,?)"
                    );

                    stm.setString(1, punish.getPlayer());
                    stm.setString(2, punish.getAuthor());
                    stm.setString(3, punish.getType());
                    stm.setString(4, punish.getReason());
                    stm.setString(5, punish.getProva());
                    stm.setInt(6, punish.getTime());

                    stm.executeUpdate();
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

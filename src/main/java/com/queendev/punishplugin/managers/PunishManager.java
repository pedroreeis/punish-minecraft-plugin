package com.queendev.punishplugin.managers;

import com.queendev.punishplugin.models.PunishModel;

import java.util.HashMap;

public class PunishManager {

    public HashMap<String, PunishModel> punishs;

    public PunishManager() {
        this.punishs = new HashMap<>();
    }

    public PunishModel createPunish(String player, String author, String type, String reason, String prova, Integer time) {
        PunishModel punish = new PunishModel(player, author, type, reason, prova, time);
        this.punishs.put(player.toLowerCase(), punish);
        return punish;
    }

    public PunishModel getPunish(String player) {
        if(this.punishs.containsKey(player.toLowerCase())) {
            return this.punishs.get(player.toLowerCase());
        }
        return null;
    }

}

package com.queendev.punishplugin.listeners;

import com.queendev.punishplugin.Punish;
import com.queendev.punishplugin.managers.ConfigManager;
import com.queendev.punishplugin.managers.PunishManager;
import com.queendev.punishplugin.models.PunishModel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {

    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PunishManager punishManager = Punish.getPlugin().getPunishManager();
        PunishModel punish = punishManager.getPunish(p.getName().toLowerCase());

        if(punish != null) {
            ConfigManager messageConfig = Punish.getPlugin().getMessagesConfig();
            String timePunish = punish.getTime() == 0 ? "§cPERMANENTE" : punish.getTime() + " HORA(S)";
            String reasonPunish = Punish.getPlugin().getReasonManager().getReason(punish.getReason()).getLabel();
            p.kickPlayer(messageConfig.getString("Mensagens.punishKick").replaceAll("&", "§").replace("@player", p.getName()).replace("@reason", reasonPunish).replace("@autor", punish.getAuthor()).replace("@prova", punish.getProva()).replace("@time", timePunish));
        }
    }
}

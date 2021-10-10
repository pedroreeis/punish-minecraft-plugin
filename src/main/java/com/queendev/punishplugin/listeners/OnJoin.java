package com.queendev.punishplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {

    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

    }
}

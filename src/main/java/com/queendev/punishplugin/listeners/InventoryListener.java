package com.queendev.punishplugin.listeners;

import com.queendev.punishplugin.Punish;
import com.queendev.punishplugin.models.PunishModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener {

    @EventHandler
    void onInteract(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("Consulta de punições:")) {
            ItemMeta item = e.getCurrentItem().getItemMeta();
            String player = item.getDisplayName();
            PunishModel punish = Punish.getPlugin().getPunishManager().getPunish(player);

            if(punish != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains(player)) {
                    if(e.isLeftClick()) {
                            e.getView().close();
                            p.sendMessage("§aClique aqui para copiar a prova: §7" + punish.getProva());
                    }

                    if(e.isRightClick()) {
                        Punish.getPlugin().getPunishManager().deletePunish(player);
                            e.getView().close();
                            Punish.getPlugin().getPunishManager().deletePunish(player);
                    }
                }
            }
        }
    }
}
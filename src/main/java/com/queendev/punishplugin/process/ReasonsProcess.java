package com.queendev.punishplugin.process;

import com.queendev.punishplugin.Punish;
import com.queendev.punishplugin.managers.ReasonManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class ReasonsProcess {

    private static ReasonManager manager = Punish.getPlugin().getReasonManager();

    public static void loadReasons() {
        ConfigurationSection section = Punish.getPlugin().getReason().getConfigurationSection("ReasonPunish");
        if(section != null) {
            for(String id : section.getKeys(false)) {
                ConfigurationSection key = Punish.getPlugin().getReason().getConfigurationSection("ReasonPunish." + id);

                String type = key.getString("type");
                Integer time = key.getInt("time");
                String permission = key.getString("permission");
                String label = key.getString("label");
                String description = key.getString("description");

                manager.addReason(id, type, time, permission, label, description);
            }
        }else {
            Bukkit.getConsoleSender().sendMessage("§c[PunishPlugin] Sua config.yml está incompleta, delete e crie uma nova.");
            Bukkit.getPluginManager().disablePlugin(Punish.getPlugin());
        }
    }
}

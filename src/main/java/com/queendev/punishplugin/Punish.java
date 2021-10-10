package com.queendev.punishplugin;

import com.queendev.punishplugin.commands.PunishCommand;
import com.queendev.punishplugin.managers.ConfigManager;
import com.queendev.punishplugin.managers.PunishManager;
import com.queendev.punishplugin.managers.ReasonManager;
import com.queendev.punishplugin.process.PunishProcess;
import com.queendev.punishplugin.process.ReasonsProcess;
import com.queendev.punishplugin.repository.Database;
import com.queendev.punishplugin.repository.providers.MySQL;
import com.queendev.punishplugin.repository.providers.SQLite;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Punish extends JavaPlugin {

    private static Punish plugin;

    private ConfigManager punishConfig;
    private ConfigManager messagesConfig;
    private ConfigManager reason;

    private PunishManager punishManager;
    private ReasonManager reasonManager;

    private Database punishDatabase;

    public static Punish getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        punishConfig = new ConfigManager(null, "config.yml", false);
        messagesConfig = new ConfigManager(null, "messages.yml", false);
        reason = new ConfigManager(null, "reason.yml", false);

        punishDatabase = punishConfig.getBoolean("MySQL.ativar") ? new MySQL() : new SQLite();
        punishDatabase.open();

        this.punishManager = new PunishManager();
        this.reasonManager = new ReasonManager();
        PunishProcess.loadPunishs();
        ReasonsProcess.loadReasons();

        getCommand("punir").setExecutor(new PunishCommand());
        //Bukkit.getPluginManager().registerEvents(new OnJoin(), this);
        getLogger().info("Plugin habilitado com sucesso.");
    }

    @Override
    public void onDisable() {
        PunishProcess.savePunishs();
    }
}

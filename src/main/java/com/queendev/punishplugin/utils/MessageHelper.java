package com.queendev.punishplugin.utils;

import com.queendev.punishplugin.Punish;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class MessageHelper {

    public static List<String> msgList(String path) {
        return Punish.getPlugin().getMessagesConfig().getStringList("Mensagens." + path).stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
    }
}

package com.queendev.punishplugin.commands;

import com.queendev.punishplugin.Punish;
import com.queendev.punishplugin.managers.ConfigManager;
import com.queendev.punishplugin.managers.PunishManager;
import com.queendev.punishplugin.models.PunishModel;
import com.queendev.punishplugin.models.ReasonModel;
import com.queendev.punishplugin.utils.ChatObject;
import com.queendev.punishplugin.utils.MessageHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnPunishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cVocê tentou executar este comando no console, ele está disponivel apenas para jogadores.");
            return false;
        }

        Player p = (Player)sender;
        ConfigManager messageConfig = Punish.getPlugin().getMessagesConfig();
        ConfigManager config = Punish.getPlugin().getPunishConfig();

        if(!p.hasPermission(config.getString("permissionUse"))) {
            p.sendMessage(messageConfig.getString("Mensagens.noPermission").replaceAll("&", "§"));
            return false;
        }

        if(args.length == 0) {
            p.sendMessage("§cUso: /unpunish <jogador> <motivo>.");
            return false;
        }

        PunishManager punishManager = Punish.getPlugin().getPunishManager();
        PunishModel punish = punishManager.getPunish(args[0].toLowerCase());

        if(punish == null) {
            p.sendMessage("§cEste jogador não possui nenhuma punição ativa, para revogar primeiro você precisa puni-lo.");
            return false;
        }

        if(args.length == 1) {
            p.sendMessage("§eEscolha um motivo para revogar a punição de: " + args[0]);
            p.sendMessage(" ");

            ArrayList<ChatObject> list = new ArrayList<>();
                    list.add(new ChatObject("Motivo Incorreto",
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder("§eMOTIVO INCORRETO\n\n§fVocê deve usar esta opção quando a punição foi aplicada com o motivo errado.").create()),
                            new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                    "/unpunish " + args[0] + " MOTIVO_INCORRETO")));

                    list.add(new ChatObject("Prova Incorreta",
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder("§ePROVA INCORRETA\n\n§fVocê deve usar esta opção quando a punição foi aplicada com a prova errada.").create()),
                            new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                    "/unpunish " + args[0] + " PROVA_INCORRETA")));

                    list.add(new ChatObject("Usuario Desbanido",
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder("§eUSUARIO DESBANIDO\n\n§fVocê deve usar esta opção quando a punição foi aplicada, mas o jogador será \ndesbanido sem nenhum motivo especial.").create()),
                            new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                    "/unpunish " + args[0] + " JOGADOR_DESBANIDO")));

            sendReasons(p, list);
            return false;
        }

        if(args.length > 2) {
            p.sendMessage("§eO motivo não deve conter espaços.");
            return false;
        }

        if(args.length == 2) {
            if(args[1].equalsIgnoreCase("MOTIVO_INCORRETO")) {
                punishManager.deletePunish(args[0].toLowerCase());

                for(String line : MessageHelper.msgList("unPunishBroadcast")) {
                    String messageLine = line.replace("@player", args[0]).replace("@reason", "Motivo Incorreto").replace("@autor", p.getName());
                    Bukkit.broadcastMessage(messageLine);
                }
                p.sendMessage(messageConfig.getString("Mensagens.unPunishSucess").replaceAll("&", "§"));

            }else if(args[1].equalsIgnoreCase("PROVA_INCORRETA")) {
                punishManager.deletePunish(args[0].toLowerCase());

                for(String line : MessageHelper.msgList("unPunishBroadcast")) {
                    String messageLine = line.replace("@player", args[0]).replace("@reason", "Prova Incorreta").replace("@autor", p.getName());
                    Bukkit.broadcastMessage(messageLine);
                }
                p.sendMessage(messageConfig.getString("Mensagens.unPunishSucess").replaceAll("&", "§"));
            }else if(args[1].equalsIgnoreCase("JOGADOR_DESBANIDO")) {
                punishManager.deletePunish(args[0].toLowerCase());

                for(String line : MessageHelper.msgList("unPunishBroadcast")) {
                    String messageLine = line.replace("@player", args[0]).replace("@reason", "Usuario Desbanido").replace("@autor", p.getName());
                    Bukkit.broadcastMessage(messageLine);
                }
                p.sendMessage(messageConfig.getString("Mensagens.unPunishSucess").replaceAll("&", "§"));
            }
        }
        return true;
    }

    public static void sendReasons(Player p, List<ChatObject> obj) {
        ArrayList<TextComponent> list = new ArrayList<>();
        for (ChatObject co : obj) {
            TextComponent c = new TextComponent(TextComponent.fromLegacyText(co.getMensagem()));
            c.setHoverEvent(co.getHoverEvent());
            c.setClickEvent(co.getClickEvent());
            list.add(c);
        }
        list.forEach(a -> p.spigot().sendMessage(a));
    }
}
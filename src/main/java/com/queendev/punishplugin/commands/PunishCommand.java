package com.queendev.punishplugin.commands;

import com.queendev.punishplugin.Punish;
import com.queendev.punishplugin.managers.ConfigManager;
import com.queendev.punishplugin.managers.PunishManager;
import com.queendev.punishplugin.models.ReasonModel;
import com.queendev.punishplugin.utils.ChatObject;
import com.queendev.punishplugin.utils.DiscordWebhook;
import com.queendev.punishplugin.utils.MessageHelper;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PunishCommand implements CommandExecutor {
    @SneakyThrows
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
                p.sendMessage("§cUso: /punir <jogador> <prova>.");
                return false;
            }

            if(args.length == 1) {
                p.sendMessage(messageConfig.getString("Mensagens.punishNoProva").replaceAll("&", "§"));
                return false;
            }

            if(args.length == 2) {
                if(!args[1].startsWith("https://")) {
                    p.sendMessage(messageConfig.getString("Mensagens.punishProvaInvalid").replaceAll("&", "§"));
                    return false;
                }

                p.sendMessage(messageConfig.getString("Mensagens.punishReasonSelect").replaceAll("&", "§").replace("@player", args[0]));
                p.sendMessage(" ");

                ArrayList<ChatObject> list = new ArrayList<>();
                HashMap<String, ReasonModel> reasons = Punish.getPlugin().getReasonManager().getReasons();
                for(ReasonModel reason : reasons.values()) {
                    String timePunish = reason.getTime() == 0 ? "§cPERMANENTE" : reason.getTime() + " HORA(S)";
                    if(p.hasPermission(reason.getPermission())) {
                        list.add(new ChatObject(reason.getLabel(),
                                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(reason.getLabel() + "\n" + "\n" + reason.getDescription() + "\n" + "\n" + "§eTipo de punição: §7" + reason.getType() + "\n" + "§eTempo da punição: §7" + timePunish).create()),
                                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                        "/punir " + args[0] + " " + args[1] + " " + reason.getName())));
                    }
                }

                sendReasons(p, list);
                return false;
            }

            if(args.length > 3) {
                p.sendMessage(messageConfig.getString("Mensagens.punishProvaInvalid").replaceAll("&", "§"));
                return false;
            }

            if(args.length == 3) {
                HashMap<String, ReasonModel> reasons = Punish.getPlugin().getReasonManager().getReasons();
                for(ReasonModel reason : reasons.values()) {
                    if(args[2].equalsIgnoreCase(reason.getName())) {
                        if(p.hasPermission(reason.getPermission())) {
                            PunishManager punishManager = Punish.getPlugin().getPunishManager();
                            punishManager.createPunish(args[0].toLowerCase(), p.getName(), reason.getType(), reason.getName(), args[1], reason.getTime());
                            String timePunish = reason.getTime() == 0 ? "§cPERMANENTE" : reason.getTime() + " HORA(S)";
                            for(String line : MessageHelper.msgList("punishPermaBroadcast")) {
                                String messageLine = line.replace("@player", args[0]).replace("@reason", reason.getLabel()).replace("@autor", p.getName()).replace("@prova", args[1]).replace("@time", timePunish);
                                Bukkit.broadcastMessage(messageLine);
                            }
                            if(config.getBoolean("Webhook.ativar") == true) {
                                if(config.getString("Webhook.url") != null) {
                                    DiscordWebhook punishWebhook = new DiscordWebhook(config.getString("Webhook.url"));
                                    punishWebhook.addEmbed(new DiscordWebhook.EmbedObject()
                                            .setTitle("Punição - " + args[0])
                                            .setDescription("Nova punição foi aplicada!")
                                            .setColor(Color.RED)
                                            .setThumbnail("https://mc-heads.net/avatar/" + args[0])
                                            .addField("Motivo:", reason.getLabel(), true)
                                            .addField("Autor:", p.getName(), false)
                                            .addField("Infrator:", args[0], false)
                                            .addField("Prova:", args[1], true)
                                            .addField("Tempo (Em horas):", reason.getTime().toString(), true)
                                            .setFooter("Punição aplicada com sucesso.", "https://mc-heads.net/avatar/" + args[0])
                                    );
                                    punishWebhook.execute();
                                }else {
                                    Bukkit.getConsoleSender().sendMessage("§cA opção 'Webhook' está ativada, mas a url está vazia, arrume!");
                                }
                            }
                            p.sendMessage(messageConfig.getString("Mensagens.punishSucess").replaceAll("&", "§"));

                            Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                            if(target != null) {
                                target.kickPlayer(messageConfig.getString("Mensagens.punishKick").replaceAll("&", "§").replace("@player", args[0]).replace("@reason", reason.getLabel()).replace("@autor", p.getName()).replace("@prova", args[1]).replace("@time", timePunish));
                            }
                        }else {
                            p.sendMessage("§cVocê não tem a permissão para aplicar este tipo de punição.");
                            return false;
                        }
                    }
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

package com.queendev.punishplugin.commands;

import com.queendev.punishplugin.Punish;
import com.queendev.punishplugin.managers.ConfigManager;
import com.queendev.punishplugin.managers.PunishManager;
import com.queendev.punishplugin.models.PunishModel;
import com.queendev.punishplugin.utils.ItemBuilder;
import com.queendev.punishplugin.utils.Scroller;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class CheckPunishCommand implements CommandExecutor {
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
            p.sendMessage("§cUso: /unpunish <jogador>.");
            return false;
        }

        PunishManager punishManager = Punish.getPlugin().getPunishManager();
        PunishModel punish = punishManager.getPunish(args[0].toLowerCase());

        ArrayList<ItemStack> items = new ArrayList<>();

        if(punish != null) {
            String timePunish = punish.getTime() == 0 ? "§cPERMANENTE" : punish.getTime() + " HORA(S)";
            ItemStack it = new ItemBuilder(new ItemStack(Material.ANVIL, 1, (short) 0))
                    .setName(args[0])
                    .setLore(config.getStringList("CheckPunishMenu.punish_item.lore").stream().map(line -> line.replace("@player", args[0]).replace("@reason", punish.getReason()).replace("@author", punish.getAuthor()).replace("@prova", punish.getProva()).replace("@time", timePunish).replace("&", "§")).collect(Collectors.toList()))
                    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .toItemStack();
            items.add(it);
        }

        int size = 9 * 3;

        ItemStack nothingItem = null;
        if (items.isEmpty()) {
            nothingItem = new ItemBuilder(new ItemStack(Material.WEB, 1, (short) 0))
                    .setName(config.getString("CheckPunishMenu.nothing_item.name").replace("&", "§"))
                    .setLore(config.getStringList("CheckPunishMenu.nothing_item.lore").stream().map(line -> line.replace("&", "§")).collect(Collectors.toList()))
                    .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .toItemStack();
        }

        Scroller scroller = Scroller.builder()
                .withName("Consulta de punições:")
                .withItems(items.isEmpty() ? Collections.singletonList(nothingItem) : items)
                .withSize(size)
                .withAllowedSlots(Collections.singletonList(13))
                .build();
        scroller.open(p);
        return true;
    }
}

package com.queendev.punishplugin.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class ItemHelper {

    public static ItemStack create(String material, short data, String name, List<String> lore, boolean glow) {
        ItemStack it;
        Material m = Material.valueOf(material);
        it = new ItemStack(m, 1, data);
        name = name.replace("&", "§");
        List<String> newLore = lore.stream().map(line -> line.replace("&", "§")).collect(Collectors.toList());
        ItemMeta mt = it.getItemMeta();
        mt.setDisplayName(name);
        mt.setLore(newLore);
        mt.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        it.setItemMeta(mt);
        return it;
    }
}

package com.yd.rankwar.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {
    public static ItemStack createItem(Material mat, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if(displayName!=null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if(lore!=null && !lore.isEmpty()) {
            for(int i=0;i<lore.size();i++){
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }
}
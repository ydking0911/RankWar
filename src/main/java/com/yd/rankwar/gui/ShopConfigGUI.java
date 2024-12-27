package com.yd.rankwar.gui;

import com.yd.rankwar.managers.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ShopConfigGUI {
    public static final String TITLE_PREFIX = "상점설정: ";

    public static void openShopConfigGUI(Player p, String shopType, ShopManager shopManager) {
        Inventory inv = Bukkit.createInventory(null, 54, TITLE_PREFIX+shopType);

        List<ShopManager.ItemInfo> items = shopManager.getShopItems(shopType);
        for (int i=0; i<items.size() && i<54; i++) {
            inv.setItem(i, items.get(i).itemStack.clone());
        }

        p.openInventory(inv);
    }

    public static boolean isShopConfigInventory(String title) {
        return title.startsWith(TITLE_PREFIX);
    }

    public static String getShopTypeFromTitle(String title) {
        if(isShopConfigInventory(title)) {
            return title.substring(TITLE_PREFIX.length());
        }
        return null;
    }
}
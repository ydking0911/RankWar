package com.yd.rankwar.gui;

import com.yd.rankwar.managers.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class PlayerShopGUI {
    public static final String TITLE = "플레이어 상점";

    public static void openSpecificShopGUI(Player p, ShopManager shopManager, String shopType) {
        Inventory inv = Bukkit.createInventory(null, 54, shopType+" 상점");
        List<ShopManager.ItemInfo> items = shopManager.getShopItems(shopType);
        for (int i=0; i<items.size() && i<54; i++) {
            inv.setItem(i, items.get(i).itemStack.clone());
        }
        p.openInventory(inv);
    }


}
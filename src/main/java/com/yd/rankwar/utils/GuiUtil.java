package com.yd.rankwar.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiUtil {
    public static void fillEmptySlots(Inventory inv, ItemStack filler) {
        for(int i=0;i<inv.getSize();i++) {
            if(inv.getItem(i)==null) {
                inv.setItem(i, filler);
            }
        }
    }
}
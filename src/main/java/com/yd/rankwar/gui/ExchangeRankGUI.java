package com.yd.rankwar.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ExchangeRankGUI {
    public static final String TITLE = "등급 교환 메뉴";

    public static void openExchangeRankGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        // 10번 슬롯: 무기 상점
        ItemStack weapon = new ItemStack(Material.IRON_SWORD);
        ItemMeta wm = weapon.getItemMeta();
        wm.setDisplayName("무기 상점 열기");
        wm.setLore(Arrays.asList(ChatColor.GRAY + "클릭 시 무기 상점을 엽니다."));
        weapon.setItemMeta(wm);
        inv.setItem(10, weapon);

        // 13번 슬롯: 체력 상점
        ItemStack health = new ItemStack(Material.APPLE);
        ItemMeta hm = health.getItemMeta();
        hm.setDisplayName("체력 상점 열기");
        hm.setLore(Arrays.asList(ChatColor.GRAY + "클릭 시 체력 상점을 엽니다."));
        health.setItemMeta(hm);
        inv.setItem(13, health);

        // 16번 슬롯: 마나 상점
        ItemStack mana = new ItemStack(Material.LAPIS_LAZULI);
        ItemMeta mm = mana.getItemMeta();
        mm.setDisplayName("기본 상점 열기");
        mm.setLore(Arrays.asList(ChatColor.GRAY + "클릭 시 기본 상점을 엽니다."));
        mana.setItemMeta(mm);
        inv.setItem(16, mana);

        p.openInventory(inv);
    }
}
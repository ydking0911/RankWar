package com.yd.rankwar.gui;

import com.yd.rankwar.items.SpecialItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class AdminItemGUI {
    public static final String TITLE = "관리자 아이템 목록";

    public static void openAdminItemGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, TITLE);

        // 각 아이템별로 DisplayName과 Lore 설정
        inv.setItem(0, createItem(Material.PAPER, SpecialItems.RANK_UP, "본인 등급을 한 단계 상승시킵니다."));
        inv.setItem(1, createItem(Material.PAPER, SpecialItems.RANDOM_RANK_ALL_NAME, "모든 유저의 등급을 재배치합니다."));
        inv.setItem(2, createItem(Material.PAPER, SpecialItems.RANDOM_RANK_DOWN_OTHER_NAME, "본인을 제외한 임의의 플레이어 등급을 하락시킵니다"));
        inv.setItem(3, createItem(Material.PAPER, SpecialItems.RANDOM_PLAYER_RANK_CHANGE_NAME, "자신 제외 랜덤 플레이어 등급 변경"));
        inv.setItem(4, createItem(Material.PAPER, SpecialItems.NEAR_PLAYER_RANK_HINT_NAME, "사용 시 가장 가까운 유저 등급 힌트를 줍니다.", "두 힌트 중 하나는 진실, 하나는 거짓입니다."));
        inv.setItem(5, createItem(Material.ENDER_PEARL, SpecialItems.RANDOM_TELEPORT_SWAP_NAME, "플레이어 중 한 명과 위치를 변경합니다."));
        inv.setItem(6, createItem(Material.ENDER_EYE, SpecialItems.RANDOM_SUMMON_NAME, "플레이어 중 한 명을 눈앞에 소환합니다."));
        inv.setItem(7, createItem(Material.GOLD_INGOT, SpecialItems.POINT_INTEREST_UP_NAME, "포인트 이자를 +1 증가시킵니다."));
        inv.setItem(8, createItem(Material.EMERALD, SpecialItems.POINT_PLUS_ONE_NAME, "즉시 포인트 1을 획득합니다."));

        p.openInventory(inv);
    }

    private static ItemStack createItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> l = new ArrayList<>();
        for(String s : lore) {
            l.add("§7" + s);
        }
        meta.setLore(l);
        item.setItemMeta(meta);
        return item;
    }
}
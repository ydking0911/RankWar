package com.yd.rankwar.gui;


import com.yd.rankwar.managers.RankManager;
import com.yd.rankwar.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RankSettingsGUI {
    public static final String TITLE = "등급 설정";

    // Rank 색상에 대응하는 양털, 그리고 유리판으로 표시
    private static final Rank[] RANK_ORDER = {Rank.RED, Rank.ORANGE, Rank.YELLOW, Rank.GREEN, Rank.BLUE, Rank.BLACK, Rank.WHITE};
    private static final int[] RANK_SLOTS = {10,11,12,13,14,15,16};
    private static final int[] PANE_SLOTS = {19,20,21,22,23,24,25};

    public static void openRankSettingsGUI(org.bukkit.plugin.Plugin plugin, Player p, RankManager rankManager) {
        Inventory inv = Bukkit.createInventory(null, 36, TITLE);

        // 양털 색상: 빨,주,노,초,파,검,흰 순서
        Material[] wools = {Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL,
                Material.GREEN_WOOL, Material.BLUE_WOOL, Material.BLACK_WOOL, Material.WHITE_WOOL};

        for(int i=0;i<RANK_ORDER.length;i++) {
            Rank r = RANK_ORDER[i];
            ItemStack wool = new ItemStack(wools[i]);
            ItemMeta meta = wool.getItemMeta();
            meta.setDisplayName(r.getColor()+r.name()+" 등급");
            wool.setItemMeta(meta);
            inv.setItem(RANK_SLOTS[i], wool);

            // 활성 상태에 따른 판넬
            boolean enabled = rankManager.isRankEnabled(r);
            Material paneMat = enabled ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
            ItemStack pane = new ItemStack(paneMat);
            ItemMeta pm = pane.getItemMeta();
            pm.setDisplayName((enabled?ChatColor.GREEN:"§c")+"클릭하여 토글");
            pane.setItemMeta(pm);

            inv.setItem(PANE_SLOTS[i], pane);
        }

        p.openInventory(inv);
    }

    public static boolean isRankSlot(int slot) {
        for(int s : PANE_SLOTS) {
            if(s == slot) return true;
        }
        return false;
    }

    public static Rank getRankFromSlot(int slot) {
        for(int i=0;i<PANE_SLOTS.length;i++) {
            if(PANE_SLOTS[i] == slot) {
                return RANK_ORDER[i];
            }
        }
        return null;
    }
}
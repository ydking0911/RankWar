package com.yd.rankwar.gui;

import com.yd.rankwar.items.SpecialItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminItemGUI {
    public static final String TITLE = "관리자 아이템 목록";

    public static void openAdminItemGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);

        // 각 아이템별로 DisplayName과 Lore 설정
        addItemToInventory(inventory, 0, Material.PAPER, SpecialItems.RANK_UP, "본인 등급을 한 단계 상승시킵니다.");
        addItemToInventory(inventory, 1, Material.PAPER, SpecialItems.RANDOM_RANK_ALL_NAME, "모든 유저의 등급을 재배치합니다.");
        addItemToInventory(inventory, 2, Material.PAPER, SpecialItems.RANDOM_RANK_DOWN_OTHER_NAME, "본인을 제외한 임의의 플레이어 등급을 하락시킵니다.");
        addItemToInventory(inventory, 3, Material.PAPER, SpecialItems.RANDOM_PLAYER_RANK_CHANGE_NAME, "자신 제외 랜덤 플레이어 등급 변경");
        addItemToInventory(inventory, 4, Material.PAPER, SpecialItems.NEAR_PLAYER_RANK_HINT_NAME,
                "사용 시 가장 가까운 유저 등급 힌트를 줍니다.", "두 힌트 중 하나는 진실, 하나는 거짓입니다.");
        addItemToInventory(inventory, 5, Material.ENDER_PEARL, SpecialItems.RANDOM_TELEPORT_SWAP_NAME, "플레이어 중 한 명과 위치를 변경합니다.");
        addItemToInventory(inventory, 6, Material.ENDER_EYE, SpecialItems.RANDOM_SUMMON_NAME, "플레이어 중 한 명을 눈앞에 소환합니다.");
        addItemToInventory(inventory, 7, Material.GOLD_INGOT, SpecialItems.POINT_INTEREST_UP_NAME, "포인트 이자를 +1 증가시킵니다.");
        addItemToInventory(inventory, 8, Material.EMERALD, SpecialItems.POINT_PLUS_ONE_NAME, "즉시 포인트 1을 획득합니다.");

        player.openInventory(inventory);
    }

    private static void addItemToInventory(Inventory inventory, int slot, Material material, String name, String... lore) {
        ItemStack item = createItem(material, name, lore);
        if (item != null) {
            inventory.setItem(slot, item);
        }
    }

    private static ItemStack createItem(Material material, String displayName, String... lore) {
        if (material == null) {
            Bukkit.getLogger().warning("[AdminItemGUI] Material이 null입니다. 아이템 생성이 중단되었습니다.");
            return null;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            List<String> formattedLore = new ArrayList<>();
            for (String line : lore) {
                formattedLore.add("§7" + line); // 회색 색상으로 표시
            }
            meta.setLore(formattedLore);
            item.setItemMeta(meta);
        } else {
            Bukkit.getLogger().warning("[AdminItemGUI] " + material + "의 ItemMeta를 설정할 수 없습니다.");
        }

        return item;
    }
}
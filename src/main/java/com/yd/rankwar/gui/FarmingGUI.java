
package com.yd.rankwar.gui;

import com.yd.rankwar.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FarmingGUI {

    public static final String TITLE = "파밍 아이템 설정";
    private final ConfigUtil configUtil;

    public FarmingGUI(ConfigUtil configUtil) {
        this.configUtil = configUtil;
    }

    public void openFarmingGUI(Player player) {
        List<ItemStack> configItems = configUtil.loadFarmingItems();
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);

        // config에 저장된 아이템을 GUI에 채워넣음
        for (int i = 0; i < configItems.size() && i < 54; i++) {
            inventory.setItem(i, configItems.get(i));
        }

        player.openInventory(inventory);
    }
}
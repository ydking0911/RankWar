package com.yd.rankwar.listeners;

import com.yd.rankwar.managers.RegionManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WandListener implements Listener {

    private final RegionManager regionManager;

    public WandListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // 손에 들고 있는 아이템이 나무도끼인지 확인
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() != Material.WOODEN_AXE) {
            return;
        }

        // 블록 클릭 (좌클릭, 우클릭)
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            // pos1 설정
            Location loc = event.getClickedBlock().getLocation();
            regionManager.setPos1(loc);
            player.sendMessage(ChatColor.GREEN + "[pos1] "
                    + ChatColor.YELLOW + locToString(loc));
            event.setCancelled(true); // 블록 파괴 방지
        }
        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // pos2 설정
            Location loc = event.getClickedBlock().getLocation();
            regionManager.setPos2(loc);
            player.sendMessage(ChatColor.GREEN + "[pos2] "
                    + ChatColor.YELLOW + locToString(loc));
            event.setCancelled(true); // 우클릭 동작 방지
        }
    }

    private String locToString(Location loc) {
        return String.format("(%d, %d, %d)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
}
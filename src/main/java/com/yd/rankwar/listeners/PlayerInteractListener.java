package com.yd.rankwar.listeners;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.items.SpecialItems;
import com.yd.rankwar.managers.GameManager;
import com.yd.rankwar.managers.RankManager;
import com.yd.rankwar.managers.ShopManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    private RankWar plugin;
    private ShopManager shopManager;
    private RankManager rankManager;
    private GameManager gameManager;

    public PlayerInteractListener(RankWar plugin, ShopManager shopManager, RankManager rankManager, GameManager gameManager) {
        this.plugin = plugin;
        this.shopManager = shopManager;
        this.rankManager = rankManager;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if(p.isSneaking()) {
            e.setCancelled(true);
            p.performCommand("등급교환");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = p.getInventory().getItemInMainHand();
            if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String displayName = item.getItemMeta().getDisplayName();
                // SpecialItems에 정의된 PREFIX를 가진 아이템이면 우클릭 사용 불가
                if (displayName.contains(SpecialItems.PREFIX)) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
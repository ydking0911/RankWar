package com.yd.rankwar.listeners;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.gui.*;
import com.yd.rankwar.managers.GameManager;
import com.yd.rankwar.managers.RankManager;
import com.yd.rankwar.managers.ShopManager;
import com.yd.rankwar.utils.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiClickListener implements Listener {
    private RankManager rankManager;
    private ShopManager shopManager;
    private GameManager gameManager;
    private RankWar plugin;

    public GuiClickListener(RankWar plugin, RankManager rankManager, ShopManager shopManager, GameManager gameManager) {
        this.rankManager = rankManager;
        this.shopManager = shopManager;
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player)e.getWhoClicked();
        String title = e.getView().getTitle();
        int slot = e.getRawSlot();

        if(title.equals(AdminItemGUI.TITLE)) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null && e.getCurrentItem().getType()!= Material.AIR) {
                p.getInventory().addItem(e.getCurrentItem());
                p.sendMessage("아이템을 지급했습니다.");
            }
        } else if(title.equals(RankSettingsGUI.TITLE)) {
            e.setCancelled(true);
            if(RankSettingsGUI.isRankSlot(slot)) {
                Rank clickedRank = RankSettingsGUI.getRankFromSlot(slot);
                if(clickedRank != null) {
                    boolean current = rankManager.isRankEnabled(clickedRank);
                    rankManager.setRankEnabled(clickedRank, !current);
                    p.sendMessage(clickedRank.name()+" 등급 활성 상태: "+(!current));
                    RankSettingsGUI.openRankSettingsGUI(plugin, p, rankManager);
                }
            }
        } else if(ShopConfigGUI.isShopConfigInventory(title)) {
            e.setCancelled(true);
            if(e.getCurrentItem()==null || e.getCurrentItem().getType()==Material.AIR) return;
            String shopType = ShopConfigGUI.getShopTypeFromTitle(title);
            if(shopType != null) {
                // 클릭한 슬롯의 아이템 제거
                shopManager.removeItemByIndex(shopType, slot);
                p.sendMessage("해당 아이템을 상점에서 제거했습니다.");
                ShopConfigGUI.openShopConfigGUI(p, shopType, shopManager);
            }
        } else if(title.equals(ExchangeRankGUI.TITLE)) {
            e.setCancelled(true);
            if(e.getCurrentItem()==null || e.getCurrentItem().getType()==Material.AIR) return;

            if(slot == 10) {
                PlayerShopGUI.openSpecificShopGUI(p, shopManager, "weapon");
            } else if(slot == 13) {
                PlayerShopGUI.openSpecificShopGUI(p, shopManager, "health");
            } else if(slot == 16) {
                PlayerShopGUI.openSpecificShopGUI(p, shopManager, "mana");
            }
        } else if(ChatColor.stripColor(title).endsWith(" 상점")) {
            // 구매 상점 GUI
            e.setCancelled(true);
            if(e.getCurrentItem()==null || e.getCurrentItem().getType()==Material.AIR) return;
            String stripped = ChatColor.stripColor(title);
            String shopType = stripped.replace(" 상점","").trim();

            int currentPoints = plugin.getPointManager().getPoints(p);
            shopManager.buyItem(p, shopType, slot, currentPoints, plugin.getPointManager());
        }
    }
}
package com.yd.rankwar.listeners;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.gui.FarmingGUI;
import com.yd.rankwar.managers.RegionManager;
import com.yd.rankwar.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FarmingListener implements Listener {

    private final RankWar plugin;
    private final RegionManager regionManager;
    private final ConfigUtil configUtil;
    private final Set<Chest> scheduledChests = new HashSet<>();

    public FarmingListener(RankWar plugin, RegionManager regionManager, ConfigUtil configUtil) {
        this.plugin = plugin;
        this.regionManager = regionManager;
        this.configUtil = configUtil;
    }

    // ----------------------------------------------------------------------
    // [A] 게임 시작 시, 영역 내 모든 상자에 '즉시' 아이템 1개씩 넣기
    // ----------------------------------------------------------------------
    public void populateChestsInRegion() {
        if (!regionManager.isRegionSet()) {
            Bukkit.getLogger().warning("[파밍시스템] 영역이 설정되지 않았습니다.");
            return;
        }
        List<ItemStack> items = configUtil.loadFarmingItems();
        if (items == null || items.isEmpty()) {
            Bukkit.getLogger().warning("[파밍시스템] 아이템 리스트가 비어 있습니다. 파밍설정을 확인하세요.");
            return;
        }

        Bukkit.getLogger().info("[파밍시스템] populateChestsInRegion 메서드 실행. 영역 내 상자를 채웁니다.");

        World world = regionManager.getPos1().getWorld();
        int minX = regionManager.getMinX();
        int maxX = regionManager.getMaxX();
        int minY = regionManager.getMinY();
        int maxY = regionManager.getMaxY();
        int minZ = regionManager.getMinZ();
        int maxZ = regionManager.getMaxZ();

        int chestCount = 0;
        Random random = new Random();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getState() instanceof Chest) {
                        Chest chest = (Chest) block.getState();

                        // 상자가 비어있는지 확인
                        if (chest.getInventory().isEmpty()) {
                            ItemStack randomItem = items.get(random.nextInt(items.size()));
                            chest.getInventory().addItem(randomItem);
                            chest.update();

                            chestCount++;
                            Bukkit.getLogger().info("[파밍시스템] 상자에 아이템 추가: " + chest.getLocation());
                        }
                    }
                }
            }
        }
        Bukkit.getLogger().info("[파밍시스템] " + chestCount + " 개의 상자가 적용되었습니다.");
    }

    // ----------------------------------------------------------------------
    // [B] 1) '파밍 아이템 설정' GUI를 닫을 때 → config 저장
    //     2) 상자를 닫을 때 → 아이템을 전부 가져가 비었으면 5분 뒤 새 아이템 추가
    // ----------------------------------------------------------------------
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // 1) '파밍 아이템 설정' GUI를 닫았는지 확인
        String title = event.getView().getTitle();
        if (title.equals(FarmingGUI.TITLE)) {
            Inventory inv = event.getInventory();
            List<ItemStack> itemsToSave = new ArrayList<>();
            for (ItemStack item : inv.getContents()) {
                if (item != null) {
                    itemsToSave.add(item.clone());
                }
            }
            configUtil.saveFarmingItems(itemsToSave);

            Player p = (Player) event.getPlayer();
            p.sendMessage(ChatColor.GREEN + "파밍 아이템 설정이 저장되었습니다!");
            return;
        }

        // 2) 상자 인벤토리를 닫았을 때
        if (event.getInventory().getHolder() instanceof Chest) {
            Chest chest = (Chest) event.getInventory().getHolder();

            Bukkit.getLogger().info("[파밍시스템] 상자 닫힘 이벤트 감지. 위치: " + chest.getLocation());

            // 게임 상태 및 영역 내 상자인지 확인
            if (!plugin.getGameManager().isGameStarted() ||
                    !regionManager.isRegionSet() ||
                    !regionManager.isInRegion(chest.getLocation())) {
                return;
            }

            // 상자가 비어 있는지 확인
            boolean isEmpty = true;
            for (ItemStack item : event.getInventory().getContents()) {
                if (item != null) {
                    isEmpty = false;
                    break;
                }
            }

            // 상자가 비어 있고, 스케줄이 실행되지 않은 경우에만 실행
            if (isEmpty && scheduledChests.add(chest)) {
                Bukkit.getLogger().info("[파밍시스템] 상자가 비어 있습니다. 5분 타이머 시작: " + chest.getLocation());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // 5분 후 아이템 재생성
                        List<ItemStack> items = configUtil.loadFarmingItems();
                        if (items == null || items.isEmpty()) {
                            Bukkit.getLogger().warning("[파밍시스템] 파밍 아이템이 비어 있습니다.");
                            return;
                        }
                        ItemStack randomItem = items.get(new Random().nextInt(items.size()));

                        chest.getInventory().addItem(randomItem);
                        chest.update();

                        // 작업 완료 후 추적에서 제거
                        scheduledChests.remove(chest);
                        Bukkit.getLogger().info("[파밍시스템] 아이템이 상자에 추가되었습니다: " + chest.getLocation());
                    }
                }.runTaskLater(plugin, 6000L); // 5분 후
            } else {
                Bukkit.getLogger().info("[파밍시스템] 이미 스케줄러가 실행 중인 상자입니다: " + chest.getLocation());
            }
        }
    }
}
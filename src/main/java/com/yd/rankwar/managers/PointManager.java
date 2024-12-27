package com.yd.rankwar.managers;

import com.yd.rankwar.RankWar;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointManager {
    private RankWar plugin;
    private RankManager rankManager;
    private Map<UUID, Integer> points = new HashMap<>();
    private Map<UUID, Integer> interest = new HashMap<>();
    private int taskId = -1;

    public PointManager(RankWar plugin, RankManager rankManager) {
        this.plugin = plugin;
        this.rankManager = rankManager;
    }

    public void initializePlayer(Player p) {
        points.putIfAbsent(p.getUniqueId(), 0);
        interest.putIfAbsent(p.getUniqueId(), 0);
    }

    public void addPoints(Player p, int amount) {
        points.put(p.getUniqueId(), points.getOrDefault(p.getUniqueId(), 0) + amount);
        // Sidebar가 아닌 개인별 Footer 갱신
        rankManager.updatePersonalInfo(p);
    }

    public void addInterest(Player p, int amt) {
        interest.put(p.getUniqueId(), interest.getOrDefault(p.getUniqueId(), 0) + amt);
        rankManager.updatePersonalInfo(p);
    }

    public int getPoints(Player p) {
        return points.getOrDefault(p.getUniqueId(), 0);
    }

    // 이자 반환 메서드 추가
    public int getInterest(Player p) {
        return interest.getOrDefault(p.getUniqueId(), 0);
    }

    // 살아있는 참가자 수 계산 메서드
    public int getAlivePlayersCount() {
        int count = 0;
        for(Player pl : Bukkit.getOnlinePlayers()) {
            if (rankManager.isParticipant(pl) && pl.getGameMode() != GameMode.SPECTATOR) {
                count++;
            }
        }
        return count;
    }

    public void startPointTask() {
        if (taskId != -1) return; // 이미 돌고 있으면 재시작 필요 없음
        taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (UUID uuid : points.keySet()) {
                int current = points.getOrDefault(uuid, 0);
                int plus = 1 + interest.getOrDefault(uuid, 0);
                points.put(uuid, current + plus);

                Player p = Bukkit.getPlayer(uuid);
                if (p != null && p.isOnline()) {
                    rankManager.updatePersonalInfo(p);
                    rankManager.updateGlobalInfo();
                }
            }
        }, 200L, 200L);
    }

    public void resetPoints() {
        if (taskId != -1) {
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = -1;
        }
        points.clear();
        interest.clear();
    }
}
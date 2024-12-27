package com.yd.rankwar.managers;

import com.yd.rankwar.RankWar;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class GameManager {
    private RankWar plugin;
    private RankManager rankManager;
    private PointManager pointManager;
    private ShopManager shopManager;

    private boolean gameStarted = false;


    public GameManager(RankWar plugin, RankManager rankManager, PointManager pointManager, ShopManager shopManager) {
        this.plugin = plugin;
        this.rankManager = rankManager;
        this.pointManager = pointManager;
        this.shopManager = shopManager;
    }

    public void startGame() {
        if (gameStarted) return;
        gameStarted = true;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (rankManager.isParticipant(p)) {
                pointManager.initializePlayer(p);
            }
        }

        rankManager.assignRandomRanks();
        pointManager.startPointTask();

        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatColor.GREEN + "등급전쟁이 시작되었습니다!");
        Bukkit.broadcastMessage(" ");
    }

    public void endGame() {
        if(!gameStarted) return;
        gameStarted = false;
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatColor.RED + "등급전쟁이 종료되었습니다!");
        Bukkit.broadcastMessage(" ");

        // 등급 초기화 및 팀 해제
        rankManager.resetAllPlayersTeam();
        rankManager.clearRanks();

        // 포인트 증가 중단 및 초기화
        pointManager.resetPoints();

        World w = Bukkit.getWorld("world");
        if (w == null) {
            Bukkit.getLogger().warning("Cannot find world 'world'. Check your server world name.");
            // 여기서 return 하거나, spawn을 null로 두고 null 체크한 후 행동
            return;
        }

        Location spawn = w.getSpawnLocation();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode() == GameMode.SPECTATOR) {
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(spawn);
            }
        }


    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}
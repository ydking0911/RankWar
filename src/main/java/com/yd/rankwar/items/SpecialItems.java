package com.yd.rankwar.items;

import com.yd.rankwar.managers.PointManager;
import com.yd.rankwar.managers.RankManager;
import com.yd.rankwar.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SpecialItems {
    // 아이템 식별용 DisplayName 상수
    public static final String PREFIX = ChatColor.RED + "<< " + ChatColor.WHITE + "Rank" + ChatColor.RED + " >> ";
    public static final String RANK_UP = PREFIX + ChatColor.YELLOW + "등급 상승권";
    public static final String RANDOM_RANK_ALL_NAME = PREFIX + ChatColor.YELLOW + "등급 랜덤 설정권";
    public static final String RANDOM_RANK_DOWN_OTHER_NAME = PREFIX + ChatColor.YELLOW + "등급 랜덤 하락권";
    public static final String RANDOM_PLAYER_RANK_CHANGE_NAME = PREFIX + ChatColor.YELLOW + "랜덤 플레이어 등급 변경권";
    public static final String NEAR_PLAYER_RANK_HINT_NAME = PREFIX + ChatColor.YELLOW + "가까운 유저 등급 힌트권";
    public static final String RANDOM_TELEPORT_SWAP_NAME = PREFIX + ChatColor.YELLOW + "랜덤 위치 변환기";
    public static final String RANDOM_SUMMON_NAME = PREFIX + ChatColor.YELLOW + "랜덤 소환권";
    public static final String POINT_INTEREST_UP_NAME = PREFIX + ChatColor.YELLOW + "포인트 이자 +1";
    public static final String POINT_PLUS_ONE_NAME = PREFIX + ChatColor.YELLOW + "포인트 +1";

    // 등급 힌트 제공 시, 한 개는 진실, 한 개는 거짓.
    // 여기서는 단순히 두 등급 중 하나를 골라 힌트주고, 다른 하나는 랜덤으로 거짓으로 준다.

    public static void useRankUp(Player p, RankManager rankManager) {
        Rank current = rankManager.getPlayerRank(p);
        if (current == null) return;
        Rank next = Rank.getNextRank(current);
        rankManager.setPlayerRank(p, next);
        p.sendMessage("등급이 한 단계 상승하였습니다!");
    }

    public static void useRandomRankAll(RankManager rankManager) {
        rankManager.assignRandomRanks();
        Bukkit.broadcastMessage("모든 유저의 등급이 재배치되었습니다!");
    }


    public static void useRandomRankDownOther(Player user, RankManager rankManager) {
        List<Player> online = Bukkit.getOnlinePlayers().stream().filter(p -> !p.equals(user) && rankManager.isParticipant(p)).collect(Collectors.toList());
        if (online.isEmpty()) {
            user.sendMessage("하락시킬 대상이 없습니다.");
            return;
        }
        Player target = online.get(new Random().nextInt(online.size()));
        Rank cur = rankManager.getPlayerRank(target);
        if (cur == null) return;
        Rank prev = Rank.getPrevRank(cur);
        rankManager.setPlayerRank(target, prev);
        user.sendMessage(target.getName() + "님의 등급을 하락시켰습니다!");
    }

    public static void useRandomChangeOther(Player user, RankManager rankManager) {
        List<Player> online = Bukkit.getOnlinePlayers().stream().filter(p -> !p.equals(user) && rankManager.isParticipant(p)).collect(Collectors.toList());
        if (online.isEmpty()) {
            user.sendMessage("변경할 대상이 없습니다.");
            return;
        }
        Player target = online.get(new Random().nextInt(online.size()));
        Rank randomRank = rankManager.getRandomEnabledRank();
        rankManager.setPlayerRank(target, randomRank);
        user.sendMessage(target.getName() + "님의 등급을 랜덤하게 변경하였습니다!");
    }

    public static void useNearPlayerRankHint(Player user, RankManager rankManager) {
        // 가장 가까운 유저 찾기
        Player nearest = null;
        double dist = Double.MAX_VALUE;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(user) || !rankManager.isParticipant(p)) continue;
            double d = p.getLocation().distance(user.getLocation());
            if (d < dist) {
                dist = d;
                nearest = p;
            }
        }
        if (nearest == null) {
            user.sendMessage("주변에 대상이 없습니다.");
            return;
        }

        Rank realRank = rankManager.getPlayerRank(nearest);
        if (realRank == null) {
            user.sendMessage("대상자의 등급 정보를 알 수 없습니다.");
            return;
        }

        // 가짜 힌트(임의의 다른 등급)
        Rank fakeRank = realRank;
        while (fakeRank == realRank) {
            fakeRank = rankManager.getRandomEnabledRank();
        }

        // 순서 랜덤화
        boolean firstReal = new Random().nextBoolean();
        Rank hint1 = firstReal ? realRank : fakeRank;
        Rank hint2 = firstReal ? fakeRank : realRank;

        user.sendMessage("가장 가까운 유저에 대한 힌트: 두 등급 중 하나가 진실, 다른 하나는 거짓");
        user.sendMessage("힌트1: " + hint1.name());
        user.sendMessage("힌트2: " + hint2.name());
    }

    public static void useRandomTeleportSwap(Player user, RankManager rankManager) {
        List<Player> online = Bukkit.getOnlinePlayers().stream().filter(p->!p.equals(user) && rankManager.isParticipant(p)).collect(Collectors.toList());
        if(online.isEmpty()) {
            user.sendMessage("위치 교환할 대상이 없습니다.");
            return;
        }
        Player target = online.get(new Random().nextInt(online.size()));
        Location userLoc = user.getLocation();
        Location targetLoc = target.getLocation();
        user.teleport(targetLoc);
        target.teleport(userLoc);
        user.sendMessage(target.getName()+"님과 위치를 교환했습니다!");
    }

    public static void useRandomSummon(Player user, RankManager rankManager) {
        List<Player> online = Bukkit.getOnlinePlayers().stream().filter(p->!p.equals(user) && rankManager.isParticipant(p)).collect(Collectors.toList());
        if(online.isEmpty()) {
            user.sendMessage("소환할 대상이 없습니다.");
            return;
        }
        Player target = online.get(new Random().nextInt(online.size()));
        Location frontOfUser = user.getLocation().add(user.getLocation().getDirection().multiply(2));
        target.teleport(frontOfUser);
        user.sendMessage(target.getName()+"님을 눈앞에 소환했습니다!");
    }

    public static void usePointInterestUp(Player p, PointManager pointManager) {
        pointManager.addInterest(p,1);
        p.sendMessage("포인트 이자가 1 증가하였습니다!");
    }

    public static void usePointPlusOne(Player p, PointManager pointManager) {
        pointManager.addPoints(p,1);
        p.sendMessage("포인트를 1 획득했습니다.");
    }
}
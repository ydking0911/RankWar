package com.yd.rankwar.listeners;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.items.SpecialItems;
import com.yd.rankwar.managers.GameManager;
import com.yd.rankwar.managers.PointManager;
import com.yd.rankwar.managers.RankManager;
import com.yd.rankwar.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class GameListener implements Listener {
    private GameManager gameManager;
    private RankManager rankManager;
    private PointManager pointManager;
    private RankWar plugin;

    private static final String PROJECTILE_OWNER_KEY = "owner";
    private static final String ATTACKER_UUID_KEY = "attackerUUID";

    public GameListener(RankWar plugin, GameManager gameManager, RankManager rankManager, PointManager pointManager) {
        this.gameManager = gameManager;
        this.rankManager = rankManager;
        this.pointManager = pointManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!gameManager.isGameStarted()) return;
        // 게임 시작하지 않아도 아이템 사용 가능하게 하려면 조건 제거

        Player p = e.getPlayer();
        if(e.getItem() == null || e.getItem().getType() == Material.AIR) return;

        ItemStack item = e.getItem();
        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        String name = item.getItemMeta().getDisplayName();

        // 아이템 이름 비교 후 해당 효과 적용
        if(name.equals(SpecialItems.RANK_UP)) {
            SpecialItems.useRankUp(p, rankManager);
            consumeItem(p, item);
        } else if(name.equals(SpecialItems.RANDOM_RANK_ALL_NAME)) {
            SpecialItems.useRandomRankAll(rankManager);
            consumeItem(p, item);
        } else if(name.equals(SpecialItems.RANDOM_RANK_DOWN_OTHER_NAME)) {
            SpecialItems.useRandomRankDownOther(p, rankManager);
            consumeItem(p, item);
        } else if(name.equals(SpecialItems.RANDOM_PLAYER_RANK_CHANGE_NAME)) {
            SpecialItems.useRandomChangeOther(p, rankManager);
            consumeItem(p, item);
        } else if(name.equals(SpecialItems.NEAR_PLAYER_RANK_HINT_NAME)) {
            SpecialItems.useNearPlayerRankHint(p, rankManager);
            consumeItem(p, item);
        } else if(name.equals(SpecialItems.RANDOM_TELEPORT_SWAP_NAME)) {
            SpecialItems.useRandomTeleportSwap(p, rankManager);
            consumeItem(p, item);
        } else if(name.equals(SpecialItems.RANDOM_SUMMON_NAME)) {
            SpecialItems.useRandomSummon(p, rankManager);
            consumeItem(p, item);
        } else if(name.equals(SpecialItems.POINT_INTEREST_UP_NAME)) {
            SpecialItems.usePointInterestUp(p, pointManager);
            consumeItem(p, item);
        } else if(name.equals(SpecialItems.POINT_PLUS_ONE_NAME)) {
            SpecialItems.usePointPlusOne(p, pointManager);
            consumeItem(p, item);
        }
    }

    private void consumeItem(Player p, ItemStack item) {
        // 아이템 1개 소모
        int amt = item.getAmount();
        if(amt > 1) {
            item.setAmount(amt-1);
        } else {
            p.getInventory().removeItem(item);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!gameManager.isGameStarted()) return;
        if (!(e.getEntity() instanceof Player)) return; // 피해자가 플레이어인 경우에만 처리

        Player victim = (Player) e.getEntity();
        Player attacker = null;

        // 데미지를 준 엔티티가 투사체인 경우
        if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            NamespacedKey key = new NamespacedKey(plugin, PROJECTILE_OWNER_KEY);
            PersistentDataContainer container = projectile.getPersistentDataContainer();
            if (container.has(key, PersistentDataType.STRING)) {
                String uuidStr = container.get(key, PersistentDataType.STRING);
                try {
                    UUID ownerUUID = UUID.fromString(uuidStr);
                    attacker = Bukkit.getPlayer(ownerUUID);
                } catch (IllegalArgumentException ex) {
                    plugin.getLogger().warning("Invalid UUID format in projectile metadata: " + uuidStr);
                }
            }
        }

        // 데미지를 준 엔티티가 직접적인 플레이어인 경우
        if (e.getDamager() instanceof Player) {
            attacker = (Player) e.getDamager();
        }

        if (attacker != null && rankManager.isParticipant(attacker)) {
            if (rankManager.isAdmin(attacker) || rankManager.isAdmin(victim)) {
                return; // 어드민은 영향을 받지 않는다.
            }

            Rank attackerRank = rankManager.getPlayerRank(attacker);
            Rank victimRank = rankManager.getPlayerRank(victim);
            if(attackerRank == null || victimRank == null) return;

            // 킬 판정
            double finalDamage = e.getFinalDamage();
            double victimHealth = victim.getHealth();
            if(finalDamage >= victimHealth) {
                // 공격자의 UUID를 피해자에게 메타데이터로 저장
                NamespacedKey attackerKey = new NamespacedKey(plugin, ATTACKER_UUID_KEY);
                victim.getPersistentDataContainer().set(attackerKey, PersistentDataType.STRING, attacker.getUniqueId().toString());
            }
        }
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!gameManager.isGameStarted()) return;

        Player victim = event.getEntity();
        NamespacedKey attackerKey = new NamespacedKey(plugin, ATTACKER_UUID_KEY);
        PersistentDataContainer container = victim.getPersistentDataContainer();

        Player killer = null;
        String deathMessage = event.getDeathMessage();

        if (deathMessage == null || deathMessage.isEmpty()) {
            plugin.getLogger().warning("Death message is empty or null.");
            return;
        }

        if (container.has(attackerKey, PersistentDataType.STRING)) {
            String uuidStr = container.get(attackerKey, PersistentDataType.STRING);
            try {
                UUID attackerUUID = UUID.fromString(uuidStr);
                killer = Bukkit.getPlayer(attackerUUID);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Invalid UUID format in attacker metadata: " + uuidStr);
            }
        } else {
            // 영어 메시지 처리: "was shot by", "was killed by", "was burnt to a crisp whilst fighting", "was skewered by", "was engulfed by" 등
            if (deathMessage.contains("was shot by") ||
                    deathMessage.contains("was killed by") ||
                    deathMessage.contains("was burnt to a crisp whilst fighting") ||
                    deathMessage.contains("was skewered by") ||
                    deathMessage.contains("was engulfed by") ||
                    deathMessage.contains("was zapped to death by") ||
                    deathMessage.contains("was slain by")) {
                String[] parts = deathMessage.split(" was ", 2);
                String victimName = parts[0].trim();
                String killerName = parts[1].contains(" by ") ? parts[1].split(" by ")[1].trim() : parts[1].split(" whilst fighting ")[1].trim();
                victim = Bukkit.getPlayer(victimName);
                killer = Bukkit.getPlayer(killerName);
            }
            // 일반적인 한국어 메시지 처리
            else if (deathMessage.contains("님이") && deathMessage.contains("님을")) {
                String[] parts = deathMessage.split("님이", 2);
                String victimPart = parts[1].split("님을")[0].trim();
                killer = Bukkit.getPlayer(victimPart);
            }
            // 추가적인 패턴 처리
            else if (deathMessage.contains("의")) {
                String[] parts = deathMessage.split("의");
                String killerName = parts[0].trim();
                String victimName = parts[1].split("에 의해")[0].trim();
                if (victim.getName().equals(victimName)) {
                    killer = Bukkit.getPlayer(killerName);
                }
            }
        }

        if (killer == null || victim == null || !rankManager.isParticipant(killer) || !rankManager.isParticipant(victim)) return;

        Rank victimRank = rankManager.getPlayerRank(victim);
        Rank killerRank = rankManager.getPlayerRank(killer);

        if (victimRank == null || killerRank == null) return;

        final Player finalVictim = victim;
        if (victimRank.getOrder() < killerRank.getOrder()) {
            rankManager.updatePersonalInfo(killer);
            rankManager.updatePersonalInfo(finalVictim);
            event.setDeathMessage(killer.getName() + "님이 " + finalVictim.getName() + "님을 죽였습니다!");
        } else {
            event.setDeathMessage(killer.getName() + "님이 " + finalVictim.getName() + "님을 죽였습니다!");
            Player finalKiller = killer;
            finalVictim.setHealth(20);
            finalVictim.setFoodLevel(20);
            finalVictim.setGameMode(GameMode.SURVIVAL);
            finalVictim.teleport(finalVictim.getLocation()); // 현재 위치에서 복구

            finalKiller.setHealth(0);
            finalKiller.setGameMode(GameMode.SPECTATOR);
        }

        container.remove(attackerKey);
    }
}
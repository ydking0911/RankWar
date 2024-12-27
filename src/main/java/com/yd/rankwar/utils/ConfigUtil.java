package com.yd.rankwar.utils;

import com.yd.rankwar.RankWar;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {
    private final RankWar plugin;
    private final File configFile;
    private final FileConfiguration config;

    public ConfigUtil(RankWar plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveFarmingItems(List<ItemStack> items) {
        config.set("farmingItems", items); // Bukkit이 YAML 형식으로 자동 직렬화
        saveConfig();
        Bukkit.getLogger().info("[파밍시스템] 파밍 아이템이 YAML 형식으로 저장되었습니다.");
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ItemStack> loadFarmingItems() {
        List<?> rawItems = config.getList("farmingItems", new ArrayList<>()); // YAML 데이터를 가져오기
        List<ItemStack> farmingItems = new ArrayList<>();

        for (Object obj : rawItems) {
            if (obj instanceof ItemStack) {
                farmingItems.add((ItemStack) obj); // ItemStack으로 캐스팅
            } else {
                Bukkit.getLogger().warning("[파밍시스템] YAML 데이터 변환 실패: " + obj);
            }
        }
        return farmingItems;
    }


}
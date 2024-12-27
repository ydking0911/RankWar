package com.yd.rankwar.utils;

import com.yd.rankwar.RankWar;
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
        config.set("farmingItems", items);
        saveConfig();
    }

    public List<ItemStack> loadFarmingItems() {
        return (List<ItemStack>) config.getList("farmingItems", new ArrayList<>());
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
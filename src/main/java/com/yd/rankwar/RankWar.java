package com.yd.rankwar;

import com.yd.rankwar.commands.*;
import com.yd.rankwar.gui.FarmingGUI;
import com.yd.rankwar.listeners.*;
import com.yd.rankwar.managers.*;
import com.yd.rankwar.utils.ConfigUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class RankWar extends JavaPlugin {

    private RankManager rankManager;
    private PointManager pointManager;
    private ShopManager shopManager;
    private GameManager gameManager;
    private ConfigUtil configUtil;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        // Utils
        configUtil = new ConfigUtil(this);

        // Managers
        rankManager = new RankManager(this);
        regionManager = new RegionManager(this);
        pointManager = new PointManager(this, rankManager);
        shopManager = new ShopManager(this, configUtil);
        gameManager = new GameManager(this, rankManager, pointManager, shopManager);

        rankManager.setPointManager(pointManager);

        // Commands
        getCommand("등급전쟁").setExecutor(new StartGameCommand(gameManager, regionManager, configUtil, this, new FarmingListener(this, regionManager, configUtil)));
        getCommand("운영자").setExecutor(new AdminModeCommand(rankManager));
        getCommand("참가자").setExecutor(new ParticipantModeCommand(rankManager));
        getCommand("관리자아이템").setExecutor(new AdminItemCommand(this, rankManager));
        getCommand("등급설정").setExecutor(new RankSettingsCommand(this, rankManager));
        getCommand("체력상점").setExecutor(new HealthShopCommand(this, shopManager));
        getCommand("무기상점").setExecutor(new WeaponShopCommand(this, shopManager));
        getCommand("마나상점").setExecutor(new ManaShopCommand(this, shopManager));
        getCommand("등급교환").setExecutor(new ExchangeRankCommand());
        getCommand("등급전쟁").setExecutor(new StartGameCommand(gameManager, regionManager, configUtil, this, new FarmingListener(this, regionManager, configUtil)));
        getCommand("파밍설정").setExecutor(new FarmingSettingsCommand(this, configUtil, new FarmingGUI(configUtil)));

        // Listeners
        getServer().getPluginManager().registerEvents(new GuiClickListener(this, rankManager, shopManager, gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this, shopManager, rankManager, gameManager), this);
        getServer().getPluginManager().registerEvents(new GameListener(this, gameManager, rankManager, pointManager), this);
        getServer().getPluginManager().registerEvents(new WandListener(regionManager), this);
        getServer().getPluginManager().registerEvents(new FarmingListener(this, regionManager, configUtil),this);




        getLogger().info("&aRankWar Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("&cRankWar Plugin Disabled");
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public PointManager getPointManager() {
        return pointManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ConfigUtil getConfigUtil() {
        return configUtil;
    }
}

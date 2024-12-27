package com.yd.rankwar.managers;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ShopManager {
    private Map<String, List<ItemInfo>> shops = new HashMap<>();
    private RankWar plugin;
    private ConfigUtil configUtil;

    public ShopManager(RankWar plugin, ConfigUtil configUtil) {
        this.plugin = plugin;
        this.configUtil = configUtil;
        FileConfiguration config = configUtil.getConfig();

        // config에서 shops 데이터 로드
        if(config.contains("shops")) {
            Set<String> keys = config.getConfigurationSection("shops").getKeys(false);
            for(String shopType : keys) {
                List<ItemInfo> list = new ArrayList<>();
                if(config.isConfigurationSection("shops."+shopType)) {
                    Set<String> indices = config.getConfigurationSection("shops."+shopType).getKeys(false);
                    for(String idxStr : indices) {
                        String path = "shops."+shopType+"."+idxStr;
                        Material mat = Material.getMaterial(config.getString(path+".material","AIR"));
                        if(mat == null) mat = Material.AIR;
                        int price = config.getInt(path+".price",0);
                        int amount = config.getInt(path+".amount",1);
                        String name = config.getString(path+".name",null);
                        List<String> lore = config.getStringList(path+".lore");

                        ItemStack item = new ItemStack(mat, amount);
                        ItemMeta meta = item.getItemMeta();
                        if(meta != null) {
                            if(name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                            if(lore != null && !lore.isEmpty()) {
                                List<String> coloredLore = new ArrayList<>();
                                for(String l : lore) {
                                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', l));
                                }
                                meta.setLore(coloredLore);
                            }
                            item.setItemMeta(meta);
                        }

                        list.add(new ItemInfo(item, price, amount));
                    }
                }
                shops.put(shopType, list);
            }
        } else {
            // config에 shops 키가 없으면 초기화
            config.createSection("shops.health");
            config.createSection("shops.weapon");
            config.createSection("shops.mana");
            configUtil.saveConfig();
            shops.put("health", new ArrayList<>());
            shops.put("weapon", new ArrayList<>());
            shops.put("mana", new ArrayList<>());
        }
    }

    public void setItem(String shopType, ItemStack originalItem, int price, int amount) {
        if (!shops.containsKey(shopType)) shops.put(shopType, new ArrayList<>());

        ItemStack copied = originalItem.clone();
        ItemMeta meta = copied.getItemMeta();
        if(meta != null) {
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            if(lore == null) lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "가격: " + price + " 포인트");
            lore.add(ChatColor.YELLOW + "수량: " + amount + "개");
            meta.setLore(lore);
            copied.setItemMeta(meta);
        }

        List<ItemInfo> list = shops.get(shopType);
        list.add(new ItemInfo(copied, price, amount));
        saveShopToConfig(shopType, list);
    }


    public void removeItemByIndex(String shopType, int index) {
        if (shops.containsKey(shopType)) {
            List<ItemInfo> list = shops.get(shopType);
            if(index >= 0 && index < list.size()) {
                list.remove(index);
                saveShopToConfig(shopType, list);
            }
        }
    }

    public List<ItemInfo> getShopItems(String shopType) {
        return shops.getOrDefault(shopType, new ArrayList<>());
    }

    public static class ItemInfo {
        public ItemStack itemStack;
        public int price;
        public int amount;
        public ItemInfo(ItemStack itemStack, int price, int amount) {
            this.itemStack = itemStack;
            this.price = price;
            this.amount = amount;
        }
    }

    public void buyItem(Player p, String shopType, int index, int playerPoints, PointManager pointManager) {
        if(!shops.containsKey(shopType)) {
            p.sendMessage("상점이 존재하지 않습니다.");
            return;
        }
        List<ItemInfo> items = shops.get(shopType);
        if(index < 0 || index >= items.size()) {
            p.sendMessage("해당 슬롯에 아이템이 없습니다.");
            return;
        }
        ItemInfo info = items.get(index);
        if(playerPoints < info.price) {
            p.sendMessage("포인트가 부족합니다! 필요한 포인트: " + info.price);
            return;
        }
        // 포인트 차감
        pointManager.addPoints(p, -info.price);
        // 아이템 지급
        ItemStack giveItem = info.itemStack.clone();
        giveItem.setAmount(info.amount);
        p.getInventory().addItem(giveItem);
        p.sendMessage("아이템을 구매하였습니다. (" + info.price + " 포인트 소모)");
    }

    private void saveShopToConfig(String shopType, List<ItemInfo> list) {
        FileConfiguration config = configUtil.getConfig();
        String basePath = "shops." + shopType;

        // basePath가 섹션이 아닐 경우 섹션으로 재생성
        if (!config.isConfigurationSection(basePath)) {
            config.set(basePath, null); // 혹시 다른 타입으로 되어 있을 수 있으므로 초기화
            config.createSection(basePath);
        }

        // 기존 데이터 클리어
        ConfigurationSection section = config.getConfigurationSection(basePath);
        if (section != null) {
            for (String key : section.getKeys(false)) {
                config.set(basePath + "." + key, null);
            }
        }

        // 아이템 리스트 저장
        for (int i = 0; i < list.size(); i++) {
            ItemInfo info = list.get(i);
            String path = basePath + "." + i;
            Material mat = info.itemStack.getType();
            config.set(path + ".material", mat.name());
            config.set(path + ".price", info.price);
            config.set(path + ".amount", info.amount);

            ItemMeta im = info.itemStack.getItemMeta();
            if (im != null && im.hasDisplayName()) {
                config.set(path + ".name", im.getDisplayName().replace('§', '&'));
            } else {
                config.set(path + ".name", null);
            }

            List<String> lore = new ArrayList<>();
            if (im != null && im.hasLore()) {
                for (String l : im.getLore()) {
                    lore.add(l.replace('§', '&'));
                }
            }
            config.set(path + ".lore", lore);
        }

        configUtil.saveConfig();
    }
}
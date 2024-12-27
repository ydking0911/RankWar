package com.yd.rankwar.utils;

import com.google.gson.Gson;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NBTUtil {
    private static final Gson gson = new Gson();

    // ItemStack을 JSON 문자열로 변환
    public static String serializeItem(ItemStack item) {
        return gson.toJson(item.serialize());
    }

    // JSON 문자열을 ItemStack으로 변환
    public static ItemStack deserializeItem(String json) {
        Map<String, Object> map = gson.fromJson(json, Map.class);
        return ItemStack.deserialize(map);
    }

}
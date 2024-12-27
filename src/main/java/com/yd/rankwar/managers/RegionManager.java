package com.yd.rankwar.managers;

import com.yd.rankwar.RankWar;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.List;

public class RegionManager {
    private Location pos1; // wand로 좌클릭 한 위치
    private Location pos2; // wand로 우클릭 한 위치
    private final RankWar plugin;

    public RegionManager(RankWar plugin) {
        this.plugin = plugin;
    }

    public void setPos1(Location loc) {
        this.pos1 = loc;
    }

    public void setPos2(Location loc) {
        this.pos2 = loc;
    }

    public boolean isRegionSet() {
        return pos1 != null && pos2 != null;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public int getMinX() {
        return Math.min(pos1.getBlockX(), pos2.getBlockX());
    }

    public int getMaxX() {
        return Math.max(pos1.getBlockX(), pos2.getBlockX());
    }

    public int getMinY() {
        return Math.min(pos1.getBlockY(), pos2.getBlockY());
    }

    public int getMaxY() {
        return Math.max(pos1.getBlockY(), pos2.getBlockY());
    }

    public int getMinZ() {
        return Math.min(pos1.getBlockZ(), pos2.getBlockZ());
    }

    public int getMaxZ() {
        return Math.max(pos1.getBlockZ(), pos2.getBlockZ());
    }

    public boolean isInRegion(Location loc) {
        // pos1/pos2가 설정되지 않았거나 월드가 다르면 false
        if (!isRegionSet() || loc.getWorld() == null) return false;
        if (!loc.getWorld().equals(pos1.getWorld()) || !pos1.getWorld().equals(pos2.getWorld())) return false;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return x >= getMinX() && x <= getMaxX()
                && y >= getMinY() && y <= getMaxY()
                && z >= getMinZ() && z <= getMaxZ();
    }
}
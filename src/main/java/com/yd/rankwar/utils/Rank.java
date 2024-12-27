package com.yd.rankwar.utils;

import org.bukkit.ChatColor;

public enum Rank {
    RED(ChatColor.RED),
    ORANGE(ChatColor.GOLD),
    YELLOW(ChatColor.YELLOW),
    GREEN(ChatColor.GREEN),
    BLUE(ChatColor.BLUE),
    BLACK(ChatColor.BLACK),
    WHITE(ChatColor.WHITE);

    private final ChatColor color;

    Rank(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getOrder() {
        switch (this) {
            case RED: return 1;
            case ORANGE: return 2;
            case YELLOW: return 3;
            case GREEN: return 4;
            case BLUE: return 5;
            case BLACK: return 6;
            case WHITE: return 7;
            default: return 0;
        }
    }

    public static Rank getNextRank(Rank current) {
        int nextOrder = current.getOrder() + 1;
        if (nextOrder > 7) {
            return RED; // 최종 흰 -> 빨강
        }
        for (Rank r : values()) {
            if (r.getOrder() == nextOrder) return r;
        }
        return RED;
    }

    public static Rank getPrevRank(Rank current) {
        int prevOrder = current.getOrder() - 1;
        if (prevOrder < 1) {
            return BLACK; // 빨강 밑으로 하락 -> 검정
        }
        for (Rank r : values()) {
            if (r.getOrder() == prevOrder) return r;
        }
        return BLACK;
    }
}
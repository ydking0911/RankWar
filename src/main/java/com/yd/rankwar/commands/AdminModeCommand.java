package com.yd.rankwar.commands;

import com.yd.rankwar.managers.RankManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminModeCommand implements CommandExecutor {
    private RankManager rankManager;
    public AdminModeCommand(RankManager rankManager) {
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용가능");
            return true;
        }
        Player p = (Player) sender;
        boolean isAdmin = rankManager.isAdmin(p);
        rankManager.setAdminMode(p, !isAdmin);
        p.sendMessage("운영자 모드: " + (!isAdmin));
        return true;
    }
}
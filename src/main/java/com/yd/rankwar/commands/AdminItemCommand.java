package com.yd.rankwar.commands;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.gui.AdminItemGUI;
import com.yd.rankwar.managers.RankManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminItemCommand implements CommandExecutor {
    private RankWar plugin;
    private RankManager rankManager;

    public AdminItemCommand(RankWar plugin, RankManager rankManager) {
        this.plugin = plugin;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){sender.sendMessage("플레이어만 사용가능");return true;}
        Player p = (Player)sender;
        if(!rankManager.isAdmin(p)) {
            p.sendMessage("운영자만 사용할 수 있습니다.");
            return true;
        }
        AdminItemGUI.openAdminItemGUI(p);
        return true;
    }
}
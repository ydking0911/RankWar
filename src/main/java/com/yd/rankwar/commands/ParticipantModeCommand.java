package com.yd.rankwar.commands;

import com.yd.rankwar.managers.RankManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParticipantModeCommand implements CommandExecutor {
    private RankManager rankManager;
    public ParticipantModeCommand(RankManager rankManager) {
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용가능");
            return true;
        }
        Player p = (Player) sender;
        rankManager.setParticipant(p);
        p.sendMessage("참가자 모드로 전환되었습니다.");
        return true;
    }
}
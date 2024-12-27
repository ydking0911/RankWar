package com.yd.rankwar.commands;

import com.yd.rankwar.gui.ExchangeRankGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExchangeRankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용가능한 명령어입니다.");
            return true;
        }
        Player p = (Player)sender;
        ExchangeRankGUI.openExchangeRankGUI(p);
        return true;
    }
}
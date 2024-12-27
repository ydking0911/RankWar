package com.yd.rankwar.commands;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.gui.FarmingGUI;
import com.yd.rankwar.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class FarmingSettingsCommand implements CommandExecutor {

    private final RankWar plugin;
    private final ConfigUtil configUtil;
    private final FarmingGUI farmingGUI;

    public FarmingSettingsCommand(RankWar plugin, ConfigUtil configUtil, FarmingGUI farmingGUI) {
        this.plugin = plugin;
        this.configUtil = configUtil;
        this.farmingGUI = farmingGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // 콘솔이면 사용 불가
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "해당 명령어는 플레이어만 사용 가능합니다.");
            return true;
        }

        Player player = (Player) sender;

        // 파밍설정 GUI 열기
        if (args.length == 0) {
            farmingGUI.openFarmingGUI(player);
            return true;
        }

        // 잘못된 인수
        player.sendMessage(ChatColor.RED + "/파밍설정 명령어 사용법: /파밍설정");
        return true;
    }
}
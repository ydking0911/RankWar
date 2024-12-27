package com.yd.rankwar.commands;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.managers.GameManager;
import com.yd.rankwar.managers.RegionManager;
import com.yd.rankwar.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.yd.rankwar.listeners.FarmingListener;

public class StartGameCommand implements CommandExecutor {

    private final GameManager gameManager;
    private final ConfigUtil configUtil;
    private final RankWar plugin;
    private final RegionManager regionManager;
    private final FarmingListener farmingListener;

    public StartGameCommand(GameManager gameManager,
                            RegionManager regionManager,
                            ConfigUtil configUtil,
                            RankWar plugin,
                            FarmingListener farmingListener) {
        this.gameManager = gameManager;
        this.regionManager = regionManager;
        this.configUtil = configUtil;
        this.plugin = plugin;
        this.farmingListener = farmingListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("시작")) {
                // 1) 영역 설정 여부 확인
                if (!regionManager.isRegionSet()) {
                    sender.sendMessage(ChatColor.RED + "영역이 설정되지 않았습니다! Wand로 pos1, pos2 먼저 지정하세요.");
                    return true;
                }

                // 2) 게임 시작
                gameManager.startGame();
                sender.sendMessage(ChatColor.GREEN + "게임을 시작했습니다!");

                // 3) 영역 내 모든 상자에 아이템 즉시 채워넣기
                farmingListener.populateChestsInRegion();

                return true;
            }
            else if (args[0].equalsIgnoreCase("끝")) {
                gameManager.endGame();
                sender.sendMessage(ChatColor.RED + "게임을 종료했습니다.");
                return true;
            }
        }

        sendHelpMessage(sender);
        return true;
    }


    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.WHITE + "=======" + ChatColor.YELLOW + " 등급전쟁 (Made by Moody) " + ChatColor.WHITE + "=======");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.YELLOW + "/등급전쟁 [시작|끝]" + ChatColor.WHITE + " : 등급전쟁을 시작합니다.");
        sender.sendMessage(ChatColor.YELLOW + "/운영자" + ChatColor.WHITE + " : 운영자모드로 전환합니다. 해당 모드인 상태에서 등급설정이 가능합니다.");
        sender.sendMessage(ChatColor.YELLOW + "/참가자" + ChatColor.WHITE + " : 참가자 모드로 전환합니다. 해당 상태에서만 게임에 참가할 수 있습니다.");
        sender.sendMessage(ChatColor.YELLOW + "/관리자아이템" + ChatColor.WHITE + " : 관리자아이템을 확인합니다.");
        sender.sendMessage(ChatColor.YELLOW + "/파밍설정" + ChatColor.WHITE + " : 파밍 아이템 설정");
        sender.sendMessage(ChatColor.YELLOW + "/등급설정" + ChatColor.WHITE + " : 등급 색상의 on/off를 설정합니다.");
        sender.sendMessage(ChatColor.YELLOW + "/등급교환" + ChatColor.WHITE + " : 상점을 오픈합니다. (SHIFT + 손바꾸기 키)");
        sender.sendMessage(ChatColor.YELLOW + "/__상점" + ChatColor.WHITE + " : 해당 상점 GUI가 뜨며, 내부 아이템을 클릭 시 제거 가능합니다. (체력상점/무기상점/마나상점)");
        sender.sendMessage(ChatColor.YELLOW + "/__상점 설정 [가격] [개수]" + ChatColor.WHITE + " : 해당 상점에 등록할 아이템을 손에 들고 사용하면 해당 상점에 등록됩니다. (체력상점/무기상점/마나상점)");
        sender.sendMessage(" ");
        sender.sendMessage("====================================");
    }
}
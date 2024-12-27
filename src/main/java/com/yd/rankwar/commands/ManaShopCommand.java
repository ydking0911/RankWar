package com.yd.rankwar.commands;

import com.yd.rankwar.RankWar;
import com.yd.rankwar.gui.ShopConfigGUI;
import com.yd.rankwar.managers.ShopManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManaShopCommand implements CommandExecutor {
    private ShopManager shopManager;
    private RankWar plugin;

    public ManaShopCommand(RankWar plugin, ShopManager shopManager) {
        this.shopManager = shopManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {sender.sendMessage("플레이어만 사용가능");return true;}
        Player p = (Player)sender;

        if(args.length == 0) {
            ShopConfigGUI.openShopConfigGUI(p, "mana", shopManager);
            return true;
        } else if(args[0].equalsIgnoreCase("설정")) {
            if(args.length == 3) {
                try {
                    int price = Integer.parseInt(args[1]);
                    int amount = Integer.parseInt(args[2]);
                    ItemStack inHand = p.getInventory().getItemInMainHand();
                    if(inHand == null || inHand.getType() == Material.AIR) {
                        p.sendMessage("손에 아이템을 들어주세요.");
                        return true;
                    }
                    shopManager.setItem("mana", inHand, price, amount);
                    p.sendMessage("해당 아이템을 마나상점에 등록하였습니다.");
                } catch (NumberFormatException e) {
                    p.sendMessage("/마나상점 설정 [가격] [개수]");
                }
            } else {
                ShopConfigGUI.openShopConfigGUI(p, "mana", shopManager);
            }
            return true;
        }
        return false;
    }
}
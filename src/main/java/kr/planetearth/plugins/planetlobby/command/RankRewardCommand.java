package kr.planetearth.plugins.planetlobby.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankRewardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        System.out.println(1);
        if (sender instanceof Player player && player.hasPermission("20250112.rankreward") && !player.hasPermission("20250112.rankreward.gained")) {
            String rank = "";
            if (player.hasPermission("group.guide")){
                sender.sendMessage(ChatColor.RED + "수령 가능한 랭크 보상이 없습니다!");
                return false;
            }
            if (player.hasPermission("group.expert")) rank = "expert";
            else if (player.hasPermission("group.pro")) rank = "pro";
            else if (player.hasPermission("group.basic")) rank = "basic";
            else{
                rank = "nlite";
            }

            if (player.hasPermission("group.newbie")){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        " lp user " + player.getName() + " parent addtemp newbie 3d accumulate");
            }
            else if (player.hasPermission("group.return")){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        " lp user " + player.getName() + " parent addtemp return 3d accumulate");
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    " lp user " + player.getName() + " parent addtemp " + rank + " 14d accumulate");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "lp user " + player.getName() + " permission settemp 20250112.rankreward.gained true 14d");
            player.sendMessage(ChatColor.GOLD + "[플래닛어스] " +
                    ChatColor.YELLOW + "랭크 보상을 수령했습니다! 현재 랭크 유지 기간이 14일 늘어났습니다.");
            return true;
        }
        else{
            sender.sendMessage(ChatColor.RED + "수령 가능한 랭크 보상이 없습니다!");
            return false;
        }
    }
}

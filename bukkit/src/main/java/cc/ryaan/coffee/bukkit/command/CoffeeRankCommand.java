package cc.ryaan.coffee.bukkit.command;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.menu.rank.edit.RankEditMenu;
import cc.ryaan.coffee.bukkit.menu.rank.manage.RankManagementMenu;
import cc.ryaan.coffee.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.protocol.plib.command.Command;
import rip.protocol.plib.command.Param;

public class CoffeeRankCommand {

    @Command(names = "coffee rank", permission = "coffee.admin", hidden = true)
    public static void main(CommandSender sender, @Param(name = "rank", defaultValue = "   ") String rankString) {
        if(sender instanceof Player) {
            if(rankString.equals("   ")) new RankManagementMenu().openMenu(((Player) sender).getPlayer());
            else {
                Rank rank = CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRank(rankString);
                if(rank == null) {
                    sender.sendMessage(ChatColor.RED + "No such rank by the name of \"" + rankString + "\".");
                    return;
                }
                new RankEditMenu(rank).openMenu(((Player) sender).getPlayer());
            }
            return;
        }
        sender.sendMessage(ChatColor.GRAY + "TODO: useful cmds");
    }

}

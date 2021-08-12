package cc.ryaan.coffee.bukkit.command;

import cc.ryaan.coffee.bukkit.menu.rank.manage.RankManagementMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.protocol.plib.command.Command;

public class CoffeeRankCommand {

    @Command(names = "coffee rank", permission = "coffee.admin", hidden = true)
    public static void main(CommandSender sender) {
        if(sender instanceof Player) {
            new RankManagementMenu().openMenu(((Player) sender).getPlayer());
            return;
        }
        sender.sendMessage(ChatColor.GRAY + "TODO: useful cmds");
    }

}

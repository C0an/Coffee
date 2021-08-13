package cc.ryaan.coffee.bukkit.command;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.command.parameter.RankParameter;
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
                Rank rank = new RankParameter().transform(sender, rankString);
                if(rank == null) return;
                new RankEditMenu(rank).openMenu(((Player) sender).getPlayer());
            }
            return;
        }
        sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Coffee Rank");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank create" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Create a new rank");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank delete" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Delete an existing rank");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank rename" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Rename a rank");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank setdisplayname" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Set a rank display name");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank setprefix" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Set a rank prefix");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank setsuffix" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Set a rank suffix");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank setcolour" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Set a rank colour");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank setpriority" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Set a ranks priority");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank setstaff" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Set a ranks staff status");
        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + "coffee rank sethidden" + ChatColor.GRAY + ": " + ChatColor.WHITE + "Set a ranks hidden status");
    }

    @Command(names = {"coffee rank create"}, permission = "coffee.admin")
    public static void permsRankCreateCmd(CommandSender sender, @Param(name = "rank") String name) {
        Rank r = CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRank(name);
        if(r != null) {
            sender.sendMessage(ChatColor.RED + "There is already a rank with the name \"" + r.getColouredName() + "\".");
            return;
        }
        r = new Rank(name);
        sender.sendMessage(ChatColor.GREEN + "Successfully created the rank " + r.getColouredName() + ChatColor.GREEN + "!");
    }

    @Command(names = {"coffee rank delete"}, permission = "coffee.admin")
    public static void permsRankDeleteCmd(CommandSender sender, @Param(name = "rank") Rank rank) {
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().deleteRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully deleted the rank " + rank.getColouredName() + ChatColor.GREEN + "!");
    }

    @Command(names = {"coffee rank rename"}, permission = "coffee.admin")
    public static void permsRankRenameCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "name") String name) {
        String original = rank.getName();
        rank.setName(name);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully renamed the rank from " + rank.getColour() + original + ChatColor.GREEN + " to " + rank.getColour() + name + ChatColor.GREEN + "!");
    }

    @Command(names = {"coffee rank setdisplayname"}, permission = "coffee.admin")
    public static void permsRankSetDisplayNameCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "displayName", wildcard = true) String displayName) {
        rank.setDisplayName(displayName);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully changed the display name to " + rank.getColouredName() + "!");
    }

    @Command(names = {"coffee rank setprefix"}, permission = "coffee.admin")
    public static void permsRankSetPrefixCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "prefix", wildcard = true) String prefixString) {
        String prefix = ChatColor.translateAlternateColorCodes('&', prefixString);
        rank.setPrefix(prefix);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully changed the prefix to " + prefix + ChatColor.GREEN + "!");
    }

    @Command(names = {"coffee rank setsuffix"}, permission = "coffee.admin")
    public static void permsRankSetSuffixCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "suffix", wildcard = true) String suffixString) {
        String suffix = ChatColor.translateAlternateColorCodes('&', suffixString);
        rank.setSuffix(suffix);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully changed the suffix to " + suffixString + ChatColor.GREEN + "!");
    }

    @Command(names = {"coffee rank setcolour"}, permission = "coffee.admin")
    public static void permsRankSetColourCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "color") String colourString) {
        String colour = ChatColor.translateAlternateColorCodes('&', colourString);
        rank.setColour(colour);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully changed the color to " + rank.getColouredName() + ChatColor.GREEN + "!");
    }

    @Command(names = {"coffee rank setpriority"}, permission = "coffee.admin")
    public static void permsRankSetPriorityCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "priority") int priority) {
        rank.setPriority(priority);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully changed the priority to " + ChatColor.BLUE + priority + ChatColor.GREEN + "!");
    }

    @Command(names = {"coffee rank setstaff"}, permission = "coffee.admin")
    public static void permsRankSetStaffCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "staff") boolean staff) {
        rank.setStaff(staff);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully changed the staff status to " + ChatColor.BLUE + staff + ChatColor.GREEN + "!");
    }

    @Command(names = {"coffee rank sethidden"}, permission = "coffee.admin")
    public static void permsRankSetHiddenCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "hidden") boolean hidden) {
        rank.setHidden(hidden);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ChatColor.GREEN + "Successfully changed the hidden status to " + ChatColor.BLUE + hidden + ChatColor.GREEN + "!");
    }


}

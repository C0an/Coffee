package cc.ryaan.coffee.bukkit.command;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.command.parameter.RankParameter;
import cc.ryaan.coffee.bukkit.menu.rank.edit.RankEditMenu;
import cc.ryaan.coffee.bukkit.menu.rank.manage.RankManagementMenu;
import cc.ryaan.coffee.bukkit.util.ColourUtil;
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

        ColourUtil.sendLangListMessage(sender, "command.coffee.rank.help");
    }

    @Command(names = {"coffee rank create"}, permission = "coffee.admin")
    public static void permsRankCreateCmd(CommandSender sender, @Param(name = "rank") String name) {
        Rank rank = CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRank(name);
        if(rank != null) {
            sender.sendMessage(ColourUtil.getLangMessage("error.rank.already_exists").replace("%rank%", rank.getColouredName()));
            return;
        }
        rank = new Rank(name);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().setupRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.create.successfully_created").replace("%rank%", rank.getColouredName()));
    }

    @Command(names = {"coffee rank delete"}, permission = "coffee.admin")
    public static void permsRankDeleteCmd(CommandSender sender, @Param(name = "rank") Rank rank) {
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().deleteRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.delete.successfully_deleted").replace("%rank%", rank.getColouredName()));
    }

    @Command(names = {"coffee rank rename"}, permission = "coffee.admin")
    public static void permsRankRenameCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "name") String name) {
        String original = rank.getName();
        rank.setName(name);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.rename.successfully_rename").replace("%colour%", rank.getColour()).replace("%oldname%", original).replace("%newname%", name));
    }

    @Command(names = {"coffee rank setdisplayname"}, permission = "coffee.admin")
    public static void permsRankSetDisplayNameCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "displayName", wildcard = true) String displayName) {
        rank.setDisplayName(displayName);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.setdisplayname.successfully_changed").replace("%rank%", rank.getColouredName()));
    }

    @Command(names = {"coffee rank setprefix"}, permission = "coffee.admin")
    public static void permsRankSetPrefixCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "prefix", wildcard = true) String prefixString) {
        String prefix = ChatColor.translateAlternateColorCodes('&', prefixString);
        rank.setPrefix(prefix);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.setprefix.successfully_changed").replace("%prefix%", prefix));
    }

    @Command(names = {"coffee rank setsuffix"}, permission = "coffee.admin")
    public static void permsRankSetSuffixCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "suffix", wildcard = true) String suffixString) {
        String suffix = ChatColor.translateAlternateColorCodes('&', suffixString);
        rank.setSuffix(suffix);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.setsuffix.successfully_changed").replace("%suffix%", suffix));
    }

    @Command(names = {"coffee rank setcolour"}, permission = "coffee.admin")
    public static void permsRankSetColourCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "color") String colourString) {
        String colour = ChatColor.translateAlternateColorCodes('&', colourString);
        rank.setColour(colour);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.setcolour.successfully_changed").replace("%name%", rank.getColouredName()));
    }

    @Command(names = {"coffee rank setpriority"}, permission = "coffee.admin")
    public static void permsRankSetPriorityCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "priority") int priority) {
        rank.setPriority(priority);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.setpriority.successfully_changed").replace("%priority%", String.valueOf(priority)));
    }

    @Command(names = {"coffee rank setstaff"}, permission = "coffee.admin")
    public static void permsRankSetStaffCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "staff") boolean staff) {
        rank.setStaff(staff);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.setstaff.successfully_changed").replace("%status%", String.valueOf(staff)));
    }

    @Command(names = {"coffee rank sethidden"}, permission = "coffee.admin")
    public static void permsRankSetHiddenCmd(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "hidden") boolean hidden) {
        rank.setHidden(hidden);
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
        sender.sendMessage(ColourUtil.getLangMessage("command.coffee.rank.sethidden.successfully_changed").replace("%status%", String.valueOf(hidden)));
    }


}

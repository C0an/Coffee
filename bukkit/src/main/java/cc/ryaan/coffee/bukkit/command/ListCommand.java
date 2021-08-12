package cc.ryaan.coffee.bukkit.command;

import cc.ryaan.coffee.bukkit.CoffeeBukkit;
import cc.ryaan.coffee.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.protocol.plib.command.Command;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand {

    @Command(names = {"list", "online", "serverlist", "elist"}, permission = "")
    public static void list(CommandSender sender) {
        sender.sendMessage(getHeader(sender));
        List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
        sender.sendMessage("(" +Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") " + names);
    }

    private static String getHeader(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        List<Rank> ranks = CoffeeBukkit.getInstance().getCoffee().getRankHandler().getRanks().stream().sorted(Comparator.comparingInt(Rank::getOrderPriority).reversed()).collect(Collectors.toList());
        for (Rank rank : ranks) {
            boolean displayed = rank.getOrderPriority() >= 0;
            if (displayed) {
                if(rank.isHidden() && sender.hasPermission("coffee.rank.viewhidden")) {
                    builder.append(ChatColor.GRAY).append("*").append(rank.getColouredName()).append(ChatColor.RESET).append(", ");
                }else if(!rank.isHidden()){
                    builder.append(rank.getColouredName()).append(ChatColor.RESET).append(", ");
                }
            }
        }
        if (builder.length() > 2) {
            builder.setLength(builder.length() - 2);
        }
        return builder.toString();
    }

}

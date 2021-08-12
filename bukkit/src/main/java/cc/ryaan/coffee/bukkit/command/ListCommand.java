package cc.ryaan.coffee.bukkit.command;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.profile.ProfileBukkit;
import cc.ryaan.coffee.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.protocol.plib.command.Command;
import rip.protocol.plib.visibility.FrozenVisibilityHandler;

import java.util.*;
import java.util.stream.Collectors;

public class ListCommand {

    @Command(names = {"list", "online", "serverlist", "elist"}, permission = "", async = true)
    public static void list(CommandSender sender) {
        Map<Rank, List<String>> sorted = new TreeMap<>(Comparator.comparingInt(Rank::getPriority).reversed());
        int online = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {

            if(!sender.hasPermission("basic.staff") && !canSee(sender, player)) continue;
            ++online;

            ProfileBukkit profile = (ProfileBukkit) CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getProfileHandler().getProfileByUUID(player.getUniqueId());
            Rank rank = profile.getRankFromGrant(profile.getCurrentGrant());

            String displayName = rank.getColour() + player.getName() + ChatColor.WHITE;
            if (player.hasMetadata("invisible")) {
                displayName = ChatColor.GRAY + "*" + displayName;
            }
            sorted.putIfAbsent(rank, new LinkedList<>());
            sorted.get(rank).add(displayName);
        }
        List<String> merged = new LinkedList<>();
        for (List<String> part : sorted.values()) {
            part.sort(String.CASE_INSENSITIVE_ORDER);
            merged.addAll(part);
        }
        sender.sendMessage(getHeader(sender));
        sender.sendMessage("(" +online + "/" + Bukkit.getMaxPlayers() + ") " + merged);
    }

    private static String getHeader(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        List<Rank> ranks = CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRanks().stream().sorted(Comparator.comparingInt(Rank::getPriority).reversed()).collect(Collectors.toList());
        for (Rank rank : ranks) {
            boolean displayed = rank.getPriority() >= 0;
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

    private static boolean canSee(CommandSender sender, CommandSender target) {
        if(!(sender instanceof Player)) return true;
        return FrozenVisibilityHandler.treatAsOnline((Player)target, (Player)sender);
    }

}

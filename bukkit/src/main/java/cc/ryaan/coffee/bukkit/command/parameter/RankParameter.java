package cc.ryaan.coffee.bukkit.command.parameter;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.protocol.plib.command.ParameterType;
import rip.protocol.plib.pLib;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RankParameter implements ParameterType<Rank> {

    @Override
    public Rank transform(CommandSender commandSender, String s) {
        Rank rank = CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRank(s);
        if(rank == null) commandSender.sendMessage(ChatColor.RED + "No such rank found by the name of \"" + s + "\".");
        return rank;
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> set, String s) {
        List<String> completions = new ArrayList<>();

        for (Rank rank : CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRanks()) {
            if (StringUtils.startsWithIgnoreCase(rank.getName(), s)) {
                completions.add(rank.getName());
            }
        }
        return completions;
    }
}

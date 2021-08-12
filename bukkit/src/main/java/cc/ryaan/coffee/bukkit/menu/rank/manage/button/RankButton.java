package cc.ryaan.coffee.bukkit.menu.rank.manage.button;

import cc.ryaan.coffee.rank.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.protocol.plib.menu.Button;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @Getter
public class RankButton extends Button {

    private Rank rank;

    @Override
    public String getName(Player player) {
        return rank.getColouredName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------");
        lore.add(ChatColor.YELLOW + "UUID: " + ChatColor.WHITE + rank.getUuid().toString());
        lore.add(ChatColor.YELLOW + "Name: " + ChatColor.WHITE + rank.getName());
        lore.add("");
        lore.add(rank.getPrefix() + rank.getColour() + player.getName() + rank.getSuffix() + ChatColor.GRAY + ": " + ChatColor.WHITE + "Hello World!");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to edit the rank!");
        lore.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------");
        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.PAPER;
    }

}

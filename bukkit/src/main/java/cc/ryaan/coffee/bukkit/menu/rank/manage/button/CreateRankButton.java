package cc.ryaan.coffee.bukkit.menu.rank.manage.button;

import cc.ryaan.coffee.bukkit.menu.rank.create.RankCreateMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.protocol.plib.menu.Button;

import java.util.Arrays;
import java.util.List;

public class CreateRankButton extends Button {

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + "Create a Rank";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
                ChatColor.YELLOW + "Click to start creating",
                ChatColor.YELLOW + "a new rank!",
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------"
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.EMERALD;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        player.sendMessage("to like a process to say hey what do you want the name to be then open a menu to start setting data!!!!");
    }
}

package cc.ryaan.coffee.bukkit.menu.rank.edit.button;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.menu.rank.manage.RankManagementMenu;
import cc.ryaan.coffee.rank.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.protocol.plib.menu.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor @Getter
public class RankDeleteButton extends Button {

    private final Rank rank;

    @Override
    public String getName(Player player) {
        return ChatColor.RED + "Delete Rank";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------");
        lore.add(ChatColor.YELLOW + "Shift Click to");
        lore.add(ChatColor.YELLOW + "delete the rank");
        if(rank.isDefaultRank()) {
            lore.add("");
            lore.add(ChatColor.RED.toString() + ChatColor.BOLD + "NOTE! " + ChatColor.YELLOW + "The default");
            lore.add(ChatColor.YELLOW + "rank cannot be deleted.");
        }
        lore.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------");

        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.TNT;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if(!clickType.isLeftClick() || !clickType.isShiftClick()) {
            player.sendMessage(ChatColor.RED + "You need to Shift Click in order to delete the rank.");
            return;
        }
        if(rank.isDefaultRank()) {
            player.sendMessage(ChatColor.RED + "You cannot delete the default rank!");
            return;
        }
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().deleteRank(rank, true);
        player.sendMessage(ChatColor.GREEN + "Successfully deleted the " + rank.getColouredName() + ChatColor.GREEN + " rank!");
        player.closeInventory();
        new RankManagementMenu().openMenu(player);
    }
}

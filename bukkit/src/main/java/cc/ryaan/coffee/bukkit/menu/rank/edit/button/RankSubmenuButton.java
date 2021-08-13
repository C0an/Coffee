package cc.ryaan.coffee.bukkit.menu.rank.edit.button;

import cc.ryaan.coffee.rank.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.protocol.plib.menu.Button;
import rip.protocol.plib.menu.Menu;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor @Getter
public class RankSubmenuButton extends Button {

    private final Rank rank;
    private final Menu menu;
    private final Material material;
    private final String name;

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + name;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                ChatColor.YELLOW + "View and edit the ranks",
                ChatColor.YELLOW + name.toLowerCase(),
                "",
                ChatColor.BLUE + "Click to open",
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return material;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        menu.openMenu(player);
    }
}

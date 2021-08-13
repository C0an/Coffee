package cc.ryaan.coffee.bukkit.menu.rank.edit;

import cc.ryaan.coffee.bukkit.menu.rank.edit.button.RankDeleteButton;
import cc.ryaan.coffee.bukkit.menu.rank.edit.button.RankProcessButton;
import cc.ryaan.coffee.bukkit.menu.rank.edit.button.RankToggleButton;
import cc.ryaan.coffee.bukkit.prompt.rank.RankColourPrompt;
import cc.ryaan.coffee.bukkit.prompt.rank.RankDisplayNamePrompt;
import cc.ryaan.coffee.bukkit.prompt.rank.RankNamePrompt;
import cc.ryaan.coffee.bukkit.prompt.rank.RankPriorityPrompt;
import cc.ryaan.coffee.bukkit.util.RankUtil;
import cc.ryaan.coffee.rank.Rank;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.protocol.plib.menu.Button;
import rip.protocol.plib.menu.Menu;
import rip.protocol.plib.menu.buttons.BackButton;
import rip.protocol.plib.util.ItemBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class RankOtherSettingsMenu extends Menu {

    private final Rank rank;

    public RankOtherSettingsMenu(Rank rank) {
        super(rank.getColouredName() + ChatColor.WHITE + " - Other Settings");
        setAutoUpdate(true);
        setPlaceholder(true);
        this.rank = rank;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(0, new BackButton(new RankEditMenu(rank)));

        buttonMap.put(9, new RankProcessButton(
                ChatColor.GOLD + "Priority",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Priority: " + ChatColor.WHITE + rank.getPriority(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank priority",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.DIAMOND).build(), new RankPriorityPrompt(player, rank, this)));

        buttonMap.put(11, new RankToggleButton(rank, "staff", "Staff", "Staff", Material.DIAMOND_SWORD));
        buttonMap.put(13, new RankToggleButton(rank, "hidden", "Hidden", "Hidden", Material.GLASS));
        buttonMap.put(15, new RankToggleButton(rank, "default", "Default", "Default Rank", Material.COAL));
        buttonMap.put(17, new RankDeleteButton(rank));


        buttonMap.put(18, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, ""));

        return buttonMap;
    }
}

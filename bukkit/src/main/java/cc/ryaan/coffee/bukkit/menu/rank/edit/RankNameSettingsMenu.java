package cc.ryaan.coffee.bukkit.menu.rank.edit;

import cc.ryaan.coffee.bukkit.menu.rank.edit.button.RankProcessButton;
import cc.ryaan.coffee.bukkit.prompt.rank.RankColourPrompt;
import cc.ryaan.coffee.bukkit.prompt.rank.RankDisplayNamePrompt;
import cc.ryaan.coffee.bukkit.prompt.rank.RankNamePrompt;
import cc.ryaan.coffee.bukkit.util.RankUtil;
import cc.ryaan.coffee.rank.Rank;
import lombok.AllArgsConstructor;
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
public class RankNameSettingsMenu extends Menu {

    private final Rank rank;

    public RankNameSettingsMenu(Rank rank) {
        super(rank.getColouredName() + ChatColor.WHITE + " - Name Settings");
        setAutoUpdate(true);
        setPlaceholder(true);
        this.rank = rank;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(0, new BackButton(new RankEditMenu(rank)));
        buttonMap.put(11, new RankProcessButton(
                ChatColor.GOLD + "Name",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Name: " + ChatColor.WHITE + rank.getName(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank name",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.PAPER).build(), new RankNamePrompt(player, rank, this)));

        buttonMap.put(13, new RankProcessButton(
                ChatColor.GOLD + "Display Name",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Display Name: " + ChatColor.WHITE + rank.getDisplayName(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank display name",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.NAME_TAG).build(), new RankDisplayNamePrompt(player, rank, this)));

        buttonMap.put(15, new RankProcessButton(
                ChatColor.GOLD + "Colour",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Colour: " + ChatColor.WHITE + rank.getColouredName(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank colour",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.WOOL).data(RankUtil.getWoolColour(rank.getColour().charAt(1)).getWoolData()).build(), new RankColourPrompt(player, rank, this)));

        buttonMap.put(18, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, ""));

        return buttonMap;
    }
}

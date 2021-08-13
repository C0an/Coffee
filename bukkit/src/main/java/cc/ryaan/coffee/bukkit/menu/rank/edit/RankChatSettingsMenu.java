package cc.ryaan.coffee.bukkit.menu.rank.edit;

import cc.ryaan.coffee.bukkit.menu.rank.edit.button.RankProcessButton;
import cc.ryaan.coffee.bukkit.prompt.rank.*;
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
public class RankChatSettingsMenu extends Menu {

    private final Rank rank;

    public RankChatSettingsMenu(Rank rank) {
        super(rank.getColouredName()  + ChatColor.WHITE + " - Chat Settings");
        setAutoUpdate(true);
        setPlaceholder(true);
        this.rank = rank;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(0, new BackButton(new RankEditMenu(rank)));
        buttonMap.put(12, new RankProcessButton(
                ChatColor.GOLD + "Prefix",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.stripColor(rank.getPrefix()).isEmpty() ? ChatColor.RED + "No Prefix Set" : ChatColor.YELLOW + "Prefix: " + ChatColor.WHITE + rank.getPrefix(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank prefix",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.IRON_INGOT).build(), new RankPrefixPrompt(player, rank, this)));

        buttonMap.put(14, new RankProcessButton(
                ChatColor.GOLD + "Suffix",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.stripColor(rank.getSuffix()).isEmpty() ? ChatColor.RED + "No Suffix Set" : ChatColor.YELLOW + "Suffix: " + ChatColor.WHITE + rank.getSuffix(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank suffix",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.GOLD_INGOT).build(), new RankSuffixPrompt(player, rank, this)));

        buttonMap.put(18, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, ""));

        return buttonMap;
    }
}

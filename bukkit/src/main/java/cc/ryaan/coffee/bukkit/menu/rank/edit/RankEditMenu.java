package cc.ryaan.coffee.bukkit.menu.rank.edit;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.menu.rank.edit.button.RankDeleteButton;
import cc.ryaan.coffee.bukkit.menu.rank.edit.button.RankProcessButton;
import cc.ryaan.coffee.bukkit.prompt.rank.*;
import cc.ryaan.coffee.bukkit.util.RankUtil;
import cc.ryaan.coffee.rank.Rank;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.protocol.plib.menu.Button;
import rip.protocol.plib.menu.Menu;
import rip.protocol.plib.util.ItemBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class RankEditMenu extends Menu {

    private final Rank rank;

    public RankEditMenu(Rank rank) {
        super(rank.getColouredName());
        this.rank = rank;
        setAutoUpdate(true);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(buttonMap.size(), new RankProcessButton(
                ChatColor.YELLOW + "Rename Rank",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Name: " + ChatColor.WHITE + rank.getName(),
                        "",
                        ChatColor.YELLOW + "Click to rename",
                        ChatColor.YELLOW + "the rank",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.PAPER).build(), new RankNamePrompt(player, rank, this)));

        buttonMap.put(buttonMap.size(), new RankProcessButton(
                ChatColor.YELLOW + "Set Display Name",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Display Name: " + ChatColor.WHITE + rank.getDisplayName(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank display name",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.NAME_TAG).build(), new RankDisplayNamePrompt(player, rank, this)));

        buttonMap.put(buttonMap.size(), new RankProcessButton(
                ChatColor.YELLOW + "Set Colour",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Colour: " + ChatColor.WHITE + rank.getColouredName(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank colour",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.WOOL).data(RankUtil.getWoolColour(rank.getColour().charAt(1)).getWoolData()).build(), new RankColourPrompt(player, rank, this)));

        buttonMap.put(buttonMap.size(), new RankProcessButton(
                ChatColor.YELLOW + "Set Prefix",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Prefix: " + ChatColor.WHITE + rank.getPrefix(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank prefix",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.IRON_INGOT).build(), new RankPrefixPrompt(player, rank, this)));

        buttonMap.put(buttonMap.size(), new RankProcessButton(
                ChatColor.YELLOW + "Set Suffix",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Suffix: " + ChatColor.WHITE + rank.getSuffix(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank suffix",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.GOLD_INGOT).build(), new RankSuffixPrompt(player, rank, this)));

        buttonMap.put(buttonMap.size(), new RankProcessButton(
                ChatColor.YELLOW + "Set Priority",
                Arrays.asList(
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                        ChatColor.YELLOW + "Priority: " + ChatColor.WHITE + rank.getPriority(),
                        "",
                        ChatColor.YELLOW + "Click to update the",
                        ChatColor.YELLOW + "rank priority",
                        ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
                ), ItemBuilder.of(Material.SKULL_ITEM).data((short)3).build(), new RankPriorityPrompt(player, rank, this)));

        buttonMap.put(8, new RankDeleteButton(rank));

        return buttonMap;
    }
}

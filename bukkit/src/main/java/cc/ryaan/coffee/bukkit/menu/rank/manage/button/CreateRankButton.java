package cc.ryaan.coffee.bukkit.menu.rank.manage.button;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.menu.rank.edit.RankEditMenu;
import cc.ryaan.coffee.bukkit.prompt.rank.*;
import cc.ryaan.coffee.rank.Rank;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;
import rip.protocol.plib.menu.Button;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class CreateRankButton extends Button {

    private Player executor;

    private final ConversationFactory factory = new ConversationFactory(CoffeeBukkitPlugin.getInstance());

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
        startConversation(player);
    }

    private void startConversation( Player player ) {
        factory.withFirstPrompt(
                new RankNamePrompt(player, null,
                new RankDisplayNamePrompt(player, null,
                new RankPriorityPrompt(player, null,
                new RankColourPrompt(player, null,
                new RankPrefixPrompt(player, null, new Done())))))).withPrefix(new NullConversationPrefix())
                .withLocalEcho(false).withEscapeSequence("cancel").buildConversation( player )
                .begin();
    }

    public class Done implements Prompt {

        @Override
        public String getPromptText( ConversationContext context ) {
            String name = (String) context.getSessionData( "name" );
            String displayName = (String) context.getSessionData( "displayName" );
            String priority = (String) context.getSessionData( "priority" );
            String colour = (String) context.getSessionData( "colour" );
            String prefix = (String) context.getSessionData( "prefix" );

            Rank rank = new Rank(name);
            rank.setDisplayName(displayName);
            rank.setPriority(Integer.parseInt(priority));
            rank.setColour(colour);
            rank.setPrefix(prefix);

            CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
            CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRanks().add(rank);

            new BukkitRunnable() {
                public void run() {
                    new RankEditMenu(rank).openMenu(executor);
                }
            }.runTask(CoffeeBukkitPlugin.getInstance());

            return ChatColor.GREEN + "Successfully created the rank " + rank.getDisplayName();
        }


        @Override
        public boolean blocksForInput( ConversationContext context ) {
            return false;
        }


        @Override
        public Prompt acceptInput( ConversationContext context, String input ) {
            return END_OF_CONVERSATION;
        }

    }

}

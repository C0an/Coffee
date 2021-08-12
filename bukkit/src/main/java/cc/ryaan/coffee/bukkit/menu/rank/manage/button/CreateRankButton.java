package cc.ryaan.coffee.bukkit.menu.rank.manage.button;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.menu.rank.edit.RankEditMenu;
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
        factory.withFirstPrompt(new NamePrompt()).withPrefix(new NullConversationPrefix())
                .withLocalEcho(false).withEscapeSequence("cancel").buildConversation( player )
                .begin();
    }


    public class NamePrompt extends ValidatingPrompt {

        @Override
        public String getPromptText( ConversationContext context ) {
            return ChatColor.YELLOW + "Please type a name for the rank, or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        @Override
        protected boolean isInputValid( ConversationContext context, String input ) {
            return !input.contains(" ");
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String input ) {
            context.setSessionData( "name", input );
            return new DisplayNamePrompt();
        }
    }

    public class DisplayNamePrompt extends ValidatingPrompt {

        @Override
        public String getPromptText( ConversationContext context ) {
            return ChatColor.YELLOW + "Please type a display name for the rank, or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        @Override
        protected boolean isInputValid( ConversationContext context, String input ) {
            return true;
        }

        @Override
        protected Prompt acceptValidatedInput( ConversationContext context, String input ) {
            context.setSessionData( "displayName", input );
            return new PriorityPrompt();
        }
    }

    public class PriorityPrompt extends ValidatingPrompt {

        @Override
        public String getPromptText( ConversationContext context ) {
            return ChatColor.YELLOW + "Please type a priority for the rank, or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        @Override
        protected boolean isInputValid( ConversationContext context, String input ) {
            try {
                Integer.parseInt(input);
                return true;
            }catch (Exception e) {
                return false;
            }
        }

        @Override
        protected Prompt acceptValidatedInput( ConversationContext context, String input ) {
            context.setSessionData( "priority", input );
            return new ColourPrompt();
        }
    }

    public class ColourPrompt extends ValidatingPrompt {

        @Override
        public String getPromptText( ConversationContext context ) {
            return ChatColor.YELLOW + "Please type a colour for the rank, (Use & to Colour) or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        @Override
        protected boolean isInputValid( ConversationContext context, String input ) {
            return true;
        }

        @Override
        protected Prompt acceptValidatedInput( ConversationContext context, String input ) {
            context.setSessionData( "colour", ChatColor.translateAlternateColorCodes('&', input) );
            return new PrefixPrompt();
        }
    }

    public class PrefixPrompt extends ValidatingPrompt {

        @Override
        public String getPromptText( ConversationContext context ) {
            return ChatColor.YELLOW + "Please type a prefix for the rank, (Use & to Colour) or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
        }

        @Override
        protected boolean isInputValid( ConversationContext context, String input ) {
            return true;
        }

        @Override
        protected Prompt acceptValidatedInput( ConversationContext context, String input ) {
            context.setSessionData( "prefix", ChatColor.translateAlternateColorCodes('&', input) );
            return new Done();
        }
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

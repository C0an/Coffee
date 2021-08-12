package cc.ryaan.coffee.bukkit.prompt.rank;

import cc.ryaan.coffee.rank.Rank;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import rip.protocol.plib.menu.Menu;

@Getter
public class RankColourPrompt extends ValidatingPrompt {

    private final Player executor;
    private final Rank rank;
    private Menu openMenu;
    private Prompt nextPrompt;

    public RankColourPrompt(Player executor, Rank rank, Menu openMenu) {
        this.executor = executor;
        this.rank = rank;
        this.openMenu = openMenu;
    }

    public RankColourPrompt(Player executor, Rank rank, Prompt nextPrompt) {
        this.executor = executor;
        this.rank = rank;
        this.nextPrompt = nextPrompt;
    }

    @Override
    public String getPromptText( ConversationContext context ) {
        return ChatColor.YELLOW + "Please type a colour for the rank, (Use & to Colour) or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
    }

    @Override
    protected boolean isInputValid( ConversationContext context, String input ) {
        return true;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input ) {
        if(nextPrompt == null) {
            rank.setColour(ChatColor.translateAlternateColorCodes('&', input));
            if(openMenu != null) openMenu.openMenu(executor);
            return END_OF_CONVERSATION;
        }else {
            context.setSessionData( "colour", ChatColor.translateAlternateColorCodes('&', input) );
            return nextPrompt;
        }
    }
}

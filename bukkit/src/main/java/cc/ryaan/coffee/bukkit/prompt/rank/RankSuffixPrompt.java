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
public class RankSuffixPrompt extends ValidatingPrompt {

    private final Player executor;
    private final Rank rank;
    private Menu openMenu;
    private Prompt nextPrompt;

    public RankSuffixPrompt(Player executor, Rank rank, Menu openMenu) {
        this.executor = executor;
        this.rank = rank;
        this.openMenu = openMenu;
    }

    public RankSuffixPrompt(Player executor, Rank rank, Prompt nextPrompt) {
        this.executor = executor;
        this.rank = rank;
        this.nextPrompt = nextPrompt;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.YELLOW + "Please type a suffix for the rank, (Use & to Colour) or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
    }

    @Override
    protected boolean isInputValid( ConversationContext context, String input ) {
        return !input.contains(" ");
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input ) {
        if(nextPrompt == null) {
            rank.setSuffix(ChatColor.translateAlternateColorCodes('&', input));
            if(openMenu != null) openMenu.openMenu(executor);
            return END_OF_CONVERSATION;
        }else {
            context.setSessionData( "suffix", ChatColor.translateAlternateColorCodes('&', input) );
            return nextPrompt;
        }
    }
}

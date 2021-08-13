package cc.ryaan.coffee.bukkit.prompt.rank;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.rank.Rank;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import rip.protocol.plib.menu.Menu;

@Getter
public class RankPriorityPrompt extends ValidatingPrompt {

    private final Player executor;
    private final Rank rank;
    private Menu openMenu;
    private Prompt nextPrompt;

    public RankPriorityPrompt(Player executor, Rank rank, Menu openMenu) {
        this.executor = executor;
        this.rank = rank;
        this.openMenu = openMenu;
    }

    public RankPriorityPrompt(Player executor, Rank rank, Prompt nextPrompt) {
        this.executor = executor;
        this.rank = rank;
        this.nextPrompt = nextPrompt;
    }

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
    protected Prompt acceptValidatedInput(ConversationContext context, String input ) {
        if(nextPrompt == null) {
            rank.setPriority(Integer.parseInt(input));
            CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
            if(openMenu != null) openMenu.openMenu(executor);
            return END_OF_CONVERSATION;
        }else {
            context.setSessionData( "priority", input );
            return nextPrompt;
        }
    }
}

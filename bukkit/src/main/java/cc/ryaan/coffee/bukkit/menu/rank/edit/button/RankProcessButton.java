package cc.ryaan.coffee.bukkit.menu.rank.edit.button;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NullConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.protocol.plib.menu.Button;
import rip.protocol.plib.util.ItemBuilder;

import java.util.List;

@AllArgsConstructor @Getter
public class RankProcessButton extends Button {

    private final String name;
    private final List<String> description;
    private final ItemStack itemStack;
    private final ConversationFactory factory = new ConversationFactory(CoffeeBukkitPlugin.getInstance());
    private final Prompt prompt;

    @Override
    public String getName(Player player) {
        return name;
    }

    @Override
    public List<String> getDescription(Player player) {
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.AIR;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return ItemBuilder.copyOf(itemStack).name(getName(player)).setLore(getDescription(player)).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        factory.withFirstPrompt(prompt).withPrefix(new NullConversationPrefix())
                .withLocalEcho(false).withEscapeSequence("cancel").buildConversation( player )
                .begin();
    }
}

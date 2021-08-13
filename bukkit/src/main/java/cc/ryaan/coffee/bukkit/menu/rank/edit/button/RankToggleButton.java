package cc.ryaan.coffee.bukkit.menu.rank.edit.button;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.rank.Rank;
import cc.ryaan.coffee.util.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.protocol.plib.menu.Button;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor @Getter
public class RankToggleButton extends Button {

    private final Rank rank;
    private final String fieldName;
    private final String name;
    private final String toggleName;
    private final Material material;

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + name;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------",
                ChatColor.YELLOW + toggleName + ": " + ChatColor.WHITE + (getFieldValue() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"),
                "",
                ChatColor.YELLOW + "Click to update the",
                ChatColor.YELLOW + "rank " + name.toLowerCase(),
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------"
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return material;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        toggleFieldValue(player);
    }

    public Field getField() {
        Class<? extends Rank> clazz = rank.getClass();
        return ReflectUtil.getField(clazz, boolean.class, fieldName);
    }

    public boolean getFieldValue() {
        try {
            Field field = getField();
            if(field == null) return false;

            return (boolean) field.get(rank);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void toggleFieldValue(Player player) {
        boolean toggledValue = !getFieldValue();
        try {
            getField().setBoolean(rank, toggledValue);
            CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, true);
            player.sendMessage(ChatColor.YELLOW + "Updated the " + name + " to: " + (toggledValue ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

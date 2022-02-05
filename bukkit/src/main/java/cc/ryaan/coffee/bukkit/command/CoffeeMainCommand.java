package cc.ryaan.coffee.bukkit.command;

import cc.ryaan.coffee.bukkit.util.ColourUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.protocol.plib.command.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoffeeMainCommand {

    @Getter private static final List<UUID> debugUsers = new ArrayList<>();

    @Command(names = "coffee debug", permission = "coffee.admin", hidden = true)
    public static void debug(Player player) {
        boolean toggledDebug = !debugUsers.contains(player.getUniqueId());
        if(toggledDebug) debugUsers.add(player.getUniqueId());
        else debugUsers.remove(player.getUniqueId());

        if(toggledDebug) player.sendMessage(ColourUtil.getLangMessage("command.coffee.debug.enabled"));
        else player.sendMessage(ColourUtil.getLangMessage("command.coffee.debug.disabled"));
    }

}

package cc.ryaan.coffee.bukkit.populator;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.command.CoffeeMainCommand;
import cc.ryaan.coffee.log.LoggerPopulator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Objects;

public class CoffeeBukkitLoggerPopulator extends LoggerPopulator {

    public CoffeeBukkitLoggerPopulator() {
        super("Coffee - Bukkit Logger", 100);
    }

    @Override
    public void printLog(String logMessage) {
        String message = ChatColor.translateAlternateColorCodes('&', logMessage);
        CoffeeBukkitPlugin.getInstance().getLogger().info(message);
        CoffeeMainCommand.getDebugUsers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(player -> player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "[COFFEE] " + ChatColor.WHITE + message));
    }

}

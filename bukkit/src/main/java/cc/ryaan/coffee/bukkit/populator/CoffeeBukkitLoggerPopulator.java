package cc.ryaan.coffee.bukkit.populator;

import cc.ryaan.coffee.bukkit.CoffeeBukkit;
import cc.ryaan.coffee.log.LoggerPopulator;
import org.bukkit.ChatColor;

public class CoffeeBukkitLoggerPopulator extends LoggerPopulator {

    public CoffeeBukkitLoggerPopulator() {
        super("Coffee - Bukkit Logger", 100);
    }

    @Override
    public void printLog(String message) {
        CoffeeBukkit.getInstance().getLogger().info(ChatColor.translateAlternateColorCodes('&', message));
        //TODO: Log to players who have debugging enabled.
    }

}

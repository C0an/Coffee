package cc.ryaan.coffee.bukkit;

import cc.ryaan.coffee.Coffee;
import cc.ryaan.coffee.bukkit.listener.CoffeeListener;
import cc.ryaan.coffee.bukkit.populator.CoffeeBukkitLoggerPopulator;
import cc.ryaan.coffee.bukkit.populator.CoffeeBukkitPopulator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CoffeeBukkit extends JavaPlugin {

    @Getter private static CoffeeBukkit instance;
    @Getter private Coffee coffee;

    @Override
    public void onEnable() {
        (instance = this).saveDefaultConfig();
        coffee = new Coffee(new CoffeeBukkitPopulator(), new CoffeeBukkitLoggerPopulator());
        Arrays.asList(new CoffeeListener()).forEach(event -> Bukkit.getPluginManager().registerEvents(event, this));
    }

    @Override
    public void onDisable() {
        instance = null;
    }

}

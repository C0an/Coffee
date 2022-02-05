package cc.ryaan.coffee.bukkit;

import cc.ryaan.coffee.Coffee;
import cc.ryaan.coffee.bukkit.command.parameter.RankParameter;
import cc.ryaan.coffee.bukkit.core.CoffeeBukkit;
import cc.ryaan.coffee.bukkit.listener.CoffeeListener;
import cc.ryaan.coffee.bukkit.populator.CoffeeBukkitLoggerPopulator;
import cc.ryaan.coffee.bukkit.populator.CoffeeBukkitPopulator;
import cc.ryaan.coffee.bukkit.util.BukkitConfigCreator;
import cc.ryaan.coffee.rank.Rank;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rip.protocol.plib.command.FrozenCommandHandler;

import java.util.Arrays;

public class CoffeeBukkitPlugin extends JavaPlugin {

    @Getter private static CoffeeBukkitPlugin instance;
    @Getter private CoffeeBukkit coffeeBukkit;
    @Getter private BukkitConfigCreator lang;

    @Override
    public void onEnable() {
        (instance = this).saveDefaultConfig();
        coffeeBukkit = new CoffeeBukkit(new CoffeeBukkitPopulator(), new CoffeeBukkitLoggerPopulator());

        this.lang = new BukkitConfigCreator("lang");

        Arrays.asList(new CoffeeListener()).forEach(event -> Bukkit.getPluginManager().registerEvents(event, this));
        FrozenCommandHandler.registerParameterType(Rank.class, new RankParameter());
        FrozenCommandHandler.registerAll(this);
    }

    @Override
    public void onDisable() {
        coffeeBukkit.shutdown();
        instance = null;
    }

}

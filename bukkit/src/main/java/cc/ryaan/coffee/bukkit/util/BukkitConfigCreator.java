package cc.ryaan.coffee.bukkit.util;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public class BukkitConfigCreator {

    private final String fileName;
    private File langYMLFile;
    private FileConfiguration config;

    public BukkitConfigCreator(String fileName) {
        this.fileName = fileName;
        update();
    }

    private void update() {
        langYMLFile = new File(CoffeeBukkitPlugin.getInstance().getDataFolder(), this.fileName + ".yml");
        if (!langYMLFile.exists()) {
            langYMLFile.getParentFile().mkdir();
            CoffeeBukkitPlugin.getInstance().saveResource(this.fileName + ".yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(langYMLFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

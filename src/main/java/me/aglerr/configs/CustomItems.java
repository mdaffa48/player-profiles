package me.aglerr.configs;

import me.aglerr.Profiles;
import me.aglerr.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomItems {

    public FileConfiguration data;
    public File cfg;

    public void setup() {
        Utils utils = Profiles.getInstance().getUtils();
        if(!Profiles.getInstance().getDataFolder().exists()) {
            Profiles.getInstance().getDataFolder().mkdir();
        }

        cfg = new File(Profiles.getInstance().getDataFolder(), "custom-items.yml");

        if(!cfg.exists()) {
            Profiles.getInstance().saveResource("custom-items.yml", false);
            utils.sendConsoleLog("custom-items.yml not found, creating custom-items.yml...");
        }

        data = YamlConfiguration.loadConfiguration(cfg);

    }

    public FileConfiguration getConfiguration() {
        return data;
    }

    public void saveData() {
        Utils utils = Profiles.getInstance().getUtils();
        try {
            data.save(cfg);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(cfg);
    }

}

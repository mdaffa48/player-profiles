package me.aglerr.utils.customgui;

import me.aglerr.Profiles;
import me.aglerr.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomLoader {

    public FileConfiguration fileconfig;
    private List<CustomItems> customItems = new ArrayList<>();

    public void load(){
        File[] files = new File(Profiles.getInstance().getDataFolder() + File.separator + "guis").listFiles();
        if(files.length > 0){
            for(File file : files){
                fileconfig = YamlConfiguration.loadConfiguration(file);

                for(String key : fileconfig.getConfigurationSection("items").getKeys(false)){

                    String title = fileconfig.getString("title");
                    int size = fileconfig.getInt("size");

                    boolean fillEnabled = fileconfig.getBoolean("fill-inventory.enabled");
                    String fillMaterial = fileconfig.getString("fill-inventory.item.material");
                    String fillName = fileconfig.getString("fill-inventory.item.name");
                    int fillAmount = fileconfig.getInt("fill-inventory.item.amount");

                    String material = fileconfig.getString("items." + key + ".material");
                    int amount = fileconfig.getInt("items." + key + ".amount");
                    String name = fileconfig.getString("items." + key + ".name");
                    int slot = fileconfig.getInt("items." + key + ".slot");
                    boolean glowing = fileconfig.getBoolean("items." + key + ".glowing");
                    boolean hide_attributes = fileconfig.getBoolean("items." + key + ".hide_attributes");
                    boolean usePermission = fileconfig.getBoolean("items." + key + ".usePermission");
                    String permission = fileconfig.getString("items." + key + ".permission");
                    List<String> lore = fileconfig.getStringList("items." + key + ".lore");
                    List<String> leftCommands = fileconfig.getStringList("items." + key + ".left-click-commands");
                    List<String> rightCommands = fileconfig.getStringList("items." + key + ".right-click-commands");
                    int customModelData = fileconfig.getInt("items." + key + ".CustomModelData");

                    customItems.add(new CustomItems(title, size, fillEnabled, fillMaterial, fillName, fillAmount, material, amount,
                            name, slot, glowing, hide_attributes, usePermission, permission, lore, leftCommands,
                            rightCommands, customModelData));

                }

            }
        } else {
            Utils utils = Profiles.getInstance().getUtils();
            utils.sendConsoleLog("Could not found any custom GUI!");
            utils.sendConsoleLog("Creating default custom GUI to prevent errors...");

            File file = new File(Profiles.getInstance().getDataFolder() + File.separator + "guis", "example.yml");
            utils.exampleFile(file);



        }
    }

    public File getFile(String guiName){
        File file = new File(Profiles.getInstance().getDataFolder() + File.separator + "guis", guiName + ".yml");
        return file;
    }

    public List<CustomItems> getCustomItems(){
        return customItems;
    }

}

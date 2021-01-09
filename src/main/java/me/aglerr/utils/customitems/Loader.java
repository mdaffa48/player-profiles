package me.aglerr.utils.customitems;

import me.aglerr.Profiles;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Items> loader = new ArrayList<>();

    public void load(){
        FileConfiguration items = Profiles.getInstance().getCustomItems().getConfiguration();
        for(String key : items.getConfigurationSection("items").getKeys(false)){

            String material = items.getString("items." + key + ".material");
            int amount = items.getInt("items." + key + ".amount");
            String name = items.getString("items." + key + ".name");
            int slot = items.getInt("items." + key + ".slot");
            boolean glowing = items.getBoolean("items." + key + ".glowing");
            boolean hide_attributes = items.getBoolean("items." + key + ".hide_attributes");
            boolean usePermission = items.getBoolean("items." + key + ".usePermission");
            String permission = items.getString("items." + key + ".permission");
            List<String> lore = items.getStringList("items." + key + ".lore");
            List<String> leftCommands = items.getStringList("items." + key + ".left-click-commands");
            List<String> rightCommands = items.getStringList("items." + key + ".right-click-commands");
            int customModelData = items.getInt("items." + key + ".CustomModelData");

            loader.add(new Items(material, amount, name, slot, glowing, hide_attributes, usePermission, permission, lore,
                    leftCommands, rightCommands, customModelData));
        }
    }

    public List<Items> getItems(){
        return loader;
    }

}

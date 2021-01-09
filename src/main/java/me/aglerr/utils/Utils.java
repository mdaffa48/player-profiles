package me.aglerr.utils;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.SirBlobman.combatlogx.api.utility.ICombatManager;
import com.cryptomorin.xseries.XMaterial;
import me.aglerr.Profiles;
import me.aglerr.utils.customgui.CustomLoader;
import me.aglerr.utils.customitems.Items;
import me.aglerr.utils.fastinv.FastInv;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {

    Pattern pattern = Pattern.compile("(?<=\\[CONSOLE\\] |\\[PLAYER\\] |\\[SOUND\\] |\\[MESSAGE\\] |\\[OPENGUIMENU\\] |\\[CLOSE\\])");

    public String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public List<String> color(List<String> strings) {
        return strings.stream().map(this::color).collect(Collectors.toList());
    }

    public String getPrefix() {
        FileConfiguration config = Profiles.getInstance().getConfig();
        return config.getString("messages.prefix");
    }

    public boolean hasOffhand() {
        if (Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17"))
            return true;
        if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.7")) return false;
        return false;

    }

    public void sendConsoleLog(String message) {
        Bukkit.getConsoleSender().sendMessage(color("[PlayerProfiles] " + message));
    }

    public void consoleCommand(String command){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    /*
    Return true if the version is WG7
    Return false if the version is WG6
     */
    /*public boolean checkWorldGuardVersion() {
        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16"))
            return true;
        if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.12"))
            return false;
        return false;

    }

    public void checkRegion(Player player) {
        FileConfiguration config = Profiles.getInstance().getConfig();

        if(config.getBoolean("disabledRegions.enabled") && Profiles.worldGuardHook) {
            if(checkWorldGuardVersion()) {

                com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
                com.sk89q.worldguard.protection.regions.RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                com.sk89q.worldguard.protection.regions.RegionQuery query = container.createQuery();
                com.sk89q.worldguard.protection.ApplicableRegionSet set = query.getApplicableRegions(loc);
                if(!set.getRegions().isEmpty()) {
                    for(com.sk89q.worldguard.protection.regions.ProtectedRegion rg : set) {
                        if(config.getStringList("disabledRegions.regions").contains(rg.getId())) {
                            player.sendMessage(color(config.getString("messages.disabled-regions"))
                            .replace("%player%", getPrefix()));
                            return;
                        }
                    }
                }

            } else {

                com.sk89q.worldguard.protection.managers.RegionManager rm = com.sk89q.worldguard.bukkit.WGBukkit.getRegionManager(player.getWorld());
                com.sk89q.worldguard.protection.ApplicableRegionSet ars = rm.getApplicableRegions(player.getLocation());
                if(!ars.getRegions().isEmpty()) {
                    for(com.sk89q.worldguard.protection.regions.ProtectedRegion rg : ars) {
                        if(config.getStringList("disabledRegions.regions").contains(rg.getId())) {
                            player.sendMessage(color(config.getString("messages.disabled-regions"))
                            .replace("%prefix%", getPrefix()));
                            return;
                        }
                    }
                }
            }

        }
    }*/


    public boolean checkCombat(Player player) {
        if (Profiles.combatLogXHook) {
            ICombatLogX combatLog = (ICombatLogX) Bukkit.getPluginManager().getPlugin("CombatLogX");
            ICombatManager combatManager = combatLog.getCombatManager();
            return combatManager.isInCombat(player);
        }
        return false;
    }


    public boolean checkModelData() {
        if (Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")) {
            return true;
        } else {
            return false;
        }
    }

    public void addClickListener(Player player, Player target, FastInv inventory){
        inventory.addClickHandler(event -> {
            FileConfiguration config = Profiles.getInstance().getConfig();
            ItemStack item = event.getCurrentItem();
            Armory armory = Profiles.getInstance().getArmory();
            CustomLoader customLoader = Profiles.getInstance().getCustomLoader();

            if(config.getBoolean("gui.fill-inventory.enabled")){
                ItemStack material = XMaterial.matchXMaterial(config.getString("gui.fill-inventory.item.material")).get().parseItem();
                int amount = config.getInt("gui.fill-inventory.item.amount");
                String name = config.getString("gui.fill-inventory.item.name");
                ItemStack fill = ItemBuilder.start(material).amount(amount).name(name).flag(ItemFlag.HIDE_ATTRIBUTES).build();
                if(item.isSimilar(fill)) return;
            }

            if(item == null || item.getType() == XMaterial.AIR.parseMaterial()) return;
            event.setCancelled(true);
            if(config.getBoolean("options.update-gui-on-click")){
                armory.setCustomItems(player, target, inventory);
            }

            for(Items items : Profiles.getInstance().getLoader().getItems()){
                if(event.getSlot() == items.getSlot()){

                    if(event.getClick().equals(ClickType.LEFT)){
                        for(String commands : items.getLeftCommands()){
                            if(!items.getLeftCommands().isEmpty()){

                                String[] cmds = pattern.split(commands);
                                String tag = cmds[0];
                                String task = PlaceholderAPI.setPlaceholders(target, cmds.length > 1 ? cmds[1]: "");

                                if(tag.equalsIgnoreCase("[CONSOLE] ")){
                                    this.consoleCommand(task);
                                } else if(tag.equalsIgnoreCase("[PLAYER] ")){
                                    player.chat("/" + task);
                                } else if(tag.equalsIgnoreCase("[MESSAGE] ")){
                                    player.sendMessage(PlaceholderAPI.setPlaceholders(target, task));
                                } else if(tag.equalsIgnoreCase("[OPENGUIMENU] ")){

                                    String guiName = task;
                                    File file = customLoader.getFile(guiName);

                                    armory.setCustomGUIItems(file, player, target);

                                } else if(tag.equalsIgnoreCase("[CLOSE]")){
                                    player.closeInventory();
                                }

                            }
                        }
                    } else if(event.getClick().equals(ClickType.RIGHT)){
                        for(String commands : items.getRightCommands()){
                            if(!items.getRightCommands().isEmpty()){

                                String[] cmds = pattern.split(commands);
                                String tag = cmds[0];
                                String task = PlaceholderAPI.setPlaceholders(target, cmds.length > 1 ? cmds[1]: "");

                                if(tag.equalsIgnoreCase("[CONSOLE] ")){
                                    this.consoleCommand(task);
                                } else if(tag.equalsIgnoreCase("[PLAYER] ")){
                                    player.chat("/" + task);
                                } else if(tag.equalsIgnoreCase("[MESSAGE] ")){
                                    player.sendMessage(PlaceholderAPI.setPlaceholders(target, task));
                                } else if(tag.equalsIgnoreCase("[OPENGUIMENU] ")){

                                    String guiName = task;
                                    File file = customLoader.getFile(guiName);

                                    armory.setCustomGUIItems(file, player, target);

                                } else if(tag.equalsIgnoreCase("[CLOSE]")){
                                    player.closeInventory();
                                }

                            }
                        }
                    }
                }
            }

        });
    }

    public void addCustomListener(File files, Player player, Player target, FastInv inventory){
        inventory.addClickHandler(event -> {
            ItemStack item = event.getCurrentItem();
            Armory armory = Profiles.getInstance().getArmory();
            CustomLoader customLoader = Profiles.getInstance().getCustomLoader();
            FileConfiguration fileconfig = YamlConfiguration.loadConfiguration(files);
            FileConfiguration config = Profiles.getInstance().getConfig();

            if(fileconfig.getBoolean("fill-inventory.enabled")){
                ItemStack material = XMaterial.matchXMaterial(fileconfig.getString("fill-inventory.item.material")).get().parseItem();
                int amount = fileconfig.getInt("fill-inventory.item.amount");
                String name = fileconfig.getString("fill-inventory.item.name");
                ItemStack fill = ItemBuilder.start(material).amount(amount).name(name).flag(ItemFlag.HIDE_ATTRIBUTES).build();
                if(item.isSimilar(fill)) return;
            }

            if(item == null || item.getType() == Material.AIR) return;
            event.setCancelled(true);
            if(config.getBoolean("options.update-gui-on-click")){
                armory.setCustomGUIItems(files, player, target);
            }

            for(String keys : fileconfig.getConfigurationSection("items").getKeys(false)){
                if(event.getSlot() == fileconfig.getInt("items." + keys + ".slot")){
                    if(event.getClick().equals(ClickType.LEFT)){
                        for(String commands : fileconfig.getStringList("items." + keys + ".left-click-commands")){
                            if(!fileconfig.getStringList("items." + keys + ".left-click-commands").isEmpty()){

                                String[] cmds = pattern.split(commands);
                                String tag = cmds[0];
                                String task = this.parse(target, cmds.length > 1 ? cmds[1]: "");

                                if(tag.equalsIgnoreCase("[CONSOLE] ")){
                                    this.consoleCommand(task);
                                } else if(tag.equalsIgnoreCase("[PLAYER] ")){
                                    player.chat("/" + task);
                                } else if(tag.equalsIgnoreCase("[MESSAGE] ")){
                                    player.sendMessage(this.parse(target, task));
                                } else if(tag.equalsIgnoreCase("[OPENGUIMENU] ")){

                                    String guiName = task;
                                    File file = customLoader.getFile(guiName);

                                    armory.setCustomGUIItems(file, player, target);

                                } else if(tag.equalsIgnoreCase("[CLOSE]")){
                                    player.closeInventory();
                                }

                            }
                        }
                    } else if(event.getClick().equals(ClickType.RIGHT)){
                        for(String commands : fileconfig.getStringList("items." + keys + ".right-click-commands")){
                            if(!fileconfig.getStringList("items." + keys + ".right-click-commands").isEmpty()){

                                String[] cmds = pattern.split(commands);
                                String tag = cmds[0];
                                String task = this.parse(player, cmds.length > 1 ? cmds[1]: "");

                                if(tag.equalsIgnoreCase("[CONSOLE] ")){
                                    this.consoleCommand(task);
                                } else if(tag.equalsIgnoreCase("[PLAYER] ")){
                                    player.chat("/" + task);
                                } else if(tag.equalsIgnoreCase("[MESSAGE] ")){
                                    player.sendMessage(this.parse(target, task));
                                } else if(tag.equalsIgnoreCase("[OPENGUIMENU] ")){

                                    String guiName = task;
                                    File file = customLoader.getFile(guiName);

                                    armory.setCustomGUIItems(file, player, target);

                                } else if(tag.equalsIgnoreCase("[CLOSE]")){
                                    player.closeInventory();
                                }

                            }
                        }
                    }
                }
            }

        });
    }

    public void createNewFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exampleFile(File file){

        FileConfiguration fileconfig = YamlConfiguration.loadConfiguration(file);

        List<String> lore = new ArrayList<>();
        lore.add("&6Click me to bless you!");

        List<String> leftCommands = new ArrayList<>();
        leftCommands.add("[CONSOLE] eco give %player_name% 100");
        leftCommands.add("[MESSAGE] &aYou have been blessed!");
        leftCommands.add("[CLOSE]");

        List<String> rightCommands = new ArrayList<>();
        rightCommands.add("[CONSOLE] eco give %player_name% 100");
        rightCommands.add("[MESSAGE] &aYou have been blessed!");
        rightCommands.add("[CLOSE]");

        fileconfig.set("title", "&6Example Custom GUI");
        fileconfig.set("size", 54);

        fileconfig.set("fill-inventory.enabled", true);
        fileconfig.set("fill-inventory.item.material", "BLACK_STAINED_GLASS_PANE");
        fileconfig.set("fill-inventory.item.amount", 1);
        fileconfig.set("fill-inventory.item.name", "&f");

        // Bless Items
        fileconfig.set("items.exampleItem.material", "DIAMOND");
        fileconfig.set("items.exampleItem.amount", 1);
        fileconfig.set("items.exampleItem.name", "&bI am gonna bless you!");
        fileconfig.set("items.exampleItem.slot", 22);
        fileconfig.set("items.exampleItem.glowing", true);
        fileconfig.set("items.exampleItem.CustomModelData", 0);
        fileconfig.set("items.exampleItem.hide_attributes", true);
        fileconfig.set("items.exampleItem.usePermission", false);
        fileconfig.set("items.exampleItem.permission", "custom.permission");
        fileconfig.set("items.exampleItem.lore", lore);
        fileconfig.set("items.exampleItem.left-click-commands", leftCommands);
        fileconfig.set("items.exampleItem.right-click-commands", rightCommands);

        List<String> lores = new ArrayList<>();
        lores.add("&7Click to go back to your profile!");

        List<String> lleftCommands = new ArrayList<>();
        lleftCommands.add("[CLOSE]");
        lleftCommands.add("[PLAYER] profile");

        List<String> rrightCommands = new ArrayList<>();
        lleftCommands.add("[CLOSE]");
        lleftCommands.add("[PLAYER] profile");

        // Back to profile
        fileconfig.set("items.backToProfile.material", "head;eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE2ZWEzNGE2YTZlYzVjMDUxZTY5MzJmMWM0NzFiNzAxMmIyOThkMzhkMTc5ZjFiNDg3YzQxM2Y1MTk1OWNkNCJ9fX0=");
        fileconfig.set("items.backToProfile.amount", 1);
        fileconfig.set("items.backToProfile.name", "&6Back to profile!");
        fileconfig.set("items.backToProfile.slot", 31);
        fileconfig.set("items.backToProfile.glowing", false);
        fileconfig.set("items.backToProfile.CustomModelData", 0);
        fileconfig.set("items.backToProfile.hide_attributes", true);
        fileconfig.set("items.backToProfile.usePermission", false);
        fileconfig.set("items.backToProfile.permission", "custom.permission");
        fileconfig.set("items.backToProfile.lore", lores);
        fileconfig.set("items.backToProfile.left-click-commands", lleftCommands);
        fileconfig.set("items.backToProfile.right-click-commands", rrightCommands);

        try{
            fileconfig.save(file);
        }catch(IOException e){
            e.printStackTrace();
        }

        this.sendConsoleLog("Default Custom GUI has been created");

    }

    public String listFiles() {

        ArrayList<String> list = new ArrayList<>();
        File[] fileList = new File(Profiles.getInstance().getDataFolder() + File.separator + "guis").listFiles();

        String fileName;
        String replace;
        String[] splitted;
        String finalName;

        for(File file : fileList) {

            fileName = file.getName();
            replace = fileName.replace(".", ";");
            splitted = replace.split(";");
            finalName = splitted[0];

            list.add(finalName);

        }

        String newList = String.join("&f,&a ", list);
        return newList;

    }

    public String parse(Player player, String string){
        return PlaceholderAPI.setPlaceholders(player, string);
    }

}

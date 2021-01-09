package me.aglerr;

import com.tchristofferson.configupdater.ConfigUpdater;
import me.aglerr.commands.PlayerProfiles;
import me.aglerr.commands.Profile;
import me.aglerr.configs.CustomItems;
import me.aglerr.events.PlayerInteract;
import me.aglerr.metrics.MetricsLite;
import me.aglerr.utils.Armory;
import me.aglerr.utils.Utils;
import me.aglerr.utils.customgui.CustomLoader;
import me.aglerr.utils.customitems.Loader;
import me.aglerr.utils.fastinv.FastInvManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Profiles extends JavaPlugin {

    private static Profiles instance;
    private Utils utils = new Utils();
    private Armory armory = new Armory();

    public static boolean worldGuardHook = false;
    public static boolean combatLogXHook = false;

    private CustomItems customItems = new CustomItems();
    private Loader loader = new Loader();
    private CustomLoader customLoader = new CustomLoader();

    private HashMap<UUID, String> blockedPlayer = new HashMap<>();

    @Override
    public void onEnable(){

        File directory = new File(this.getDataFolder() + File.separator + "guis");
        if(!directory.exists()) {
            directory.mkdirs();
        }

        instance = this;

        FastInvManager.register(this);
        registerConfigs();
        registerEvents();
        registerCommands();

        loader.load();
        customLoader.load();

        MetricsLite metricsLite = new MetricsLite(this, 7049);
        utils.sendConsoleLog("Player Profiles has been successfully loaded!");
        utils.sendConsoleLog("You are currently running version " + this.getDescription().getVersion());
        registerHooks();

        updateConfig();

    }

    @Override
    public void onDisable(){

        clearHash();

    }

    private void updateConfig(){
        File configFile = new File(this.getDataFolder(), "config.yml");
        ArrayList<String> list = new ArrayList<>();

        try{
            ConfigUpdater.update(this, "config.yml", configFile, list);
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    private void registerConfigs(){

        // config.yml
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        // custom-items.yml
        customItems.setup();

    }

    private void registerEvents(){

        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new PlayerInteract(), this);

    }

    private void registerCommands(){
        this.getCommand("profile").setExecutor(new Profile());
        this.getCommand("playerprofiles").setExecutor(new PlayerProfiles());
    }

    private void registerHooks(){
        PluginManager pm = Bukkit.getServer().getPluginManager();
        int totalHooks = 0;
        if(pm.getPlugin("PlaceholderAPI") != null){
            utils.sendConsoleLog("PlaceholderAPI was found, enabling hooks...");
            totalHooks++;
        }

        /*if(pm.getPlugin("WorldGuard") != null){
            utils.sendConsoleLog("WorldGuard was found, enabling hooks...");
            worldGuardHook = true;
            totalHooks++;
        }*/

        if(pm.getPlugin("CombatLogX") != null){
            utils.sendConsoleLog("CombatLogX was found, enabling hooks...");
            combatLogXHook = true;
            totalHooks++;
        }

        utils.sendConsoleLog("Successfully hooked " + totalHooks + " plugins, enjoy!");

    }

    public void reloadAllConfig(){

        this.reloadConfig();
        this.getCustomItems().reloadData();

        this.getLoader().load();
        this.getCustomLoader().load();

    }

    private void clearHash(){
        loader.getItems().clear();
        customLoader.getCustomItems().clear();
        blockedPlayer.clear();
    }

    public HashMap<UUID, String> getBlockedPlayer() { return blockedPlayer; }
    public static Profiles getInstance() {
        return instance;
    }
    public Utils getUtils(){
        return utils;
    }
    public Armory getArmory() { return armory; }
    public CustomItems getCustomItems() { return customItems; }
    public Loader getLoader() { return loader; }
    public CustomLoader getCustomLoader() { return customLoader; }

}

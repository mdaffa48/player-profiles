package me.aglerr.commands;

import me.aglerr.Profiles;
import me.aglerr.utils.Armory;
import me.aglerr.utils.Utils;
import me.aglerr.utils.customgui.CustomLoader;
import me.aglerr.utils.fastinv.FastInvManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PlayerProfiles implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        FileConfiguration config = Profiles.getInstance().getConfig();
        Utils utils = Profiles.getInstance().getUtils();
        CustomLoader customLoader = Profiles.getInstance().getCustomLoader();
        Armory armory = Profiles.getInstance().getArmory();

        if(args.length == 0){
            sendHelpMessages(sender);

        } else if(args[0].equalsIgnoreCase("help")){
            sendHelpMessages(sender);

        } else if(args[0].equalsIgnoreCase("reload")){
            if(!(sender.hasPermission("playerprofiles.admin"))) {
                sender.sendMessage(utils.color(utils.getPrefix() +
                        " &cYou are lacking permission 'playerprofiles.admin'"));
                return true;
            }

            FastInvManager.closeAll();

            Profiles.getInstance().getLoader().getItems().clear();
            Profiles.getInstance().getCustomLoader().getCustomItems().clear();

            new BukkitRunnable(){
                public void run(){
                    Profiles.getInstance().reloadAllConfig();
                }
            }.runTaskLaterAsynchronously(Profiles.getInstance(), 10);

            sender.sendMessage(utils.color(config.getString("messages.reload"))
            .replace("%prefix%", utils.color(utils.getPrefix())));

        } else if(args[0].equalsIgnoreCase("creategui")){
            if(!(sender.hasPermission("playerprofiles.admin"))) {
                sender.sendMessage(utils.color(utils.getPrefix() +
                        " &cYou are lacking permission 'playerprofiles.admin'"));
                return true;
            }

            if(args.length < 2){
                sender.sendMessage(ChatColor.RED + "Invalid usage: /playerprofiles creategui <name>");
                return true;
            }

            String guiName = args[1];
            File file = customLoader.getFile(guiName);
            if(!file.exists()){
                file.getParentFile().mkdirs();

                utils.createNewFile(file);
                utils.exampleFile(file);

                sender.sendMessage(utils.color(config.getString("messages.createGUI"))
                        .replace("%prefix%", utils.color(utils.getPrefix()))
                        .replace("%guiName%", guiName));

            } else {

                sender.sendMessage(utils.color(config.getString("messages.guiAlreadyExist"))
                        .replace("%prefix%", utils.color(utils.getPrefix()))
                        .replace("%guiName%", guiName));
                return true;

            }

        } else if(args[0].equalsIgnoreCase("deletegui")){
            if(!(sender.hasPermission("playerprofiles.admin"))) {
                sender.sendMessage(utils.color(utils.getPrefix() +
                        " &cYou are lacking permission 'playerprofiles.admin'"));
                return true;
            }

            if(args.length < 2){
                sender.sendMessage(ChatColor.RED + "Invalid usage: /playerprofiles deletegui <name>");
                return true;
            }

            String guiName = args[1];
            File file = customLoader.getFile(guiName);
            if(file.exists()){
                file.delete();
                sender.sendMessage(utils.color(config.getString("messages.deleteGUI"))
                        .replace("%prefix%", utils.color(utils.getPrefix()))
                        .replace("%guiName%", guiName));

            } else {

                sender.sendMessage(utils.color(config.getString("messages.guiNotFound"))
                        .replace("%prefix%", utils.color(utils.getPrefix()))
                        .replace("%guiName%", guiName));
                return true;

            }

        } else if(args[0].equalsIgnoreCase("listgui")){
            if(!(sender.hasPermission("playerprofiles.admin"))) {
                sender.sendMessage(utils.color(utils.getPrefix() +
                        " &cYou are lacking permission 'playerprofiles.admin'"));
                return true;
            }

            sender.sendMessage(utils.color(utils.getPrefix() + " &fLoaded GUIs: &a" + utils.listFiles()));

        } else if(args[0].equalsIgnoreCase("opengui")){
            if(!(sender.hasPermission("playerprofiles.admin"))) {
                sender.sendMessage(utils.color(utils.getPrefix() +
                        " &cYou are lacking permission 'playerprofiles.admin'"));
                return true;
            }

            if(args.length < 2){
                sender.sendMessage(ChatColor.RED + "Invalid usage: /playerprofiles opengui <name>");
                return true;
            }

            String guiName = args[1];
            File file = customLoader.getFile(guiName);

            if(sender instanceof Player){
                Player player = (Player) sender;

                if(file.exists()){
                    armory.setCustomGUIItems(file, player, player);

                } else {
                    sender.sendMessage(utils.color(config.getString("messages.guiNotFound"))
                    .replace("%prefix%", utils.color(utils.getPrefix()))
                    .replace("%guiName%", guiName));
                    return true;
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }


        } else if(args[0].equalsIgnoreCase("toggle")){
            if(!(sender.hasPermission("playerprofiles.toggle"))) {
                sender.sendMessage(utils.color(utils.getPrefix() +
                        " &cYou are lacking permission 'playerprofiles.toggle'"));
                return true;
            }

            if(sender instanceof Player){
                Player player = (Player) sender;

                if(Profiles.getInstance().getBlockedPlayer().containsKey(player.getUniqueId())){
                    Profiles.getInstance().getBlockedPlayer().remove(player.getUniqueId());
                    for(String msg : config.getStringList("messages.unlockProfile")){
                        player.sendMessage(utils.color(msg)
                                .replace("%prefix%", utils.color(utils.getPrefix())));
                    }
                } else {
                    Profiles.getInstance().getBlockedPlayer().put(player.getUniqueId(), player.getName());
                    for(String msg : config.getStringList("messages.lockProfile")){
                        player.sendMessage(utils.color(msg)
                                .replace("%prefix%", utils.color(utils.getPrefix())));
                    }
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }
        }

        return false;
    }

    private void sendHelpMessages(CommandSender sender){
        FileConfiguration config = Profiles.getInstance().getConfig();
        Utils utils = Profiles.getInstance().getUtils();

        if(sender.hasPermission("playerprofiles.admin")){
            for(String msg : config.getStringList("messages.help")){
                sender.sendMessage(utils.color(msg));
            }
        } else {
            for(String msg : config.getStringList("messages.help-player")){
                sender.sendMessage(utils.color(msg));
            }
        }

    }

}

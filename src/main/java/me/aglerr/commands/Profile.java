package me.aglerr.commands;

import me.aglerr.Profiles;
import me.aglerr.utils.Armory;
import me.aglerr.utils.Utils;
import me.aglerr.utils.fastinv.FastInv;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public class Profile implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Utils utils = Profiles.getInstance().getUtils();
        Armory armory = Profiles.getInstance().getArmory();
        FileConfiguration config = Profiles.getInstance().getConfig();

        if(sender instanceof Player){
            Player player = (Player) sender;

            if(args.length == 0){
                if(!(sender.hasPermission("playerprofiles.profiles"))) {
                    sender.sendMessage(utils.color(utils.getPrefix() +
                            " &cYou are lacking permission 'playerprofiles.profiles'"));
                    return true;
                }

                FastInv inventory = new FastInv(config.getInt("gui.size"), InventoryType.CHEST, PlaceholderAPI.setPlaceholders(player, config.getString("gui.title")), player);

                armory.setItems(player, player, inventory);
                utils.addClickListener(player, player, inventory);
                inventory.open(player);


            } else if(args.length == 1){
                if(!(sender.hasPermission("playerprofiles.profiles.others"))) {
                    sender.sendMessage(utils.color(utils.getPrefix() +
                            " &cYou are lacking permission 'playerprofiles.profiles.others'"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    sender.sendMessage(utils.color(config.getString("messages.target-not-found"))
                    .replace("%prefix%", utils.color(utils.getPrefix())));
                    return true;
                } else {

                    FastInv inventory = new FastInv(config.getInt("gui.size"), InventoryType.CHEST, PlaceholderAPI.setPlaceholders(target, config.getString("gui.title")), target);

                    armory.setItems(target, player, inventory);
                    utils.addClickListener(player, target, inventory);
                    inventory.open(player);

                    player.sendMessage(PlaceholderAPI.setPlaceholders(target, config.getString("messages.open-message"))
                    .replace("%prefix%", utils.color(utils.getPrefix())));

                }

            }

        } else {
            sender.sendMessage(utils.color("&cOnly players can execute this command!"));
            return true;
        }
        return false;
    }

}

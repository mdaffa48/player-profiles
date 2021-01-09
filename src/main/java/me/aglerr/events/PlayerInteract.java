package me.aglerr.events;

import me.aglerr.Profiles;
import me.aglerr.utils.Armory;
import me.aglerr.utils.Utils;
import me.aglerr.utils.fastinv.FastInv;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.UUID;

public class PlayerInteract implements Listener {

    public HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event){
        Entity entity = event.getRightClicked();
        if(!(entity instanceof Player)) return;

        Player player = event.getPlayer();
        Player target = (Player) entity;

        Utils utils = Profiles.getInstance().getUtils();
        FileConfiguration config = Profiles.getInstance().getConfig();
        Armory armory = Profiles.getInstance().getArmory();

        if(event.isCancelled()) return;
        if(utils.hasOffhand()){
            if(!event.getHand().equals(EquipmentSlot.HAND)){
                return;
            }
        }

        if(config.getBoolean("options.shift-click")){
            if(!player.isSneaking()) return;
        }

        if(config.getBoolean("options.disable-npc")){
            if(entity.hasMetadata("NPC")){
                return;
            }
        }

        if(config.getBoolean("options.interact-permission")){
            if(!(player.hasPermission("playerprofiles.viewinteract"))){
                player.sendMessage(utils.color(utils.getPrefix() + " &cYou are lacking permission 'playerprofiles.viewinteract'"));
                return;
            }
        }


        if(cooldowns.containsKey(player.getUniqueId())) {
            int cooldownTime = config.getInt("cooldown.duration");
            long timeLeft = ((cooldowns.get(player.getUniqueId())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
            if(timeLeft > 0) {
                player.sendMessage(utils.color(config.getString("messages.on-cooldown")
                        .replace("%time%", String.valueOf(timeLeft)))
                        .replace("%prefix%", utils.color(utils.getPrefix())));
                return;
            }
        }

        for(String world : config.getStringList("disabledWorlds")){
            if(!player.hasPermission("playerprofiles.bypass.world")){
                if(target.getWorld().getName().equals(world)){
                    player.sendMessage(utils.color(config.getString("messages.disabled-worlds"))
                            .replace("%prefix%", utils.color(utils.getPrefix())));
                    return;
                }
            }
        }

        if(config.getBoolean("options.disable-in-combat")){
            if(!player.hasPermission("playerprofiles.bypass.combat")){
                if(utils.checkCombat(player)){
                    player.sendMessage(utils.color(config.getString("messages.in-combat"))
                            .replace("%prefix%", utils.color(utils.getPrefix())));
                    return;
                }
            }
        }

        if(Profiles.getInstance().getBlockedPlayer().containsKey(target.getUniqueId())){
            if(!(player.hasPermission("playerprofiles.bypass.blocked"))){
                player.sendMessage(utils.color(config.getString("messages.profileLocked"))
                        .replace("%prefix%", utils.color(utils.getPrefix())));
                return;
            }
        }

        if(config.getBoolean("cooldown.enabled")) {
            if (!(player.hasPermission("playerprofiles.bypass.cooldown"))) {
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }

        FastInv inventory = new FastInv(config.getInt("gui.size"), InventoryType.CHEST, PlaceholderAPI.setPlaceholders(target, config.getString("gui.title")), target);
        inventory.addCloseHandler(events -> {
            Player closedPlayer = (Player) events.getPlayer();
        });

        armory.setItems(target, player, inventory);
        utils.addClickListener(player, target, inventory);
        inventory.open(player);

    }

}

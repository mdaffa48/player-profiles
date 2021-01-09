package me.aglerr.utils;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import me.aglerr.Profiles;
import me.aglerr.utils.customgui.CustomLoader;
import me.aglerr.utils.customitems.Items;
import me.aglerr.utils.fastinv.FastInv;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;

public class Armory {

    private ItemStack checkArmor(Player player, String armorType){
        if(armorType.equals("helmet")){
            return player.getInventory().getHelmet();
        } else if(armorType.equals("chestplate")){
            return player.getInventory().getChestplate();
        } else if(armorType.equals("leggings")){
            return player.getInventory().getLeggings();
        } else if(armorType.equals("boots")){
            return player.getInventory().getBoots();
        }
        return null;
    }

    private ItemStack checkHand(Player player, String handType){
        Utils utils = Profiles.getInstance().getUtils();
        if(handType.equals("main-hand")){
            return player.getItemInHand();
        } else if(handType.equals("off-hand")){
            if(utils.hasOffhand()){
                return player.getInventory().getItemInOffHand();
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     *
     * @param player - get player's armor.
     * @param armorType - what armor should we get? (helmet, leggings, chestplate, boots)
     * @return
     */
    public ItemStack getArmor(Player player, String armorType){

        FileConfiguration config = Profiles.getInstance().getConfig();
        Utils utils = Profiles.getInstance().getUtils();

        ItemStack armor = null;
        if(config.getBoolean("gui.items." + armorType + ".enabled")){
            if(checkArmor(player, armorType) == null || checkArmor(player, armorType).getType() == Material.AIR){
                String material = config.getString("gui.items." + armorType + ".material");
                String name = PlaceholderAPI.setPlaceholders(player, config.getString("gui.items." + armorType + ".name"));
                List<String> lore = PlaceholderAPI.setPlaceholders(player, config.getStringList("gui.items." + armorType + ".lore"));

                if(material.contains(";")){
                    String[] mat = material.split(";");
                    if(mat[0].equalsIgnoreCase("head")){
                        armor = XMaterial.PLAYER_HEAD.parseItem();
                        ItemMeta im = armor.getItemMeta();
                        SkullUtils.applySkin(im, PlaceholderAPI.setPlaceholders(player, mat[1]));
                        im.setDisplayName(name);
                        im.setLore(lore);
                        armor.setItemMeta(im);
                        return armor;
                    }
                } else {

                    armor = ItemBuilder.start(XMaterial.matchXMaterial(material).get().parseItem())
                            .name(name)
                            .lore(lore)
                            .build();

                    if(config.getBoolean("gui.items." + armorType + ".glowing")){
                        armor = ItemBuilder.start(armor)
                                .enchant(Enchantment.DURABILITY)
                                .flag(ItemFlag.HIDE_ENCHANTS)
                                .build();
                    }

                    if(config.getBoolean("gui.items." + armorType + ".hide_attributes")){
                        armor = ItemBuilder.start(armor)
                                .flag(ItemFlag.HIDE_ATTRIBUTES)
                                .build();
                    }

                    if(utils.checkModelData()){
                        int model = config.getInt("gui.items." + armorType + ".CustomModelData");
                        armor = ItemBuilder.start(armor)
                                .customModelData(model)
                                .build();

                    }

                    return armor;

                }
            } else {

                armor = checkArmor(player, armorType);
                return armor;
            }

        }
        return null;
    }

    public ItemStack getHand(Player player, String handType){

        FileConfiguration config = Profiles.getInstance().getConfig();
        Utils utils = Profiles.getInstance().getUtils();

        ItemStack hand = null;
        if(config.getBoolean("gui.items." + handType + ".enabled")) {
            if (checkHand(player, handType) == null || checkHand(player, handType).getType() == Material.AIR) {
                String material = config.getString("gui.items." + handType + ".material");
                String name = PlaceholderAPI.setPlaceholders(player, config.getString("gui.items." + handType + ".name"));
                List<String> lore = PlaceholderAPI.setPlaceholders(player, config.getStringList("gui.items." + handType + ".lore"));

                if (material.contains(";")) {
                    String[] mat = material.split(";");
                    if (mat[0].equalsIgnoreCase("head")) {
                        hand = XMaterial.PLAYER_HEAD.parseItem();
                        ItemMeta im = hand.getItemMeta();
                        SkullUtils.applySkin(im, PlaceholderAPI.setPlaceholders(player, mat[1]));
                        im.setDisplayName(name);
                        im.setLore(lore);
                        hand.setItemMeta(im);
                        return hand;
                    }
                } else {

                    hand = ItemBuilder.start(XMaterial.matchXMaterial(material).get().parseItem())
                            .name(name)
                            .lore(lore)
                            .build();

                    if (config.getBoolean("gui.items." + handType + ".glowing")) {
                        hand = ItemBuilder.start(hand)
                                .enchant(Enchantment.DURABILITY)
                                .flag(ItemFlag.HIDE_ENCHANTS)
                                .build();
                    }

                    if (config.getBoolean("gui.items." + handType + ".hide_attributes")) {
                        hand = ItemBuilder.start(hand)
                                .flag(ItemFlag.HIDE_ATTRIBUTES)
                                .build();
                    }

                    if (utils.checkModelData()) {
                        int model = config.getInt("gui.items." + handType + ".CustomModelData");
                        hand = ItemBuilder.start(hand)
                                .customModelData(model)
                                .build();

                    }

                    return hand;

                }
            } else {

                hand = checkHand(player, handType);
                return hand;

            }
        }
        return null;

    }

    public void setCustomItems(Player player, Player target, FastInv inventory){
        Utils utils = Profiles.getInstance().getUtils();

        for(Items items : Profiles.getInstance().getLoader().getItems()){

            String material = items.getMaterial();
            String name = PlaceholderAPI.setPlaceholders(player, items.getName());
            List<String> lore = PlaceholderAPI.setPlaceholders(player, items.getLore());
            int amount = items.getAmount();
            int slot = items.getSlot();

            ItemStack stack = null;
            if (material.contains(";")) {
                String[] mat = material.split(";");
                if (mat[0].equalsIgnoreCase("head")) {

                    stack = XMaterial.PLAYER_HEAD.parseItem();
                    ItemMeta im = stack.getItemMeta();
                    SkullUtils.applySkin(im, PlaceholderAPI.setPlaceholders(player, mat[1]));
                    im.setDisplayName(utils.color(name));
                    im.setLore(utils.color(lore));
                    stack.setItemMeta(im);

                } else if(mat[0].equalsIgnoreCase("playerInventory")){

                    stack = target.getInventory().getItem(Integer.parseInt(mat[1]));

                }
            } else {

                stack = ItemBuilder.start(XMaterial.matchXMaterial(material).get().parseItem())
                        .name(name)
                        .lore(lore)
                        .amount(amount)
                        .build();

                if(items.isGlowing()){
                    stack = ItemBuilder.start(stack)
                            .enchant(Enchantment.DURABILITY)
                            .flag(ItemFlag.HIDE_ENCHANTS)
                            .build();
                }

                if(items.isHide_attributes()){
                    stack = ItemBuilder.start(stack)
                            .flag(ItemFlag.HIDE_ATTRIBUTES)
                            .build();
                }

                if(utils.checkModelData()){
                    int modelData = items.getCustomModelData();
                    stack = ItemBuilder.start(stack)
                            .customModelData(modelData)
                            .build();
                }

            }

            if(items.isUsePermission()){
                if(target.hasPermission(items.getPermission()) || target.isOp()){
                    inventory.setItem(slot, stack);
                }
            } else {
                inventory.setItem(slot, stack);
            }

        }

    }

    public void setItems(Player player, Player target, FastInv inventory){

        FileConfiguration config = Profiles.getInstance().getConfig();

        int helmet = config.getInt("gui.items.helmet.slot");
        int chestplate = config.getInt("gui.items.chestplate.slot");
        int leggings = config.getInt("gui.items.leggings.slot");
        int boots = config.getInt("gui.items.boots.slot");
        int mainHand = config.getInt("gui.items.main-hand.slot");
        int offHand = config.getInt("gui.items.off-hand.slot");

        inventory.setItem(helmet, getArmor(player, "helmet"));
        inventory.setItem(chestplate, getArmor(player, "chestplate"));
        inventory.setItem(leggings, getArmor(player, "leggings"));
        inventory.setItem(boots, getArmor(player, "boots"));
        inventory.setItem(mainHand, getHand(player, "main-hand"));
        inventory.setItem(offHand, getHand(player, "off-hand"));
        this.setCustomItems(player, target, inventory);

        if(config.getBoolean("gui.fill-inventory.enabled")){
            ItemStack material = XMaterial.matchXMaterial(config.getString("gui.fill-inventory.item.material")).get().parseItem();
            int amount = config.getInt("gui.fill-inventory.item.amount");
            String name = config.getString("gui.fill-inventory.item.name");

            ItemStack stack = ItemBuilder.start(material).amount(amount).name(name).flag(ItemFlag.HIDE_ATTRIBUTES).build();
            for(int x = 0; x < config.getInt("gui.size"); x++){
                if(inventory.getInventory().getItem(x) == null || inventory.getInventory().getItem(x).getType() == Material.AIR){
                    inventory.setItem(x, stack);
                }
            }

        }

    }

    public void setCustomGUIItems(File file, Player player, Player target){
        Utils utils = Profiles.getInstance().getUtils();
        FileConfiguration config = Profiles.getInstance().getConfig();
        CustomLoader customLoader = Profiles.getInstance().getCustomLoader();
        if(file.exists()){

            FileConfiguration fileconfig = YamlConfiguration.loadConfiguration(file);
            FastInv inventory = new FastInv(fileconfig.getInt("size"), InventoryType.CHEST, utils.parse(target, fileconfig.getString("title")), target);
            for(String keys : fileconfig.getConfigurationSection("items").getKeys(false)){

                String material = fileconfig.getString("items." + keys + ".material");
                int amount = fileconfig.getInt("items." + keys + ".amount");
                String name = fileconfig.getString("items." + keys + ".name");
                int slot = fileconfig.getInt("items." + keys + ".slot");
                boolean isGlowing = fileconfig.getBoolean("items." + keys + ".glowing");
                boolean isHideAttributes = fileconfig.getBoolean("items." + keys + ".hide_attributes");
                boolean isUsePermission = fileconfig.getBoolean("items." + keys + ".usePermission");
                String permission = fileconfig.getString("items." + keys + ".permission");
                List<String> lore = fileconfig.getStringList("items." + keys + ".lore");
                List<String> leftCommands = fileconfig.getStringList("items." + keys + ".left-click-commands");
                List<String> rightCommands = fileconfig.getStringList("items." + keys + ".right-click-commands");
                int customModelData = fileconfig.getInt("items." + keys + ".CustomModelData");

                boolean isFillInventory = fileconfig.getBoolean("fill-inventory.enabled");
                ItemStack fillMaterial = XMaterial.matchXMaterial(fileconfig.getString("fill-inventory.item.material")).get().parseItem();
                int fillAmount = fileconfig.getInt("fill-inventory.item.amount");
                String fillName = fileconfig.getString("fill-inventory.item.name");
                int guiSize = fileconfig.getInt("size");

                ItemStack stack = null;
                if (material.contains(";")) {
                    String[] mat = material.split(";");
                    if (mat[0].equalsIgnoreCase("head")) {

                        stack = XMaterial.PLAYER_HEAD.parseItem();
                        ItemMeta im = stack.getItemMeta();
                        SkullUtils.applySkin(im, PlaceholderAPI.setPlaceholders(player, mat[1]));
                        im.setDisplayName(utils.color(name));
                        im.setLore(utils.color(lore));
                        stack.setItemMeta(im);

                    } else if(mat[0].equalsIgnoreCase("playerInventory")){

                        stack = target.getInventory().getItem(Integer.parseInt(mat[1]));

                    }
                } else {

                    stack = ItemBuilder.start(XMaterial.matchXMaterial(material).get().parseItem())
                            .name(name)
                            .lore(lore)
                            .amount(amount)
                            .build();

                    if(isGlowing){
                        stack = ItemBuilder.start(stack)
                                .enchant(Enchantment.DURABILITY)
                                .flag(ItemFlag.HIDE_ENCHANTS)
                                .build();
                    }

                    if(isHideAttributes){
                        stack = ItemBuilder.start(stack)
                                .flag(ItemFlag.HIDE_ATTRIBUTES)
                                .build();
                    }

                    if(utils.checkModelData()){
                        int modelData = customModelData;
                        stack = ItemBuilder.start(stack)
                                .customModelData(modelData)
                                .build();
                    }

                }

                if(isUsePermission){
                    if(player.hasPermission(permission) || player.isOp()){
                        inventory.setItem(slot, stack);
                    }
                } else {
                    inventory.setItem(slot, stack);
                }

                if(isFillInventory){
                    ItemStack fillStack = ItemBuilder.start(fillMaterial).amount(fillAmount).name(fillName).flag(ItemFlag.HIDE_ATTRIBUTES).build();
                    for(int x = 0; x < guiSize; x++){
                        if(inventory.getInventory().getItem(x) == null || inventory.getInventory().getItem(x).getType() == Material.AIR){
                            inventory.setItem(x, fillStack);
                        }
                    }
                }

            }

            utils.addCustomListener(file, player, target, inventory);
            inventory.open(player);

        } else {

            player.sendMessage(utils.color(utils.getPrefix() + " &cThe selected GUI is not exist! Please contact administrator"));
            return;

        }

    }

}
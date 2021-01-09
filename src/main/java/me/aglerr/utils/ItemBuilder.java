package me.aglerr.utils;

import me.aglerr.Profiles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack stack;
    public Utils utils = Profiles.getInstance().getUtils();

    /**
     * Create/Modify custom {@link ItemStack}s easily.
     * If you want to create rather than modify an ItemStack,
     * use {@link ItemBuilder#start(Material)} instead.
     *
     * <br>
     *
     * It supports shorthand for setting the following attributes:
     * <ul>
     *     <li>Name</li>
     *     <li>Amount</li>
     *     <li>Lore</li>
     *     <li>Data / Durability</li>
     * </ul>
     *
     * @param stack The ItemStack you want to modify.
     */
    public ItemBuilder(ItemStack stack){
        this.stack = stack;
    }

    public ItemBuilder(ItemStack stack, int amount, String name) {

        this.stack = stack;
        stack.setAmount(amount);
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(utils.color(name));
        stack.setItemMeta(stackMeta);

    }

    public ItemBuilder(ItemStack stack, int amount, String name, List<String> lore) {

        this.stack = stack;
        stack.setAmount(amount);
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(utils.color(name));
        stackMeta.setLore(utils.color(lore));
        stack.setItemMeta(stackMeta);

    }

    public ItemBuilder(ItemStack stack, int amount, String name, List<String> lore, Enchantment enchantment) {

        this.stack = stack;
        stack.setAmount(amount);
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(utils.color(name));
        stackMeta.setLore(utils.color(lore));
        stackMeta.addEnchant(enchantment, 0, true);
        stack.setItemMeta(stackMeta);

    }

    public static ItemBuilder start(ItemStack is) {
        return new ItemBuilder(is);
    }

    /**
     * Create a custom {@link ItemStack} easily.
     *
     * @param material The {@link Material} of the ItemStack that you want to create.
     * @return A new {@link ItemBuilder} object.
     */
    public static ItemBuilder start(Material material){
        return new ItemBuilder(new ItemStack(material));
    }

    public ItemBuilder enchant(Enchantment enchantment) {
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.addEnchant(enchantment, 0, true);
        stack.setItemMeta(stackMeta);
        return this;
    }

    public ItemBuilder flag(ItemFlag flag) {
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.addItemFlags(flag);
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * Sets the display name of the item.
     * Color Codes are supported (and should be prepended with an ampersand [&amp;]; e.g. &amp;c for red.)
     *
     * @param name The desired name of the ItemStack.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder name(String name){
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount The desired amount of the ItemStack.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder amount(int amount){
        stack.setAmount(amount);
        return this;
    }

    /**
     * Sets the lore of the item. (Shorthand - unlimited argument version)
     *
     * <br>
     *
     * Color Codes are supported (and should be prepended with an ampersand [&amp;]; e.g. &amp;c for red.)
     * <b>Note:</b> Each argument will be a line of the lore.
     *
     * @param lore The desired lore of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder lore(String... lore){
        for(int i = 0; i < lore.length; i++){
            lore[i] = ChatColor.translateAlternateColorCodes('&', lore[i]);
        }

        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setLore(Arrays.asList(lore));
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * Sets the lore of the item. (Reference - {@link List<String>} version)
     * This essentially just wrapper for {@link ItemMeta#setLore(List)}, however, with color replacement.
     *
     * <br>
     *
     * Color Codes are supported (and should be prepended with an ampersand [&amp;]; e.g. &amp;c for red.)
     *
     * @param lore The desired lore of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder lore(List<String> lore){
        for(int i = 0; i < lore.size(); i++){
            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
        }

        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setLore(lore);
        stack.setItemMeta(stackMeta);
        return this;
    }

    public ItemBuilder customModelData(int number){
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setCustomModelData(number);
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * An alias for {@link ItemBuilder#durability(short)}.
     * Intended to improve readability of code.
     *
     * @param data The desired data value (durability) of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    @SuppressWarnings("deprecation")
    public ItemBuilder data(short data){
        stack.setDurability(data);
        return this;
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability The desired durability of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    @SuppressWarnings("deprecation")
    public ItemBuilder durability(short durability){
        stack.setDurability(durability);
        return this;
    }

    /**
     * Returns the class' internally modified {@link ItemStack} object.
     *
     * @return The updated ItemStack.
     */
    public ItemStack build(){
        return stack;
    }

}

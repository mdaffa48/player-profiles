package me.aglerr.utils.customgui;

import java.util.List;

public class CustomItems {

    private final String title;
    private final int size;

    private final boolean fillEnabled;
    private final String fillMaterial;
    private final String fillName;
    private final int fillAmount;

    private final String material;
    private final int amount;
    private final String name;
    private final int slot;
    private final boolean glowing;
    private final boolean hide_attributes;
    private final boolean usePermission;
    private final String permission;
    private final List<String> lore;
    private final List<String> leftCommands;
    private final List<String> rightCommands;
    private final int customModelData;

    public CustomItems(String title, int size, boolean fillEnabled, String fillMaterial, String fillName, int fillAmount,
                 String material, int amount, String name, int slot, boolean glowing, boolean hide_attributes,
                 boolean usePermission, String permission, List<String> lore, List<String> leftCommands,
                 List<String> rightCommands, int customModelData){
        this.title = title;
        this.size = size;
        this.fillEnabled = fillEnabled;
        this.fillMaterial = fillMaterial;
        this.fillName = fillName;
        this.fillAmount = fillAmount;
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.slot = slot;
        this.glowing = glowing;
        this.hide_attributes = hide_attributes;
        this.usePermission = usePermission;
        this.permission = permission;
        this.lore = lore;
        this.leftCommands = leftCommands;
        this.rightCommands = rightCommands;
        this.customModelData = customModelData;
    }


    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public boolean isFillEnabled() {
        return fillEnabled;
    }

    public String getFillMaterial() {
        return fillMaterial;
    }

    public String getFillName() {
        return fillName;
    }

    public int getFillAmount() {
        return fillAmount;
    }

    public String getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public boolean isHide_attributes() {
        return hide_attributes;
    }

    public boolean isUsePermission() {
        return usePermission;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getLeftCommands() {
        return leftCommands;
    }

    public List<String> getRightCommands() {
        return rightCommands;
    }

    public int getCustomModelData() {
        return customModelData;
    }
}

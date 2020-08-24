package gg.steve.mc.baseball.tntb.framework.gui.utils;

import gg.steve.mc.baseball.tntb.framework.utils.ItemBuilderUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class GuiItemUtil {

    public static ItemStack createItem(ConfigurationSection section) {
        ItemBuilderUtil builder = ItemBuilderUtil.getBuilderForMaterial(section.getString("material"), section.getString("data"));
        builder.addName(section.getString("name"));
        builder.addLore(section.getStringList("lore"));
        builder.addEnchantments(section.getStringList("enchantments"));
        builder.addItemFlags(section.getStringList("item-flags"));
        builder.addGuiNBT(section.getBoolean("unbreakable"));
        return builder.getItem();
    }
}

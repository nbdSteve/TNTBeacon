package gg.steve.mc.baseball.tntb.core;

import gg.steve.mc.baseball.tntb.framework.nbt.NBTItem;
import gg.steve.mc.baseball.tntb.framework.utils.ItemBuilderUtil;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TnTBeaconItem {
    private static ItemStack item;

    public static void init() {
        ConfigurationSection section = Files.CONFIG.get().getConfigurationSection("item");
        ItemBuilderUtil builder = ItemBuilderUtil.getBuilderForMaterial(section.getString("material"), section.getString("data"));
        builder.addName(section.getString("name"));
        builder.addLore(section.getStringList("lore"));
        builder.addEnchantments(section.getStringList("enchantments"));
        builder.addItemFlags(section.getStringList("item-flags"));
        item = builder.getItem();
    }

    public static void give(Player player) {
        NBTItem nbt = new NBTItem(item);
        nbt.setString("beacons.id", String.valueOf(UUID.randomUUID()));
        player.getInventory().addItem(nbt.getItem());
    }

    public static ItemStack getItem() {
        NBTItem nbt = new NBTItem(item);
        nbt.setString("beacons.id", String.valueOf(UUID.randomUUID()));
        return nbt.getItem();
    }
}

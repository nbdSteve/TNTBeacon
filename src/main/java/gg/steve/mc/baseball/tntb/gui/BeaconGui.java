package gg.steve.mc.baseball.tntb.gui;

import gg.steve.mc.baseball.tntb.core.TnTBeacon;
import gg.steve.mc.baseball.tntb.framework.gui.AbstractGui;
import gg.steve.mc.baseball.tntb.framework.gui.utils.GuiItemUtil;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BeaconGui extends AbstractGui {
    private ConfigurationSection section;

    /**
     * Constructor the create a new Gui
     */
    public BeaconGui() {
        super(Files.CONFIG.get().getConfigurationSection("gui"), Files.CONFIG.get().getString("gui.type"), Files.CONFIG.get().getInt("gui.size"));
        this.section = Files.CONFIG.get().getConfigurationSection("gui");
        List<Integer> slots = section.getIntegerList("fillers.slots");
        ItemStack filler = GuiItemUtil.createItem(section.getConfigurationSection("fillers"));
        for (Integer slot : slots) {
            setItemInSlot(slot, filler, player -> {
            });
        }
    }

    @Override
    public void refresh(TnTBeacon beacon) {
        for (String entry : section.getKeys(false)) {
            try {
                Integer.parseInt(entry);
            } catch (NumberFormatException e) {
                continue;
            }
            ItemStack item = GuiItemUtil.createItem(section.getConfigurationSection(entry));
            List<Integer> slots = section.getIntegerList(entry + ".slots");
            switch (section.getString(entry + ".action")) {
                case "fill-amt":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                        });
                    }
                    break;
                case "fill-max":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                            beacon.fill(-1, Files.CONFIG.get().getInt("starting-radius"));
                        });
                    }
                    break;
                case "extact":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                            beacon.extract(Files.CONFIG.get().getInt("starting-radius"));
                        });
                    }
                    break;
                case "close":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, HumanEntity::closeInventory);
                    }
                    break;
                default:
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                        });
                    }
            }
        }
    }
}

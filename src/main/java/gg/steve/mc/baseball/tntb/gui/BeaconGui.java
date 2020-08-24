package gg.steve.mc.baseball.tntb.gui;

import gg.steve.mc.baseball.tntb.Beacons;
import gg.steve.mc.baseball.tntb.core.TnTBeacon;
import gg.steve.mc.baseball.tntb.framework.gui.AbstractGui;
import gg.steve.mc.baseball.tntb.framework.gui.utils.GuiItemUtil;
import gg.steve.mc.baseball.tntb.framework.message.DebugMessage;
import gg.steve.mc.baseball.tntb.framework.message.GeneralMessage;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import gg.steve.mc.baseball.tntb.listener.ChatListener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class BeaconGui extends AbstractGui {
    private ConfigurationSection section;
    private TnTBeacon beacon;

    /**
     * Constructor the create a new Gui
     */
    public BeaconGui(TnTBeacon beacon) {
        super(Files.CONFIG.get().getConfigurationSection("gui"), Files.CONFIG.get().getString("gui.type"), Files.CONFIG.get().getInt("gui.size"));
        this.section = Files.CONFIG.get().getConfigurationSection("gui");
        List<Integer> slots = section.getIntegerList("fillers.slots");
        ItemStack filler = GuiItemUtil.createItem(section.getConfigurationSection("fillers"));
        for (Integer slot : slots) {
            setItemInSlot(slot, filler, player -> {
            });
        }
        this.beacon = beacon;
    }

    @Override
    public void refresh() {
        for (String entry : section.getKeys(false)) {
            try {
                Integer.parseInt(entry);
            } catch (NumberFormatException e) {
                continue;
            }
            ItemStack item = GuiItemUtil.createItem(section.getConfigurationSection(entry));
            List<Integer> slots = section.getIntegerList(entry + ".slots");
            switch (Objects.requireNonNull(section.getString(entry + ".action"))) {
                case "auto-fill-cancel":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                            player.closeInventory();
                            beacon.cancelAutofillTask();
                            GeneralMessage.AUTO_FILL_CANCEL.message(player);
                        });
                    }
                    break;
                case "fill-interval":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                            player.closeInventory();
                            ChatListener.active.remove(player.getUniqueId());
                            ChatListener.autofill.put(player.getUniqueId(), this.beacon);
                            GeneralMessage.AUTO_FILL_CHAT_INITIALISE.message(player);
                        });
                    }
                    break;
                case "fill-amt":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                            player.closeInventory();
                            ChatListener.autofill.remove(player.getUniqueId());
                            ChatListener.active.put(player.getUniqueId(), this.beacon);
                            GeneralMessage.CHAT_INITIALISE.message(player);
                        });
                    }
                    break;
                case "fill-max":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                            player.closeInventory();
                            int[] data = beacon.fill(-1, Files.CONFIG.get().getInt("starting-radius"), false);
                            if (data[0] == -2) {
                                DebugMessage.LARGER_AMOUNT_REQUIRED_MAX.message(player);
                            } else {
                                GeneralMessage.SUCCESSFUL_FILL.message(player,
                                        Beacons.formatNumber(data[0]),
                                        Beacons.formatNumber(data[1]),
                                        Beacons.formatNumber(data[0] / data[1]));
                            }
                        });
                    }
                    break;
                case "extract":
                    for (Integer slot : slots) {
                        setItemInSlot(slot, item, player -> {
                            int[] data = beacon.extract(Files.CONFIG.get().getInt("starting-radius"));
                            GeneralMessage.SUCCESSFUL_EXTRACT.message(player, Beacons.formatNumber(data[0]), Beacons.formatNumber(data[1]));
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

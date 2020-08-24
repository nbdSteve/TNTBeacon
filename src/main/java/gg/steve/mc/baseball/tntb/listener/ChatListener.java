package gg.steve.mc.baseball.tntb.listener;

import gg.steve.mc.baseball.tntb.Beacons;
import gg.steve.mc.baseball.tntb.core.TnTBeacon;
import gg.steve.mc.baseball.tntb.framework.message.DebugMessage;
import gg.steve.mc.baseball.tntb.framework.message.GeneralMessage;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {
    public static Map<UUID, TnTBeacon> active = new HashMap<>();
    public static Map<UUID, TnTBeacon> autofill = new HashMap<>();

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        if (!active.containsKey(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase("exit")) {
            active.remove(event.getPlayer().getUniqueId());
            GeneralMessage.ACTION_CANCELLED.message(event.getPlayer());
            return;
        }
        int amt;
        try {
            amt = Integer.parseInt(event.getMessage());
            if (amt <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            DebugMessage.INVALID_FILL_AMOUNT.message(event.getPlayer());
            return;
        }
        int[] data = active.get(event.getPlayer().getUniqueId()).fill(amt, Files.CONFIG.get().getInt("starting-radius"), false);
        if (data[0] == -1) {
            DebugMessage.INSUFFICIENT_TNT.message(event.getPlayer());
        } else if (data[0] == -2) {
            DebugMessage.LARGER_AMOUNT_REQUIRED.message(event.getPlayer());
        } else {
            GeneralMessage.SUCCESSFUL_FILL.message(event.getPlayer(),
                    Beacons.formatNumber(data[0]),
                    Beacons.formatNumber(data[1]),
                    Beacons.formatNumber(data[0] / data[1]));
            active.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void autofillChat(AsyncPlayerChatEvent event) {
        if (!autofill.containsKey(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase("exit")) {
            autofill.remove(event.getPlayer().getUniqueId());
            GeneralMessage.ACTION_CANCELLED.message(event.getPlayer());
            return;
        }
        int amt, mins, secs;
        try {
            String[] amount = event.getMessage().split("-");
            try {
                amt = Integer.parseInt(amount[0]);
                if (amt <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                DebugMessage.INVALID_FILL_AMOUNT.message(event.getPlayer());
                return;
            }
            String minutes = amount[1].split("m")[0];
            String seconds = amount[1].split("m")[1].split("s")[0];
            try {
                mins = Integer.parseInt(minutes);
                if (mins < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                DebugMessage.INVALID_MINUTES.message(event.getPlayer());
                return;
            }
            try {
                secs = Integer.parseInt(seconds);
                if (secs < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                DebugMessage.INVALID_SECONDS.message(event.getPlayer());
                return;
            }
            if (mins == 0 && secs == 0) {
                DebugMessage.INVALID_TIME.message(event.getPlayer());
                return;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            DebugMessage.INVALID_FORMAT.message(event.getPlayer());
            return;
        }
        autofill.get(event.getPlayer().getUniqueId()).assignAutofillTask(amt, (mins * 60 * 20) + (secs * 20));
        GeneralMessage.AUTO_FILL_SUCCESS.message(event.getPlayer(), Beacons.formatNumber(amt), Beacons.formatNumber(mins), Beacons.formatNumber(secs));
    }
}

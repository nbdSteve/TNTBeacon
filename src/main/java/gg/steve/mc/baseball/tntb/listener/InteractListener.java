package gg.steve.mc.baseball.tntb.listener;

import gg.steve.mc.baseball.tntb.managers.BeaconManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!BeaconManager.isBeaconBlock(event.getClickedBlock())) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage("inventory");
    }
}

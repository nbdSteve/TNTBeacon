package gg.steve.mc.baseball.tntb.listener;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.event.FactionDisbandEvent;
import com.massivecraft.factions.event.LandUnclaimAllEvent;
import com.massivecraft.factions.event.LandUnclaimEvent;
import gg.steve.mc.baseball.tntb.core.TnTBeacon;
import gg.steve.mc.baseball.tntb.core.TnTBeaconItem;
import gg.steve.mc.baseball.tntb.managers.BeaconManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;

public class FEventListener implements Listener {

    @EventHandler
    public void disband(FactionDisbandEvent event) {
        Set<FLocation> claims = event.getFPlayer().getFaction().getAllClaims();
        for (FLocation loc : claims) {
            if (BeaconManager.getBeacon(event.getFaction(), loc.getChunk()) == null) continue;
            TnTBeacon beacon = BeaconManager.getBeacon(event.getFaction(), loc.getChunk());
            BeaconManager.removeBeacon(event.getFaction(), loc.getChunk());
            beacon.getBlock().setType(Material.AIR);
            beacon.getWorld().dropItemNaturally(beacon.getBlock().getLocation(), TnTBeaconItem.getItem());
        }
    }

    @EventHandler
    public void unclaim(LandUnclaimEvent event) {
        if (BeaconManager.getBeacon(event.getFaction(), event.getLocation().getChunk()) == null) return;
        TnTBeacon beacon = BeaconManager.getBeacon(event.getFaction(), event.getLocation().getChunk());
        BeaconManager.removeBeacon(event.getFaction(), event.getLocation().getChunk());
        beacon.getBlock().setType(Material.AIR);
        beacon.getWorld().dropItemNaturally(beacon.getBlock().getLocation(), TnTBeaconItem.getItem());
    }

    @EventHandler
    public void unclaimAll(LandUnclaimAllEvent event) {
        Set<FLocation> claims = event.getFaction().getAllClaims();
        for (FLocation loc : claims) {
            if (BeaconManager.getBeacon(event.getFaction(), loc.getChunk()) == null) continue;
            TnTBeacon beacon = BeaconManager.getBeacon(event.getFaction(), loc.getChunk());
            BeaconManager.removeBeacon(event.getFaction(), loc.getChunk());
            beacon.getBlock().setType(Material.AIR);
            beacon.getWorld().dropItemNaturally(beacon.getBlock().getLocation(), TnTBeaconItem.getItem());
        }
    }
}

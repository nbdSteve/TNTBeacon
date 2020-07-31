package gg.steve.mc.baseball.tntb.listener;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import gg.steve.mc.baseball.tntb.core.TnTBeaconItem;
import gg.steve.mc.baseball.tntb.framework.nbt.NBTItem;
import gg.steve.mc.baseball.tntb.framework.utils.LogUtil;
import gg.steve.mc.baseball.tntb.managers.BeaconManager;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class PlacementListener implements Listener {

    @EventHandler
    public void place(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        if (event.getItemInHand().getType() == Material.AIR) return;
        if (!FPlayers.getInstance().getByPlayer(event.getPlayer()).hasFaction()) return;
        NBTItem item = new NBTItem(event.getItemInHand());
        if (item.getString("beacons.id").equalsIgnoreCase("")) return;
        UUID id = UUID.fromString(item.getString("beacons.id"));
        Faction faction = FPlayers.getInstance().getByPlayer(event.getPlayer()).getFaction();
        if (!Board.getInstance().getFactionAt(new FLocation(event.getBlock())).equals(faction)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You can only place tnt beacons in your claimed land.");
            return;
        }
        if (!BeaconManager.addBeacon(faction, event.getBlockPlaced(), id)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("There is already a tnt beacon in that chunk - you can not place another.");
            return;
        }
        Block block = event.getBlockPlaced();
        if (block.getType() == Material.BEACON) {
            Beacon beacon = (Beacon) block.getState();
//            beacon.setPrimaryEffect(PotionEffectType.SATURATION);
//            beacon.update(true);
        }
    }

    @EventHandler
    public void destroy(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (!BeaconManager.isBeaconBlock(event.getBlock())) return;
        event.setCancelled(true);
        Faction faction = Board.getInstance().getFactionAt(new FLocation(event.getBlock()));
        BeaconManager.removeBeacon(faction, event.getBlock().getChunk());
        event.getBlock().setType(Material.AIR);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), TnTBeaconItem.getItem());
    }
}

package gg.steve.mc.baseball.tntb.listener;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import gg.steve.mc.baseball.tntb.core.TnTBeacon;
import gg.steve.mc.baseball.tntb.framework.message.DebugMessage;
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
        Faction faction = FPlayers.getInstance().getByPlayer(event.getPlayer()).getFaction();
        if (!Board.getInstance().getFactionAt(new FLocation(event.getClickedBlock())).equals(faction)) {
            DebugMessage.OWNER_BY_OTHER_FACTION.message(event.getPlayer());
            return;
        }
        TnTBeacon beacon = BeaconManager.getBeacon(faction, event.getClickedBlock().getChunk());
        beacon.openGui(event.getPlayer());
    }
}

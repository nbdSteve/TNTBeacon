package gg.steve.mc.baseball.tntb.core;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TnTBeacon extends AbstractBeacon {
    private Chunk chunk;

    public TnTBeacon(UUID id, int x, int y, int z, World world, Faction faction) {
        super(id, BeaconType.TNT, x, y, z, world, faction);
        chunk = world.getChunkAt(world.getBlockAt(x, y, z));
    }

    public void fill(int tnt, int radius) {
        if (tnt == -1) tnt = getFaction().getTnt();
        List<Dispenser> dispensers = new ArrayList<>();
        for (int radX = -radius; radX <= radius; radX++) {
            for (int radZ = -radius; radZ <= radius; radZ++) {
                Chunk current = getWorld().getChunkAt(chunk.getX() + radX, chunk.getZ() + radZ);
                if (!Board.getInstance().getFactionAt(new FLocation(current.getBlock(0, 100, 0))).equals(getFaction())) continue;
                for (int blockX = 0; blockX <= 16; blockX++) {
                    for (int blockZ = 0; blockZ <= 16; blockZ++) {
                        for (int blockY = current.getWorld().getHighestBlockYAt(current.getBlock(blockX, 100, blockZ).getLocation()); blockY > 0; blockY--) {
                            Block block = current.getBlock(blockX, blockY, blockZ);
                            if (block.getType() == Material.AIR) continue;
                            if (block.getType() != Material.DISPENSER) continue;
                            Dispenser dispenser = (Dispenser) block.getState();
                            dispensers.add(dispenser);
                        }
                    }
                }
            }
        }
        int tntPerDispener = (int) Math.floor(tnt / dispensers.size());
        for (Dispenser dispenser : dispensers) {
            if (dispenser.getInventory().firstEmpty() == -1) continue;
            dispenser.getInventory().addItem(new ItemStack(Material.TNT, tntPerDispener));
            getFaction().takeTnt(tntPerDispener);
            dispenser.update(true);
        }
    }

    public void extract(int radius) {

    }

    public Chunk getChunk() {
        return chunk;
    }
}

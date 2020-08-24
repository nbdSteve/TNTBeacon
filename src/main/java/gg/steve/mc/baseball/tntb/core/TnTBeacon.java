package gg.steve.mc.baseball.tntb.core;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import gg.steve.mc.baseball.tntb.Beacons;
import gg.steve.mc.baseball.tntb.framework.message.GeneralMessage;
import gg.steve.mc.baseball.tntb.framework.utils.ColorUtil;
import gg.steve.mc.baseball.tntb.framework.utils.ItemBuilderUtil;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import gg.steve.mc.baseball.tntb.gui.BeaconGui;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TnTBeacon extends AbstractBeacon {
    private Chunk chunk;
    private BeaconGui gui;
    private Hologram hologram;
    private Block block;
    private BukkitTask task;

    public TnTBeacon(UUID id, int x, int y, int z, World world, Faction faction, int autofillAmt, long autofillDelay) {
        super(id, BeaconType.TNT, x, y, z, world, faction, autofillAmt, autofillDelay);
        this.chunk = world.getChunkAt(world.getBlockAt(x, y, z));
        this.block = getWorld().getBlockAt(getX(), getY(), getZ());
        this.gui = new BeaconGui(this);
        if (autofillAmt != 0 && autofillDelay != 0) {
            assignAutofillTask(autofillAmt, autofillDelay);
        }
        if (Files.CONFIG.get().getBoolean("hologram.enabled")) {
            this.hologram = HologramsAPI.createHologram(Beacons.getInstance(),
                    this.block.getLocation().add(0.5, Files.CONFIG.get().getDouble("hologram.offset"), 0.5));
            for (String line : Files.CONFIG.get().getStringList("hologram.text")) {
                this.hologram.appendTextLine(ColorUtil.colorize(line));
            }
            if (Files.CONFIG.get().getBoolean("hologram.item.enabled")) {
                ItemBuilderUtil builder = ItemBuilderUtil.getBuilderForMaterial(Files.CONFIG.get().getString("hologram.item.material"),
                        Files.CONFIG.get().getString("hologram.item.data"));
                this.hologram.appendItemLine(builder.getItem());
            }
        }
    }

    public int[] fill(int tnt, int radius, boolean autofill) {
        int[] data = new int[2];
        if (tnt == -1) tnt = getFaction().getTnt();
        List<Dispenser> dispensers;
        if (autofill) {
            dispensers = getDispensersAutofill(radius, tnt);
        } else {
            dispensers = getDispensers(radius);
        }
        int tntPerDispenser = tnt;
        if (tnt != getFaction().getTnt()) {
            if (getFaction().getTnt() < tnt * dispensers.size()) {
                data[0] = -1;
                return data;
            }
        } else {
            tntPerDispenser = tnt / dispensers.size();
        }
        if (tntPerDispenser <= 0) {
            data[0] = -2;
            return data;
        }
        for (Dispenser dispenser : dispensers) {
            if (dispenser.getInventory().firstEmpty() == -1) continue;
            dispenser.getInventory().addItem(new ItemStack(Material.TNT, tntPerDispenser));
            getFaction().takeTnt(tntPerDispenser);
            dispenser.update(true);
        }
        data[0] = tntPerDispenser * dispensers.size();
        data[1] = dispensers.size();
        return data;
    }

    public int[] extract(int radius) {
        List<Dispenser> dispensers = getDispensers(radius);
        int amt = 0;
        int[] data = new int[2];
        for (Dispenser dispenser : dispensers) {
            if (dispenser.getInventory().firstEmpty() == -1) continue;
            for (int i = 0; i < dispenser.getInventory().getSize(); i++) {
                if (dispenser.getInventory().getItem(i) == null || dispenser.getInventory().getItem(i).getType() == Material.AIR)
                    continue;
                if (dispenser.getInventory().getItem(i).getType() == Material.TNT) {
                    amt += dispenser.getInventory().getItem(i).getAmount();
                    dispenser.getInventory().clear(i);
                    dispenser.update(true);
                }
            }
        }
        getFaction().addTnt(amt);
        data[0] = amt;
        data[1] = dispensers.size();
        return data;
    }

    public List<Dispenser> getDispensers(int radius) {
        List<Dispenser> dispensers = new ArrayList<>();
        for (int radX = -radius; radX <= radius; radX++) {
            for (int radZ = -radius; radZ <= radius; radZ++) {
                Chunk current = getWorld().getChunkAt(chunk.getX() + radX, chunk.getZ() + radZ);
                if (!Board.getInstance().getFactionAt(new FLocation(current.getBlock(0, 100, 0))).equals(getFaction()))
                    continue;
                for (int blockX = 0; blockX < 16; blockX++) {
                    for (int blockZ = 0; blockZ < 16; blockZ++) {
                        for (int blockY = current.getWorld().getHighestBlockYAt(current.getBlock(blockX, 100, blockZ).getLocation()); blockY > 0; blockY--) {
                            Block block = current.getBlock(blockX, blockY, blockZ);
                            if (block.getType() == Material.AIR) continue;
                            if (block.getType() != Material.DISPENSER) continue;
                            Dispenser dispenser = (Dispenser) block.getState();
                            if (dispenser.getInventory().firstEmpty() != -1) dispensers.add(dispenser);
                        }
                    }
                }
            }
        }
        return dispensers;
    }

    public List<Dispenser> getDispensersAutofill(int radius, int amt) {
        List<Dispenser> dispensers = new ArrayList<>();
        for (int radX = -radius; radX <= radius; radX++) {
            for (int radZ = -radius; radZ <= radius; radZ++) {
                Chunk current = getWorld().getChunkAt(chunk.getX() + radX, chunk.getZ() + radZ);
                if (!Board.getInstance().getFactionAt(new FLocation(current.getBlock(0, 100, 0))).equals(getFaction()))
                    continue;
                for (int blockX = 0; blockX < 16; blockX++) {
                    for (int blockZ = 0; blockZ < 16; blockZ++) {
                        for (int blockY = current.getWorld().getHighestBlockYAt(current.getBlock(blockX, 100, blockZ).getLocation()); blockY > 0; blockY--) {
                            Block block = current.getBlock(blockX, blockY, blockZ);
                            if (block.getType() == Material.AIR) continue;
                            if (block.getType() != Material.DISPENSER) continue;
                            Dispenser dispenser = (Dispenser) block.getState();
                            if (dispenser.getInventory().firstEmpty() != -1) {
                                int tnt = 0;
                                for (int i = 0; i < dispenser.getInventory().getSize(); i++) {
                                    if (dispenser.getInventory().getItem(i) != null && dispenser.getInventory().getItem(i).getType() == Material.TNT) {
                                        tnt += dispenser.getInventory().getItem(i).getAmount();
                                    }
                                }
                                if (tnt <= amt) {
                                    dispensers.add(dispenser);
                                }
                            }
                        }
                    }
                }
            }
        }
        return dispensers;
    }

    public void assignAutofillTask(int amt, long delay) {
        cancelAutofillTask();
        setAutofillAmt(amt);
        setAutofillDelay(delay);
        this.task = Bukkit.getScheduler().runTaskTimer(Beacons.getInstance(), () -> {
            int[] data = fill(amt, Files.CONFIG.get().getInt("starting-radius"), true);
            if (data[0] == -1 || data[0] == -2) {
                this.task.cancel();
                GeneralMessage.AUTO_FILL_STOPPED.message(this.getFaction(), String.valueOf(this.getChunk().getX()), String.valueOf(this.getChunk().getZ()));
            } else {
                GeneralMessage.AUTO_FILL_UPDATE.message(this.getFaction(), String.valueOf(this.getChunk().getX()), String.valueOf(this.getChunk().getZ()), Beacons.formatNumber(amt));
            }
        }, 0L, delay);
    }

    public boolean cancelAutofillTask() {
        if (this.task == null) return false;
        setAutofillAmt(0);
        setAutofillDelay(0);
        this.task.cancel();
        this.task = null;
        return true;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void openGui(Player player) {
        this.gui.refresh();
        this.gui.open(player);
    }

    public Block getBlock() {
        return this.block;
    }

    public void clearHologram() {
        if (this.hologram != null) {
            this.hologram.delete();
        }
    }
}

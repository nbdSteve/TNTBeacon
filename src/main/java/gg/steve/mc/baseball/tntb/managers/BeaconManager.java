package gg.steve.mc.baseball.tntb.managers;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import gg.steve.mc.baseball.tntb.core.TnTBeacon;
import gg.steve.mc.baseball.tntb.framework.utils.LogUtil;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class BeaconManager {
    private static Map<Faction, List<TnTBeacon>> beacons;

    public static void loadBeacons() {
        beacons = new HashMap<>();
        for (String entry : Files.DATA.get().getKeys(false)) {
            UUID id = UUID.fromString(entry);
            TnTBeacon beacon = beaconFromFile(id);
            if (!beacons.containsKey(beacon.getFaction())) {
                beacons.put(beacon.getFaction(), new ArrayList<>());
            }
            beacons.get(beacon.getFaction()).add(beacon);
        }
    }

    public static TnTBeacon beaconFromFile(UUID id) {
        ConfigurationSection section = Files.DATA.get().getConfigurationSection(String.valueOf(id));
        return new TnTBeacon(id,
                section.getInt("x"),
                section.getInt("y"),
                section.getInt("z"),
                Bukkit.getWorld(section.getString("world")),
                Factions.getInstance().getFactionById(section.getString("faction")));
    }

    public static void shutdown() {
        if (beacons != null && !beacons.isEmpty()) {
            for (Faction faction : beacons.keySet()) {
                for (TnTBeacon beacon : beacons.get(faction)) {
                    beacon.saveToFile();
                }
            }
            beacons.clear();
        }
    }

    public static boolean addBeacon(Faction faction, Block block, UUID id) {
        if (beacons.containsKey(faction)) {
            for (TnTBeacon beacon : beacons.get(faction)) {
                if (beacon.getChunk().getX() == block.getChunk().getX() && beacon.getChunk().getZ() == block.getChunk().getZ()) {
                    return false;
                }
            }
        } else {
            beacons.put(faction, new ArrayList<>());
        }
        beacons.get(faction).add(new TnTBeacon(id, block.getX(), block.getY(), block.getZ(), block.getWorld(), faction));
        return true;
    }

    public static boolean removeBeacon(Faction faction, Chunk chunk) {
        if (!beacons.containsKey(faction)) return false;
        for (TnTBeacon beacon : beacons.get(faction)) {
            if (beacon.getChunk().getX() == chunk.getX() && beacon.getChunk().getZ() == chunk.getZ()) {
                beacon.purge();
                beacons.get(faction).remove(beacon);
                return true;
            }
        }
        return false;
    }

    public static TnTBeacon getBeacon(Faction faction, Chunk chunk) {
        if (!beacons.containsKey(faction)) return null;
        for (TnTBeacon beacon : beacons.get(faction)) {
            if (beacon.getChunk().getX() == chunk.getX() && beacon.getChunk().getZ() == chunk.getZ()) {
                return beacon;
            }
        }
        return null;
    }

    public static boolean isBeaconBlock(Block block) {
        for (Faction faction : beacons.keySet()) {
            for (TnTBeacon beacon : beacons.get(faction)) {
                if (beacon.getX() == block.getX() && beacon.getY() == block.getY() && beacon.getZ() == block.getZ()) return true;
            }
        }
        return false;
    }
}

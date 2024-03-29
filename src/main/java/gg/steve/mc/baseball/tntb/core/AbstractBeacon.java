package gg.steve.mc.baseball.tntb.core;

import com.massivecraft.factions.Faction;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public abstract class AbstractBeacon {
    private UUID id;
    private BeaconType type;
    private int x, y, z, autofillAmt;
    private long autofillDelay;
    private World world;
    private Faction faction;

    public AbstractBeacon(UUID id, BeaconType type, int x, int y, int z, World world, Faction faction, int autofillAmt, long autofillDelay) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.faction = faction;
        this.autofillAmt = autofillAmt;
        this.autofillDelay = autofillDelay;
    }

    public void saveToFile() {
        if (Files.DATA.get().getConfigurationSection(String.valueOf(this.id)) == null) {
            Files.DATA.get().createSection(String.valueOf(this.id));
        }
        ConfigurationSection section = Files.DATA.get().getConfigurationSection(String.valueOf(this.id));
        section.set("type", this.type.name());
        section.set("x", this.x);
        section.set("y", this.y);
        section.set("z", this.z);
        section.set("world", this.world.getName());
        section.set("faction", this.faction.getId());
        if (autofillAmt != 0 && autofillDelay != 0) {
            section.set("auto-fill.filling", true);
            section.set("auto-fill.amount", this.autofillAmt);
            section.set("auto-fill.delay", this.autofillDelay);
        } else {
            section.set("auto-fill.filling", false);
            section.set("auto-fill.amount", this.autofillAmt);
            section.set("auto-fill.delay", this.autofillDelay);
        }
        Files.DATA.save();
    }

    public void purge() {
        Files.DATA.get().set(String.valueOf(this.id), null);
        Files.DATA.save();
    }

    public UUID getId() {
        return id;
    }

    public BeaconType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public World getWorld() {
        return world;
    }

    public Faction getFaction() {
        return faction;
    }

    public int getAutofillAmt() {
        return autofillAmt;
    }

    public void setAutofillAmt(int autofillAmt) {
        this.autofillAmt = autofillAmt;
    }

    public long getAutofillDelay() {
        return autofillDelay;
    }

    public void setAutofillDelay(long autofillDelay) {
        this.autofillDelay = autofillDelay;
    }
}

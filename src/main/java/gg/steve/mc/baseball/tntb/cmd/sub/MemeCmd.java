package gg.steve.mc.baseball.tntb.cmd.sub;

import com.massivecraft.factions.FPlayers;
import gg.steve.mc.baseball.tntb.framework.cmd.MainCommand;
import gg.steve.mc.baseball.tntb.framework.cmd.SubCommand;
import gg.steve.mc.baseball.tntb.framework.permission.PermissionNode;
import gg.steve.mc.baseball.tntb.managers.BeaconManager;
import org.bukkit.command.CommandSender;

public class MemeCmd extends SubCommand {

    public MemeCmd(MainCommand parent) {
        super(parent, "test", 1, 1, false, PermissionNode.GIVE);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        BeaconManager.getBeacon(FPlayers.getInstance().getByPlayer(getPlayer(sender)).getFaction(), getPlayer(sender).getLocation().getChunk()).fill(-1, 1);
        return false;
    }
}

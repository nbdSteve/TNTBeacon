package gg.steve.mc.baseball.tntb.cmd.sub;

import gg.steve.mc.baseball.tntb.core.TnTBeaconItem;
import gg.steve.mc.baseball.tntb.framework.cmd.MainCommand;
import gg.steve.mc.baseball.tntb.framework.cmd.SubCommand;
import gg.steve.mc.baseball.tntb.framework.permission.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCmd extends SubCommand {

    public GiveCmd(MainCommand parent) {
        super(parent, "give", 2, 3, false, PermissionNode.GIVE);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        // give player amount
        Player target = Bukkit.getPlayer(args[1]);
        if (!target.isOnline()) {
            // message
            return false;
        }
        int amount = 1;
        if (args.length == 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                // message
                return false;
            }
        }
        for (int i = 0; i < amount; i++) {
            // send message
            TnTBeaconItem.give(target);
        }
        return true;
    }
}

package gg.steve.mc.baseball.tntb.cmd.sub;

import gg.steve.mc.baseball.tntb.Beacons;
import gg.steve.mc.baseball.tntb.core.TnTBeaconItem;
import gg.steve.mc.baseball.tntb.framework.cmd.MainCommand;
import gg.steve.mc.baseball.tntb.framework.cmd.SubCommand;
import gg.steve.mc.baseball.tntb.framework.message.DebugMessage;
import gg.steve.mc.baseball.tntb.framework.message.GeneralMessage;
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
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            DebugMessage.TARGET_NOT_ONLINE.message(sender);
            return false;
        }
        int amount = 1;
        if (args.length == 3) {
            try {
                amount = Integer.parseInt(args[2]);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                DebugMessage.INVALID_AMOUNT.message(sender);
                return false;
            }
        }
        for (int i = 0; i < amount; i++) {
            TnTBeaconItem.give(target);
        }
        Player player = null;
        if (sender instanceof Player && !target.getUniqueId().equals(((Player) sender).getUniqueId())) {
            player = (Player) sender;
        }
        GeneralMessage.GIVE_RECEIVER.message(target, Beacons.formatNumber(amount));
        if (!(sender instanceof Player) || player != null) {
            GeneralMessage.GIVE_GIVER.message(sender, Beacons.formatNumber(amount), target.getName());
        }
        return true;
    }
}

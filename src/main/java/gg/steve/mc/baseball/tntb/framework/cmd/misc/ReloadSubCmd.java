package gg.steve.mc.baseball.tntb.framework.cmd.misc;

import gg.steve.mc.baseball.tntb.Beacons;
import gg.steve.mc.baseball.tntb.framework.cmd.MainCommand;
import gg.steve.mc.baseball.tntb.framework.cmd.SubCommand;
import gg.steve.mc.baseball.tntb.framework.message.GeneralMessage;
import gg.steve.mc.baseball.tntb.framework.permission.PermissionNode;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadSubCmd extends SubCommand {

    public ReloadSubCmd(MainCommand parent) {
        super(parent, "reload", 1, 1, false, PermissionNode.RELOAD);
        addAlias("r");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Files.reload();
        Bukkit.getPluginManager().disablePlugin(Beacons.getInstance());
        Bukkit.getPluginManager().enablePlugin(Beacons.getInstance());
        GeneralMessage.RELOAD.message(sender);
        return true;
    }
}

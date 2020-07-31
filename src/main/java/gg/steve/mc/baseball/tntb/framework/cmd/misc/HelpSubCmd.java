package gg.steve.mc.baseball.tntb.framework.cmd.misc;

import gg.steve.mc.baseball.tntb.framework.message.GeneralMessage;
import gg.steve.mc.baseball.tntb.framework.permission.PermissionNode;
import gg.steve.mc.baseball.tntb.framework.cmd.MainCommand;
import gg.steve.mc.baseball.tntb.framework.cmd.SubCommand;
import org.bukkit.command.CommandSender;

public class HelpSubCmd extends SubCommand {

    public HelpSubCmd(MainCommand parent) {
        super(parent, "help", 0, 1, false, PermissionNode.HELP);
        addAlias("h");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        GeneralMessage.HELP.message(sender);
        return true;
    }
}

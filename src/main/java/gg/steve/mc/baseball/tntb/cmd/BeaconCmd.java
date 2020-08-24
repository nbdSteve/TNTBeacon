package gg.steve.mc.baseball.tntb.cmd;

import gg.steve.mc.baseball.tntb.cmd.sub.GiveCmd;
import gg.steve.mc.baseball.tntb.framework.cmd.MainCommand;
import gg.steve.mc.baseball.tntb.framework.cmd.misc.HelpSubCmd;
import gg.steve.mc.baseball.tntb.framework.cmd.misc.ReloadSubCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BeaconCmd extends MainCommand {

    public BeaconCmd() {
        addSubCommand(new HelpSubCmd(this), true);
        addSubCommand(new ReloadSubCmd(this), false);
        addSubCommand(new GiveCmd(this), false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return onCommand(sender, args);
    }
}

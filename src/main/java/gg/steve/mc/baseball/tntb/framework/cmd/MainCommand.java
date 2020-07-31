package gg.steve.mc.baseball.tntb.framework.cmd;

import gg.steve.mc.baseball.tntb.framework.message.DebugMessage;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public abstract class MainCommand implements CommandExecutor {
    private Map<SubCommand, Boolean> subs;

    public MainCommand() {
        subs = new HashMap<>();
    }

    public void addSubCommand(SubCommand sub, boolean noArgsCommand) {
        subs.put(sub, noArgsCommand);
    }

    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return this.noArgsCommand(sender, args);
        }
        for (SubCommand subCommand : subs.keySet()) {
            if (!subCommand.isExecutor(args[0])) continue;
            if (!subCommand.isValidCommand(sender, args)) return true;
            subCommand.onCommand(sender, args);
            return true;
        }
        DebugMessage.INVALID_COMMAND.message(sender);
        return true;
    }

    public boolean noArgsCommand(CommandSender sender, String[] args) {
        for (Map.Entry sub : subs.entrySet()) {
            if ((boolean) sub.getValue()) {
                SubCommand cmd = (SubCommand) sub.getKey();
                if (!cmd.isValidCommand(sender, args)) return true;
                return cmd.onCommand(sender, args);
            }
        }
        return false;
    }
}

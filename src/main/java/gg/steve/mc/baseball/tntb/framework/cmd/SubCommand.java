package gg.steve.mc.baseball.tntb.framework.cmd;

import gg.steve.mc.baseball.tntb.framework.message.DebugMessage;
import gg.steve.mc.baseball.tntb.framework.permission.PermissionNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SubCommand {
    private String name;
    private int minArgLength, maxArgLength;
    private boolean isPlayerOnly;
    private PermissionNode node;
    private List<String> aliases;

    public SubCommand(MainCommand parent, String name, int minArgLength, int maxArgLength, boolean isPlayerOnly, PermissionNode node) {
        parent.addSubCommand(this, minArgLength == 0);
        this.name = name;
        this.minArgLength = minArgLength;
        this.maxArgLength = maxArgLength;
        this.isPlayerOnly = isPlayerOnly;
        this.node = node;
        this.aliases = new ArrayList<>();
    }

    public Player getPlayer(CommandSender sender) {
        return (Player) sender;
    }

    public UUID getPlayerId(CommandSender sender) {
        return ((Player) sender).getUniqueId();
    }

    public boolean isValidCommand(CommandSender sender, String[] args) {
        if (isPlayerOnly && !(sender instanceof Player)) {
            // do message
            return false;
        }
        if (!node.hasPermission(sender)) {
            DebugMessage.INSUFFICIENT_PERMISSION.message(sender, node.get());
            return false;
        }
        if (!isArgLengthValid(args)) {
            DebugMessage.INCORRECT_ARGS.message(sender);
            return false;
        }
        return true;
    }

    public boolean isArgLengthValid(String[] args) {
        return args.length >= minArgLength && args.length <= maxArgLength;
    }

    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public boolean isExecutor(String arg) {
        return this.name.equalsIgnoreCase(arg) || this.aliases.contains(arg.toLowerCase());
    }

    public abstract boolean onCommand(CommandSender sender, String[] args);
}

package gg.steve.mc.baseball.tntb.framework.permission;

import gg.steve.mc.baseball.tntb.framework.yml.Files;
import org.bukkit.command.CommandSender;

public enum PermissionNode {
    // cmd
    RELOAD("command.reload"),
    GIVE("command.give"),
    HELP("command.help");

    private String path;

    PermissionNode(String path) {
        this.path = path;
    }

    public String get() {
        return Files.PERMISSIONS.get().getString(this.path);
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(get());
    }

    public static boolean isPurchasePerms() {
        return Files.PERMISSIONS.get().getBoolean("purchase.enabled");
    }
}

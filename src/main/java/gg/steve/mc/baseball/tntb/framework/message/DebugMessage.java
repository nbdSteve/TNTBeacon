package gg.steve.mc.baseball.tntb.framework.message;

import gg.steve.mc.baseball.tntb.framework.yml.Files;
import gg.steve.mc.baseball.tntb.framework.utils.ColorUtil;
import gg.steve.mc.baseball.tntb.framework.utils.actionbarapi.ActionBarAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum DebugMessage {
    TARGET_NOT_ONLINE("target-not-online"),
    INVALID_AMOUNT("invalid-amount"),
    ONLY_IN_CLAIMS("only-in-claims"),
    BEACON_ALREADY_ACTIVE("beacon-already-active"),
    OWNER_BY_OTHER_FACTION("owned-by-other-faction"),
    INVALID_FILL_AMOUNT("invalid-fill-amount"),
    INSUFFICIENT_TNT("insufficient-tnt"),
    LARGER_AMOUNT_REQUIRED("larger-amount-required"),
    LARGER_AMOUNT_REQUIRED_MAX("larger-amount-required-max"),
    INVALID_MINUTES("invalid-minutes"),
    INVALID_SECONDS("invalid-seconds"),
    INVALID_TIME("invalid-time"),
    INVALID_FORMAT("invalid-format"),
    INVALID_COMMAND("invalid-command"),
    INCORRECT_ARGS("incorrect-args"),
    INSUFFICIENT_PERMISSION("insufficient-permission", "{node}");

    private String path;
    private boolean actionBar;
    private List<String> placeholders;

    DebugMessage(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
        this.actionBar = Files.MESSAGES.get().getBoolean(this.path + ".action-bar");
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        if (this.actionBar) {
            for (String line : Files.DEBUG.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                ActionBarAPI.sendActionBar(receiver, ColorUtil.colorize(line));
            }
        } else {
            for (String line : Files.DEBUG.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                receiver.sendMessage(ColorUtil.colorize(line));
            }
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        if (this.actionBar && receiver instanceof Player) {
            for (String line : Files.DEBUG.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replaceAll(this.placeholders.get(i), data.get(i));
                }
                ActionBarAPI.sendActionBar((Player) receiver, ColorUtil.colorize(line));
            }
        } else {
            for (String line : Files.DEBUG.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                receiver.sendMessage(ColorUtil.colorize(line));
            }
        }
    }
}

package gg.steve.mc.baseball.tntb.framework.message;

import com.massivecraft.factions.Faction;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import gg.steve.mc.baseball.tntb.framework.utils.ColorUtil;
import gg.steve.mc.baseball.tntb.framework.utils.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public enum GeneralMessage {
    GIVE_RECEIVER("give-receiver", "{amount}"),
    GIVE_GIVER("give-giver", "{amount}", "{target}"),
    SUCCESSFUL_FILL("successful-fill", "{tnt-total}", "{dispenser-count}", "{tnt-each}"),
    ACTION_CANCELLED("action-cancelled"),
    SUCCESSFUL_EXTRACT("successful-extract", "{tnt-total}", "{dispenser-count}"),
    CHAT_INITIALISE("chat-initialise"),
    AUTO_FILL_CANCEL("auto-fill-cancel"),
    AUTO_FILL_CHAT_INITIALISE("auto-fill-chat-initialise"),
    AUTO_FILL_SUCCESS("auto-fill-success", "{tnt}", "{minutes}", "{seconds}"),
    AUTO_FILL_UPDATE("auto-fill-update", "{x}", "{z}", "{tnt}"),
    AUTO_FILL_STOPPED("auto-fill-stopped", "{x}", "{z}"),
    RELOAD("reload"),
    HELP("help");

    private String path;
    private boolean actionBar;
    private List<String> placeholders;

    GeneralMessage(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
        this.actionBar = Files.MESSAGES.get().getBoolean(this.path + ".action-bar");
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        if (this.actionBar) {
            for (String line : Files.MESSAGES.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                ActionBarAPI.sendActionBar(receiver, ColorUtil.colorize(line));
            }
        } else {
            for (String line : Files.MESSAGES.get().getStringList(this.path + ".text")) {
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
            for (String line : Files.MESSAGES.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                ActionBarAPI.sendActionBar((Player) receiver, ColorUtil.colorize(line));
            }
        } else {
            for (String line : Files.MESSAGES.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                receiver.sendMessage(ColorUtil.colorize(line));
            }
        }
    }

    public void message(Faction faction, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (Player receiver : faction.getOnlinePlayers()) {
            if (this.actionBar) {
                for (String line : Files.MESSAGES.get().getStringList(this.path + ".text")) {
                    for (int i = 0; i < this.placeholders.size(); i++) {
                        line = line.replace(this.placeholders.get(i), data.get(i));
                    }
                    ActionBarAPI.sendActionBar(receiver, ColorUtil.colorize(line));
                }
            } else {
                for (String line : Files.MESSAGES.get().getStringList(this.path + ".text")) {
                    for (int i = 0; i < this.placeholders.size(); i++) {
                        line = line.replace(this.placeholders.get(i), data.get(i));
                    }
                    receiver.sendMessage(ColorUtil.colorize(line));
                }
            }
        }
    }

    public void broadcast(JavaPlugin instance, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        if (this.actionBar) {
            for (String line : Files.MESSAGES.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                ActionBarAPI.sendActionBarToAllPlayers(ColorUtil.colorize(line), instance);
            }
        } else {
            for (String line : Files.MESSAGES.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                Bukkit.broadcastMessage(ColorUtil.colorize(line));
            }
        }
    }

    public static void doMessage(Player receiver, List<String> lines) {
        for (String line : lines) {
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }
}
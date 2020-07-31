package gg.steve.mc.baseball.tntb.framework;

import gg.steve.mc.baseball.tntb.cmd.BeaconCmd;
import gg.steve.mc.baseball.tntb.core.TnTBeaconItem;
import gg.steve.mc.baseball.tntb.framework.yml.Files;
import gg.steve.mc.baseball.tntb.framework.yml.utils.FileManagerUtil;
import gg.steve.mc.baseball.tntb.listener.InteractListener;
import gg.steve.mc.baseball.tntb.listener.PlacementListener;
import gg.steve.mc.baseball.tntb.managers.BeaconManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class that handles setting up the plugin on start
 */
public class SetupManager {
    private static FileManagerUtil fileManager;

    private SetupManager() throws IllegalAccessException {
        throw new IllegalAccessException("Manager class cannot be instantiated.");
    }

    /**
     * Loads the files into the file manager
     */
    public static void setupFiles(FileManagerUtil fm) {
        fileManager = fm;
        Files.CONFIG.load(fm);
        Files.PERMISSIONS.load(fm);
        Files.DEBUG.load(fm);
        Files.MESSAGES.load(fm);
        Files.DATA.load(fm);
    }

    public static void registerCommands(JavaPlugin instance) {
        instance.getCommand("beacon").setExecutor(new BeaconCmd());
    }

    /**
     * Register all of the events for the plugin
     *
     * @param instance Plugin, the main plugin instance
     */
    public static void registerEvents(JavaPlugin instance) {
        PluginManager pm = instance.getServer().getPluginManager();
        pm.registerEvents(new PlacementListener(), instance);
        pm.registerEvents(new InteractListener(), instance);
    }

    public static void registerEvent(JavaPlugin instance, Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }

    public static void loadPluginCache() {
        AbstractManager.initialiseManagers();
        BeaconManager.loadBeacons();
        TnTBeaconItem.init();
    }

    public static void shutdownPluginCache() {
        AbstractManager.shutdownManagers();
        BeaconManager.shutdown();
    }

    public static FileManagerUtil getFileManager() {
        return fileManager;
    }
}

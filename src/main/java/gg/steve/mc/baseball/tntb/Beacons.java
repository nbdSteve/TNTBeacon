package gg.steve.mc.baseball.tntb;

import gg.steve.mc.baseball.tntb.framework.SetupManager;
import gg.steve.mc.baseball.tntb.framework.utils.LogUtil;
import gg.steve.mc.baseball.tntb.framework.yml.utils.FileManagerUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class Beacons extends JavaPlugin {
    private static Beacons instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        LogUtil.setInstance(instance, true);
        SetupManager.setupFiles(new FileManagerUtil(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
        SetupManager.loadPluginCache();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SetupManager.shutdownPluginCache();
    }

    public static Beacons getInstance() {
        return instance;
    }
}

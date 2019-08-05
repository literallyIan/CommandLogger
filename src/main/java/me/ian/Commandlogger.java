package me.ian;

import com.google.common.collect.Maps;
import me.ian.events.CommandPreprocess;
import me.ian.utils.Updater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.logging.Level;

public final class Commandlogger extends JavaPlugin {

    private static Commandlogger instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        log("Plugin running on version " + getDescription().getVersion());
        log("Developed with ❤  by Wg0.");

        processConfigFile();

        getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                checkUpdate();
            }
        }, 20L);

        registerEvents(Bukkit.getServer().getPluginManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }

    private void registerEvents(PluginManager pluginManager) {
        pluginManager.registerEvents(new CommandPreprocess(), this);
    }

    public void log(String message) {
        getLogger().info(message);
    }

    public void log(Level level, String message) {
        getLogger().log(level, message);
    }

    public static Commandlogger getInstance() {
        return instance;
    }

    public void processConfigFile() {

        final Map<String, Object> defaultParams = Maps.newHashMap();
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);

        defaultParams.put("INGAME_MESSAGE", "%prefix% %player% &7performed &c%command%");
        defaultParams.put("LOG_MESSAGE", "%player% used command %command%");
        defaultParams.put("PREFIX", "&8[&6CommandLogger&8] &7");

        for (final Map.Entry<String, Object> e : defaultParams.entrySet())
            if (!config.contains(e.getKey()))
                config.set(e.getKey(), e.getValue());

        this.saveConfig();
    }

    private void checkUpdate() {
        log("[UPDATER] Checking for updates...");
        Updater updater = new Updater(this, 69845, false);
        Updater.UpdateResult updateResult = updater.getResult();
        switch (updateResult) {
            case FAIL_SPIGOT:
                log(Level.WARNING, "[UPDATER] Couldn't reach spigot.");
                break;
            case NO_UPDATE:
                log("[UPDATER] The plugin is up to date.");
                break;
            case UPDATE_AVAILABLE:
                log("============================================");
                log("An update is available:");
                log("Download at: https://www.spigotmc.org/resources/commandlogger.69845/");
                log("============================================");
                break;
            case FAIL_NOVERSION:
                log(Level.SEVERE, "[Updater] No version detected.");
                break;
            default:
                log(updateResult.toString());
                break;
        }
    }
}

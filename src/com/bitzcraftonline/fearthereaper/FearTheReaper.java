package com.bitzcraftonline.fearthereaper;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import com.bitzcraftonline.fearthereaper.commands.ReaperCommands;
import com.bitzcraftonline.fearthereaper.spawnpoint.Spawn;
import com.bitzcraftonline.fearthereaper.spawnpoint.SpawnPoint;

public class FearTheReaper extends JavaPlugin {

    public static Logger log = Logger.getLogger("Minecraft");
    public static File pointsDirectory;
    public static FileConfiguration config;
    public static HashMap<String, Spawn> SpawnPointList = new HashMap<String, Spawn>();    
    public static ReaperMarkers markers = null;
    
    public static HashMap<String, Spawn> getSpawnList() {
        return SpawnPointList;
    }

    public void onDisable() {
        saveConfig();
        log.info("[" + getDescription().getName() + "] v" + getDescription().getVersion() + " unloaded successfully!");
    }

    public void onEnable() {
        pointsDirectory = new File(getDataFolder(), "points");
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        SpawnPoint.loadAllPoints();

        getServer().getPluginManager().registerEvents(new ReaperPlayerListener(), this);

        log.info("[" + getDescription().getName() + "] v" + getDescription().getVersion() + " loaded successfully!");

        getCommand("graveyard").setExecutor(new ReaperCommands(this));

        final Plugin dm = getServer().getPluginManager().getPlugin("dynmap");
        if (dm != null) {
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new ReaperMarkerSetup(this, (DynmapAPI) dm), 5);
        }
    }

    public static void reloadConfig(FearTheReaper plugin) {
        System.out.println("[FearTheReaper] - Reloading config.");
        plugin.reloadConfig();
        FearTheReaper.config = plugin.getConfig();
    }

    public static boolean isBedsEnabled() {
        return FearTheReaper.config.getBoolean("enable-beds", false);
    }
    
    public class ReaperMarkerSetup implements Runnable {

        private final DynmapAPI dm;
        private final FearTheReaper plugin;
        
        public ReaperMarkerSetup(FearTheReaper plugin, DynmapAPI dm) {
            this.dm = dm;
            this.plugin = plugin;
        }
        
        @Override
        public void run() {
            markers = new ReaperMarkers(plugin, dm);
        }
    }
}

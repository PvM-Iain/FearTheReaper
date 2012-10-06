package com.bitzcraftonline.fearthereaper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.bitzcraftonline.fearthereaper.spawnpoint.Spawn;
import com.bitzcraftonline.fearthereaper.spawnpoint.SpawnPoint;
import com.bitzcraftonline.fearthereaper.utils.GraveyardUtils;

public class ReaperPlayerListener implements Listener {

    public ReaperPlayerListener() {   }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (GraveyardUtils.useBed(event.getPlayer()) && event.isBedSpawn()) {
            return;
        }

        if (event.getPlayer().hasPermission("graveyard.closest")) {
            if (SpawnPoint.getAllowedList(event.getPlayer()).size() != 0) {
                Spawn closest = SpawnPoint.getClosestAllowed(event.getPlayer());
                if (closest == null) {
                    return;
                } else if (!closest.getSpawnMessage().equalsIgnoreCase("none")) {
                    event.getPlayer().sendMessage(GraveyardUtils.replaceColors(closest.getSpawnMessage()));
                }
                event.setRespawnLocation(closest.getLocation());
            }
        }
    }
}
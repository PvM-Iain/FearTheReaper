package com.bitzcraftonline.fearthereaper;

import java.io.InputStream;
import java.util.Map.Entry;

import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import com.bitzcraftonline.fearthereaper.spawnpoint.Spawn;

public class ReaperMarkers {

    private DynmapAPI api;
    private MarkerAPI mApi;
    private static MarkerSet set;
    private static MarkerIcon icon;

    public ReaperMarkers(FearTheReaper plugin, DynmapAPI dm) {
        // Setup the API
        api = dm;
        mApi = api.getMarkerAPI();
        if (mApi == null) {
            return;
        }
        icon = mApi.getMarkerIcon("tomb");
        if (icon == null) {
            InputStream in = getClass().getResourceAsStream("/tomb.png");
            icon = mApi.createMarkerIcon("tomb", "Graveyards", in);
        }

        set = mApi.getMarkerSet("graveyard.markerset");
        if (set == null) {
            set = mApi.createMarkerSet("graveyard.markerset", "Graveyards", null, true);
            set.setLayerPriority(10);
            set.setHideByDefault(false);
            set.setMinZoom(0);
        }

        for (Entry<String, Spawn> entry : FearTheReaper.getSpawnList().entrySet()) {
            Marker m = set.findMarker(entry.getKey());
            Spawn s = entry.getValue();
            if (m != null) {
                if (m.getX() != s.getX() || m.getY() != s.getY() || m.getZ() != s.getZ()) {
                    m.setLocation(s.getWorldName(), s.getX(), s.getY(), s.getZ());
                }
            }

            set.createMarker(entry.getKey(), entry.getKey(), s.getWorldName(), s.getX(), s.getY(), s.getZ(), icon, true);
        }
    }

    public static void updateMarker(Spawn point) {
        Marker m = set.findMarker(point.getName());
        if (m != null) {
            if (m.getX() != point.getX() || m.getY() != point.getY() || m.getZ() != point.getZ()) {
                m.setLocation(point.getWorldName(), point.getX(), point.getY(), point.getZ());
            }
        }
        set.createMarker(point.getName(), point.getName(), point.getWorldName(), point.getX(), point.getY(), point.getZ(), icon, true);
    }

    public static void deleteMarker(Spawn point) {
        Marker m = set.findMarker(point.getName());
        if (m != null) {
            m.deleteMarker();
        }
    }
}

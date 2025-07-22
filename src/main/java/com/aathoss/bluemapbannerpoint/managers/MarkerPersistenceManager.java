package com.aathoss.bluemapbannerpoint.managers;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.managers.data.BannerMarkerData;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;

import java.io.*;
import java.util.*;

/**
 * Manager spécialisé dans la persistance des marqueurs
 */
public class MarkerPersistenceManager {

    private final BlueMapBannerPoint plugin;
    private final String MARKERS_FILE = "banners-markers.yml";
    private final File markersFile;
    private final Yaml yaml;

    public MarkerPersistenceManager(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
        this.markersFile = new File(plugin.getDataFolder(), MARKERS_FILE);

        // Configuration YAML
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        this.yaml = new Yaml(options);

        // S'assurer que le dossier de données existe
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    }

    /**
     * Sauvegarde tous les marqueurs dans le fichier YAML
     */
    public void saveMarkers(Map<String, BannerMarkerData> bannerMarkers) {
        try {
            // Convertir les données en format sérialisable sans tags personnalisés
            Map<String, Object> dataToSave = new HashMap<>();
            Map<String, Map<String, Object>> markersToSave = new HashMap<>();

            // Convertir chaque BannerMarkerData en Map simple
            for (Map.Entry<String, BannerMarkerData> entry : bannerMarkers.entrySet()) {
                BannerMarkerData marker = entry.getValue();
                Map<String, Object> markerMap = new HashMap<>();

                markerMap.put("markerId", marker.markerId);
                markerMap.put("playerUuid", marker.playerUuid);
                markerMap.put("playerName", marker.playerName);
                markerMap.put("world", marker.world);
                markerMap.put("x", marker.x);
                markerMap.put("y", marker.y);
                markerMap.put("z", marker.z);
                markerMap.put("markerName", marker.markerName);
                markerMap.put("iconName", marker.iconName);
                markerMap.put("timestamp", marker.timestamp);

                markersToSave.put(entry.getKey(), markerMap);
            }

            dataToSave.put("markers", markersToSave);
            dataToSave.put("last-save", System.currentTimeMillis());

            // Sauvegarder dans le fichier
            try (FileWriter writer = new FileWriter(markersFile)) {
                yaml.dump(dataToSave, writer);
            }

            if (plugin.getConfigManager().isDebugEnabled()) {
                plugin.getLogger().info(Messages.get("debug.markers_saved", new Object[]{bannerMarkers.size()}));
            }

        } catch (IOException e) {
            plugin.getLogger().severe(Messages.get("system.save_error", new Object[]{e.getMessage()}));
            if (plugin.getConfigManager().isDebugEnabled()) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Charge tous les marqueurs depuis le fichier YAML
     */
    @SuppressWarnings("unchecked")
    public Map<String, BannerMarkerData> loadMarkers() {
        Map<String, BannerMarkerData> loadedMarkers = new HashMap<>();

        if (!markersFile.exists()) {
            if (plugin.getConfigManager().isDebugEnabled()) {
                plugin.getLogger().info(Messages.get("debug.no_markers_file"));
            }
            return loadedMarkers;
        }

        try (FileReader reader = new FileReader(markersFile)) {
            Map<String, Object> data = yaml.load(reader);

            if (data != null && data.containsKey("markers")) {
                Map<String, Object> markersData = (Map<String, Object>) data.get("markers");

                for (Map.Entry<String, Object> entry : markersData.entrySet()) {
                    try {
                        Map<String, Object> markerData = (Map<String, Object>) entry.getValue();
                        BannerMarkerData marker = createMarkerFromData(markerData);
                        if (marker != null) {
                            loadedMarkers.put(entry.getKey(), marker);
                        }
                    } catch (Exception e) {
                        plugin.getLogger().warning(Messages.get("system.marker_load_error", new Object[]{entry.getKey(), e.getMessage()}));
                    }
                }

                if (plugin.getConfigManager().isDebugEnabled()) {
                    plugin.getLogger().info(Messages.get("debug.markers_loaded", new Object[]{loadedMarkers.size()}));
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe(Messages.get("system.load_error", new Object[]{e.getMessage()}));
            if (plugin.getConfigManager().isDebugEnabled()) {
                e.printStackTrace();
            }
        }

        return loadedMarkers;
    }

    /**
     * Crée un objet BannerMarkerData à partir des données YAML
     */
    private BannerMarkerData createMarkerFromData(Map<String, Object> data) {
        try {
            BannerMarkerData marker = new BannerMarkerData();

            marker.markerId = (String) data.get("markerId");
            marker.playerUuid = (String) data.get("playerUuid");
            marker.playerName = (String) data.get("playerName");
            marker.world = (String) data.get("world");
            marker.x = (Integer) data.get("x");
            marker.y = (Integer) data.get("y");
            marker.z = (Integer) data.get("z");
            marker.markerName = (String) data.get("markerName");
            marker.iconName = (String) data.get("iconName");
            marker.timestamp = (Long) data.get("timestamp");

            return marker;
        } catch (Exception e) {
            plugin.getLogger().warning(Messages.get("system.marker_data_error", new Object[]{e.getMessage()}));
            return null;
        }
    }

    /**
     * Supprime le fichier de marqueurs
     */
    public void deleteMarkersFile() {
        if (markersFile.exists()) {
            if (markersFile.delete()) {
                plugin.getLogger().info(Messages.get("system.markers_file_deleted"));
            } else {
                plugin.getLogger().warning(Messages.get("system.markers_file_delete_error"));
            }
        }
    }
}

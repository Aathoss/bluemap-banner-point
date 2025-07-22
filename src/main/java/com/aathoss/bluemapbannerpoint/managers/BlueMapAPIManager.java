package com.aathoss.bluemapbannerpoint.managers;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.managers.data.BannerMarkerData;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.POIMarker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import com.flowpowered.math.vector.Vector3d;

import java.util.Date;
import java.util.Optional;

/**
 * Manager spécialisé dans l'interaction avec l'API BlueMap
 */
public class BlueMapAPIManager {

    private final BlueMapBannerPoint plugin;
    private final MarkerImageManager markerImageManager;
    private static final String MARKER_SET_ID = "banners";

    public BlueMapAPIManager(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
        this.markerImageManager = new MarkerImageManager(plugin);

        // Initialiser les callbacks BlueMap
        initializeBlueMapCallbacks();
    }

    /**
     * Initialise les callbacks BlueMap
     */
    private void initializeBlueMapCallbacks() {
        BlueMapAPI.onEnable(api -> {
            markerImageManager.loadImagesToBlueMap(api);
            plugin.getLogger().info(Messages.get("system.bluemap_enabled"));

            // Ajouter tous les marqueurs existants à BlueMap maintenant qu'il est disponible
            plugin.getBlueMapManager().addAllMarkersToBlueMap();
        });

        BlueMapAPI.onDisable(api -> {
            plugin.getLogger().info(Messages.get("system.bluemap_disabled"));
        });
    }

    /**
     * Ajoute un marqueur à BlueMap
     */
    public void addMarkerToBlueMap(BannerMarkerData data) {
        try {
            Optional<BlueMapAPI> apiOptional = BlueMapAPI.getInstance();
            if (apiOptional.isEmpty()) {
                return;
            }

            BlueMapAPI api = apiOptional.get();

            // Trouver la carte correspondante au monde
            Optional<BlueMapMap> mapOptional = api.getMaps().stream()
                    .filter(map -> map.getId().equals(data.world))
                    .findFirst();

            if (mapOptional.isEmpty()) {
                plugin.getLogger().warning(Messages.get("system.map_not_found", new Object[]{data.world}));
                return;
            }

            BlueMapMap map = mapOptional.get();
            MarkerSet markerSet = getOrCreateMarkerSet(map);

            // Déterminer le label du marqueur
            String labelValue = Messages.get("config.markers.label_name", new Object[]{data.playerName});
            if (data.markerName != null && !data.markerName.isEmpty()) {
                labelValue = data.playerName + " - " + data.markerName;
            }

            // Convert int to double
            double x = data.x + 0.5;
            double y = data.y + 1;
            double z = data.z + 0.5;

            // Correction de la position du marqueur
            Vector3d position = new Vector3d(x, y, z);

            // Créer le marqueur POI
            POIMarker marker = POIMarker.builder()
                    .label(labelValue)
                    .detail(generateMarkerDetails(data))
                    .icon(markerImageManager.getImagePath(data.iconName), 32/2, 32/2)
                    .position(position)
                    .build();

            // Ajouter le marqueur à l'ensemble
            markerSet.put(data.markerId, marker);

            if (plugin.getConfigManager().isDebugEnabled()) {
                plugin.getLogger().info(Messages.get("debug.marker_added_to_bluemap", new Object[]{
                    data.markerId, data.world, data.x, data.y, data.z
                }));
            }

        } catch (Exception e) {
            plugin.getLogger().severe(Messages.get("system.bluemap_marker_error", new Object[]{e.getMessage()}));
            if (plugin.getConfigManager().isDebugEnabled()) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Supprime un marqueur de BlueMap
     */
    public void removeMarkerFromBlueMap(String markerId, String worldName) {
        try {
            Optional<BlueMapAPI> apiOptional = BlueMapAPI.getInstance();
            if (apiOptional.isEmpty()) {
                return;
            }

            BlueMapAPI api = apiOptional.get();

            // Trouver la carte correspondante au monde
            Optional<BlueMapMap> mapOptional = api.getMaps().stream()
                    .filter(map -> map.getId().equals(worldName))
                    .findFirst();

            if (mapOptional.isPresent()) {
                BlueMapMap map = mapOptional.get();
                MarkerSet markerSet = map.getMarkerSets().get(MARKER_SET_ID);

                if (markerSet != null) {
                    markerSet.remove(markerId);

                    if (plugin.getConfigManager().isDebugEnabled()) {
                        plugin.getLogger().info(Messages.get("debug.marker_removed_from_bluemap", new Object[]{markerId, worldName}));
                    }
                }
            }

        } catch (Exception e) {
            plugin.getLogger().severe(Messages.get("system.bluemap_remove_error", new Object[]{e.getMessage()}));
            if (plugin.getConfigManager().isDebugEnabled()) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Récupère ou crée l'ensemble de marqueurs pour une carte
     */
    private MarkerSet getOrCreateMarkerSet(BlueMapMap map) {
        MarkerSet markerSet = map.getMarkerSets().get(MARKER_SET_ID);

        if (markerSet == null) {
            markerSet = MarkerSet.builder()
                    .label(Messages.get("config.markers.category_name"))
                    .defaultHidden(false)
                    .build();

            map.getMarkerSets().put(MARKER_SET_ID, markerSet);
        }

        return markerSet;
    }

    /**
     * Génère les détails d'un marqueur
     */
    private String generateMarkerDetails(BannerMarkerData data) {
        String markerName = data.markerName;
        String date = new Date(data.timestamp).toString();
        String key = "config.markers.detail_with_name";

        if (plugin.getConfigManager().getLanguage().equals("fr")) {
            date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(data.timestamp)).toString();
        } else {
            date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(data.timestamp));
        }

        if (markerName == null || markerName.isEmpty()) {
            key = "config.markers.detail_simple";
        }

        return Messages.get(key, new Object[]{
            data.playerName,
            markerName,
            data.iconName,
            data.x, data.y, data.z,
            data.world,
            date
        });
    }

    /**
     * Vérifie si BlueMap est disponible
     */
    public boolean isBlueMapAvailable() {
        return BlueMapAPI.getInstance() != null;
    }

    /**
     * Nettoie tous les marqueurs d'une carte
     */
    public void clearMarkersFromMap(String worldName) {
        try {
            Optional<BlueMapAPI> apiOptional = BlueMapAPI.getInstance();
            if (apiOptional.isEmpty()) {
                return;
            }

            BlueMapAPI api = apiOptional.get();

            Optional<BlueMapMap> mapOptional = api.getMaps().stream()
                    .filter(map -> map.getId().equals(worldName))
                    .findFirst();

            if (mapOptional.isPresent()) {
                BlueMapMap map = mapOptional.get();
                map.getMarkerSets().remove(MARKER_SET_ID);

                if (plugin.getConfigManager().isDebugEnabled()) {
                    plugin.getLogger().info(Messages.get("debug.markers_cleared_from_map", new Object[]{worldName}));
                }
            }

        } catch (Exception e) {
            plugin.getLogger().severe(Messages.get("system.bluemap_clear_error", new Object[]{e.getMessage()}));
            if (plugin.getConfigManager().isDebugEnabled()) {
                e.printStackTrace();
            }
        }
    }
}

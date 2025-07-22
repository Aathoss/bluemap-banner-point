package com.aathoss.bluemapbannerpoint.managers;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.managers.data.BannerMarkerData;
import com.aathoss.bluemapbannerpoint.managers.interfaces.MarkerManager;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * BlueMapManager refactorisé utilisant des managers spécialisés
 */
public class BlueMapManager implements MarkerManager {

    private final BlueMapBannerPoint plugin;
    private final ConfigManager configManager;
    private final BlueMapAPIManager blueMapAPIManager;
    private final MarkerPersistenceManager persistenceManager;
    private final MarkerIndexManager indexManager;
    private final Map<String, BannerMarkerData> bannerMarkers; // locationKey -> BannerMarkerData

    public BlueMapManager(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.blueMapAPIManager = new BlueMapAPIManager(plugin);
        this.persistenceManager = new MarkerPersistenceManager(plugin);
        this.indexManager = new MarkerIndexManager(plugin);
        this.bannerMarkers = new HashMap<>();

        // Charger les marqueurs sauvegardés
        loadAllMarkers();
    }

    @Override
    public void addBannerMarker(Location location, Player player, String bannerName, String iconName) {
        try {
            String locationKey = getLocationKey(location);
            String markerName = generateMarkerName(bannerName);
            String markerId = "banner_" + player.getUniqueId() + "_" + System.currentTimeMillis();

            BannerMarkerData data = new BannerMarkerData(markerId, player.getUniqueId().toString(), player.getName(),
                    location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                    markerName, iconName, System.currentTimeMillis());

            bannerMarkers.put(locationKey, data);

            // Mettre à jour les index
            indexManager.updateIndexes(data, true);

            // Ajouter à BlueMap si disponible
            if (blueMapAPIManager.isBlueMapAvailable()) {
                blueMapAPIManager.addMarkerToBlueMap(data);
            }

            // Sauvegarder
            persistenceManager.saveMarkers(bannerMarkers);

            if (configManager.isDebugEnabled()) {
                plugin.getLogger().info(Messages.get("debug.marker_added", new Object[]{player.getName(), locationKey, iconName}));
            }

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = e.getClass().getSimpleName();
            }
            plugin.getLogger().severe(Messages.get("system.marker_add_error", new Object[]{errorMessage}));
            if (configManager.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeBannerMarker(Location location) {
        String locationKey = getLocationKey(location);
        BannerMarkerData data = bannerMarkers.get(locationKey);

        if (data != null) {
            // Supprimer de BlueMap si disponible
            if (blueMapAPIManager.isBlueMapAvailable()) {
                blueMapAPIManager.removeMarkerFromBlueMap(data.markerId, data.world);
            }

            // Mettre à jour les index
            indexManager.updateIndexes(data, false);

            // Supprimer de la collection principale
            bannerMarkers.remove(locationKey);

            // Sauvegarder
            persistenceManager.saveMarkers(bannerMarkers);

            if (configManager.isDebugEnabled()) {
                plugin.getLogger().info(Messages.get("debug.marker_removed", new Object[]{locationKey}));
            }
        }
    }

    @Override
    public void updateBannerMarkerName(Location location, Player player, String newBannerName, String iconName) {
        String locationKey = getLocationKey(location);
        BannerMarkerData data = bannerMarkers.get(locationKey);

        if (data != null) {
            // Mettre à jour les données
            data.markerName = generateMarkerName(newBannerName);
            data.iconName = iconName;

            // Mettre à jour dans BlueMap si disponible
            if (blueMapAPIManager.isBlueMapAvailable()) {
                blueMapAPIManager.removeMarkerFromBlueMap(data.markerId, data.world);
                blueMapAPIManager.addMarkerToBlueMap(data);
            }

            // Sauvegarder
            persistenceManager.saveMarkers(bannerMarkers);

            if (configManager.isDebugEnabled()) {
                plugin.getLogger().info(Messages.get("debug.marker_updated", new Object[]{locationKey, newBannerName}));
            }
        }
    }

    @Override
    public boolean hasMarkerAt(Location location) {
        return bannerMarkers.containsKey(getLocationKey(location));
    }

    @Override
    public BannerMarkerData getMarkerAt(Location location) {
        return bannerMarkers.get(getLocationKey(location));
    }

    @Override
    public List<BannerMarkerData> getMarkersByPlayer(String playerName) {
        return indexManager.getMarkersByPlayer(playerName);
    }

    @Override
    public List<BannerMarkerData> getMarkersByWorld(String worldName) {
        return indexManager.getMarkersByWorld(worldName);
    }

    @Override
    public int getTotalMarkers() {
        return bannerMarkers.size();
    }

    @Override
    public List<BannerMarkerData> getAllMarkers() {
        return new ArrayList<>(bannerMarkers.values());
    }

    @Override
    public Map<String, List<BannerMarkerData>> getMarkersByWorld() {
        return indexManager.getMarkersByWorld();
    }

    @Override
    public Map<String, List<BannerMarkerData>> getMarkersByPlayer() {
        return indexManager.getMarkersByPlayer();
    }

    @Override
    public void saveMarkers() {
        persistenceManager.saveMarkers(bannerMarkers);
    }

    @Override
    public void reloadMarkers() {
        // Vider les collections
        bannerMarkers.clear();
        indexManager.rebuildIndexes(bannerMarkers);

        // Recharger depuis le fichier
        loadAllMarkers();

        if (configManager.isDebugEnabled()) {
            plugin.getLogger().info(Messages.get("debug.markers_reloaded", new Object[]{bannerMarkers.size()}));
        }
    }

    @Override
    public void clearAllMarkers() {
        // Supprimer de BlueMap
        for (BannerMarkerData data : bannerMarkers.values()) {
            blueMapAPIManager.removeMarkerFromBlueMap(data.markerId, data.world);
        }

        // Vider les collections
        bannerMarkers.clear();
        indexManager.rebuildIndexes(bannerMarkers);

        // Supprimer le fichier de sauvegarde
        persistenceManager.deleteMarkersFile();

        if (configManager.isDebugEnabled()) {
            plugin.getLogger().info(Messages.get("debug.all_markers_cleared"));
        }
    }

    @Override
    public void cleanup() {
        // Sauvegarder avant de nettoyer
        persistenceManager.saveMarkers(bannerMarkers);

        // Nettoyer les index
        indexManager.cleanupEmptyIndexes();

        if (configManager.isDebugEnabled()) {
            plugin.getLogger().info(Messages.get("debug.cleanup_completed"));
        }
    }

    /**
     * Charge tous les marqueurs depuis le fichier de sauvegarde
     */
    private void loadAllMarkers() {
        Map<String, BannerMarkerData> loadedMarkers = persistenceManager.loadMarkers();
        bannerMarkers.putAll(loadedMarkers);

        // Reconstruire les index
        indexManager.rebuildIndexes(bannerMarkers);

        // Ajouter à BlueMap si disponible
        if (blueMapAPIManager.isBlueMapAvailable()) {
            addAllMarkersToBlueMap();
        } else {
            // Si BlueMap n'est pas encore disponible, on log l'information
            if (configManager.isDebugEnabled() && !bannerMarkers.isEmpty()) {
                plugin.getLogger().info(Messages.get("debug.markers_loaded_waiting_bluemap", new Object[]{bannerMarkers.size()}));
            }
        }
    }

    /**
     * Génère une clé unique pour un emplacement
     */
    private String getLocationKey(Location location) {
        return location.getWorld().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    }

    /**
     * Génère le nom d'un marqueur
     */
    private String generateMarkerName(String bannerName) {
        if (bannerName != null && !bannerName.trim().isEmpty()) {
            return bannerName;
        }
        return null;
    }

    /**
     * Récupère le manager API BlueMap
     */
    public BlueMapAPIManager getBlueMapAPIManager() {
        return blueMapAPIManager;
    }

    /**
     * Ajoute tous les marqueurs existants à BlueMap
     * Utilisé quand BlueMap devient disponible après le chargement du plugin
     */
    public void addAllMarkersToBlueMap() {
        if (!blueMapAPIManager.isBlueMapAvailable()) {
            return;
        }

        int addedCount = 0;
        for (BannerMarkerData data : bannerMarkers.values()) {
            try {
                blueMapAPIManager.addMarkerToBlueMap(data);
                addedCount++;
            } catch (Exception e) {
                plugin.getLogger().warning(Messages.get("system.marker_add_error", new Object[]{e.getMessage()}));
            }
        }

        if (addedCount > 0) {
            plugin.getLogger().info(Messages.get("debug.markers_added_to_bluemap_on_enable", new Object[]{addedCount}));
        }
    }

}

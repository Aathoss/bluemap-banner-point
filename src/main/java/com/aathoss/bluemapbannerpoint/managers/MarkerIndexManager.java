package com.aathoss.bluemapbannerpoint.managers;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.managers.data.BannerMarkerData;
import com.aathoss.bluemapbannerpoint.utils.Messages;

import java.util.*;

/**
 * Manager spécialisé dans la gestion des index des marqueurs
 */
public class MarkerIndexManager {

    private final BlueMapBannerPoint plugin;
    private final Map<String, List<BannerMarkerData>> markersByPlayer;
    private final Map<String, List<BannerMarkerData>> markersByWorld;

    public MarkerIndexManager(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
        this.markersByPlayer = new HashMap<>();
        this.markersByWorld = new HashMap<>();
    }

        /**
     * Met à jour les index pour des recherches plus rapides
     */
    public void updateIndexes(BannerMarkerData data, boolean add) {
        if (add) {
            // Ajouter aux index
            markersByPlayer.computeIfAbsent(data.playerName, k -> new ArrayList<>()).add(data);
            markersByWorld.computeIfAbsent(data.world, k -> new ArrayList<>()).add(data);
        } else {
            // Supprimer des index
            List<BannerMarkerData> playerMarkers = markersByPlayer.get(data.playerName);
            if (playerMarkers != null) {
                playerMarkers.removeIf(marker -> marker.markerId.equals(data.markerId));
                if (playerMarkers.isEmpty()) {
                    markersByPlayer.remove(data.playerName);
                }
            }

            List<BannerMarkerData> worldMarkers = markersByWorld.get(data.world);
            if (worldMarkers != null) {
                worldMarkers.removeIf(marker -> marker.markerId.equals(data.markerId));
                if (worldMarkers.isEmpty()) {
                    markersByWorld.remove(data.world);
                }
            }
        }
    }

    /**
     * Récupère tous les marqueurs d'un joueur
     */
    public List<BannerMarkerData> getMarkersByPlayer(String playerName) {
        return markersByPlayer.getOrDefault(playerName, new ArrayList<>());
    }

    /**
     * Récupère tous les marqueurs d'un monde
     */
    public List<BannerMarkerData> getMarkersByWorld(String worldName) {
        return markersByWorld.getOrDefault(worldName, new ArrayList<>());
    }

    /**
     * Récupère tous les marqueurs groupés par joueur
     */
    public Map<String, List<BannerMarkerData>> getMarkersByPlayer() {
        return new HashMap<>(markersByPlayer);
    }

    /**
     * Récupère tous les marqueurs groupés par monde
     */
    public Map<String, List<BannerMarkerData>> getMarkersByWorld() {
        return new HashMap<>(markersByWorld);
    }

    /**
     * Reconstruit les index à partir de la liste complète des marqueurs
     */
    public void rebuildIndexes(Map<String, BannerMarkerData> allMarkers) {
        // Vider les index existants
        markersByPlayer.clear();
        markersByWorld.clear();

        // Reconstruire les index
        for (BannerMarkerData marker : allMarkers.values()) {
            updateIndexes(marker, true);
        }

        if (plugin.getConfigManager().isDebugEnabled()) {
            plugin.getLogger().info(Messages.get("debug.indexes_rebuilt", new Object[]{
                markersByPlayer.size(), markersByWorld.size()
            }));
        }
    }

    /**
     * Nettoie les index vides
     */
    public void cleanupEmptyIndexes() {
        markersByPlayer.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        markersByWorld.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    /**
     * Récupère les statistiques des index
     */
    public Map<String, Object> getIndexStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("players", markersByPlayer.size());
        stats.put("worlds", markersByWorld.size());
        stats.put("totalPlayerMarkers", markersByPlayer.values().stream().mapToInt(List::size).sum());
        stats.put("totalWorldMarkers", markersByWorld.values().stream().mapToInt(List::size).sum());
        return stats;
    }
}

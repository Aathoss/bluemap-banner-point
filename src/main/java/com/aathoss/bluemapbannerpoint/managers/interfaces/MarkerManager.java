package com.aathoss.bluemapbannerpoint.managers.interfaces;

import com.aathoss.bluemapbannerpoint.managers.data.BannerMarkerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface MarkerManager {

    /**
     * Ajoute un marqueur pour une bannière
     */
    void addBannerMarker(Location location, Player player, String bannerName, String iconName);

    /**
     * Supprime un marqueur de bannière
     */
    void removeBannerMarker(Location location);

    /**
     * Met à jour le nom d'un marqueur de bannière
     */
    void updateBannerMarkerName(Location location, Player player, String newBannerName, String iconName);

    /**
     * Vérifie si un marqueur existe à l'emplacement donné
     */
    boolean hasMarkerAt(Location location);

    /**
     * Récupère le marqueur à l'emplacement donné
     */
    BannerMarkerData getMarkerAt(Location location);

    /**
     * Récupère tous les marqueurs d'un joueur
     */
    List<BannerMarkerData> getMarkersByPlayer(String playerName);

    /**
     * Récupère tous les marqueurs d'un monde
     */
    List<BannerMarkerData> getMarkersByWorld(String worldName);

    /**
     * Récupère le nombre total de marqueurs
     */
    int getTotalMarkers();

    /**
     * Récupère tous les marqueurs
     */
    List<BannerMarkerData> getAllMarkers();

    /**
     * Récupère tous les marqueurs groupés par monde
     */
    Map<String, List<BannerMarkerData>> getMarkersByWorld();

    /**
     * Récupère tous les marqueurs groupés par joueur
     */
    Map<String, List<BannerMarkerData>> getMarkersByPlayer();

    /**
     * Sauvegarde tous les marqueurs
     */
    void saveMarkers();

    /**
     * Recharge tous les marqueurs
     */
    void reloadMarkers();

    /**
     * Supprime tous les marqueurs
     */
    void clearAllMarkers();

    /**
     * Nettoie les ressources
     */
    void cleanup();
}

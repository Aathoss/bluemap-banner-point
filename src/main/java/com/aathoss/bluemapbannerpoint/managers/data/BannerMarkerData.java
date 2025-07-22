package com.aathoss.bluemapbannerpoint.managers.data;

/**
 * Classe de données pour les marqueurs de bannières
 * Utilisée par tous les managers pour assurer la cohérence
 */
public class BannerMarkerData {
    public String markerId;
    public String playerUuid;
    public String playerName;
    public String world;
    public int x, y, z;
    public String markerName;
    public String iconName;
    public long timestamp;

    public BannerMarkerData() {}

    public BannerMarkerData(String markerId, String playerUuid, String playerName, String world, int x, int y, int z, String markerName, String iconName, long timestamp) {
        this.markerId = markerId;
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.markerName = markerName;
        this.iconName = iconName;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BannerMarkerData{" +
                "markerId='" + markerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", markerName='" + markerName + '\'' +
                ", iconName='" + iconName + '\'' +
                '}';
    }
}

package com.aathoss.bluemapbannerpoint;

import com.aathoss.bluemapbannerpoint.listeners.BannerListener;
import com.aathoss.bluemapbannerpoint.managers.BlueMapManager;
import com.aathoss.bluemapbannerpoint.managers.ConfigManager;
import com.aathoss.bluemapbannerpoint.managers.MarkerImageManager;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class BlueMapBannerPoint extends JavaPlugin {

    private static BlueMapBannerPoint instance;
    private ConfigManager configManager;
    private BlueMapManager blueMapManager;
    private MarkerImageManager markerImageManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialiser les managers dans le bon ordre
        this.configManager = new ConfigManager(this);

        // Initialiser le système de traduction AVANT les autres managers
        Messages.initialize(this);
        Messages.saveDefaultLanguageFiles();
        Messages.setLanguage(configManager.getLanguage());

        // Initialiser les autres managers après le système de messages
        this.markerImageManager = new MarkerImageManager(this);
        this.blueMapManager = new BlueMapManager(this);

        // Enregistrer les listeners
        getServer().getPluginManager().registerEvents(new BannerListener(this), this);

        // Enregistrer les commandes
        getCommand("bmp").setExecutor(new Commands(this));

        // Vérifier si BlueMap est disponible
        if (this.blueMapManager.getBlueMapAPIManager().isBlueMapAvailable()) {
            getLogger().info(Messages.get("system.bluemap_enabled"));
        } else {
            getLogger().info(Messages.get("system.bluemap_optional_mode"));
        }

        getLogger().info(Messages.get("plugin.enabled"));
    }

    @Override
    public void onDisable() {
        // Nettoyer les marqueurs si nécessaire
        if (blueMapManager != null) {
            blueMapManager.cleanup();
        }

        // Nettoyer les références statiques
        instance = null;

        getLogger().info(Messages.get("plugin.disabled"));
    }

    public static BlueMapBannerPoint getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public BlueMapManager getBlueMapManager() {
        return blueMapManager;
    }

    public MarkerImageManager getMarkerImageManager() {
        return markerImageManager;
    }
}

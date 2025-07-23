package com.aathoss.bluemapbannerpoint.managers;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final BlueMapBannerPoint plugin;
    private FileConfiguration config;

    public ConfigManager(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public void reloadConfig() {
        loadConfig();
    }

    public boolean isNotifyEnabled() {
        return config.getBoolean("marker.notify", true);
    }

    public boolean isSneakyEnabled() {
        return config.getBoolean("marker.sneaky", true);
    }

    public boolean isMarkerVisible() {
        return config.getBoolean("marker.visible", true);
    }

    public String getDefaultMarkerName() {
        return config.getString("marker.default-name", Messages.get("config.markers.category_name"));
    }

    public boolean isDebugEnabled() {
        return config.getBoolean("debug", false);
    }

    public void setDebugEnabled(boolean enabled) {
        config.set("debug", enabled);
        plugin.saveConfig();
    }

    public boolean isAutoRemoveEnabled() {
        return config.getBoolean("auto-remove", true);
    }

    public String getLanguage() {
        return config.getString("language", "en");
    }

    public void setLanguage(String language) {
        config.set("language", language);
        plugin.saveConfig();
        Messages.setLanguage(language);

        // Recharger les messages pour s'assurer que le changement de langue est pris en compte
        Messages.reloadLanguageFiles();
    }
}

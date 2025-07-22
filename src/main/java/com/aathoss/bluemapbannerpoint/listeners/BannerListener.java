package com.aathoss.bluemapbannerpoint.listeners;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.managers.interfaces.MarkerManager;
import com.aathoss.bluemapbannerpoint.managers.ConfigManager;
import com.aathoss.bluemapbannerpoint.utils.DefaultBannerAnalyzer;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BannerListener implements Listener {

    private final BlueMapBannerPoint plugin;
    private final MarkerManager markerManager;
    private final ConfigManager configManager;
    private final DefaultBannerAnalyzer bannerAnalyzer;

    public BannerListener(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
        this.markerManager = plugin.getBlueMapManager(); // RefactoredBlueMapManager implémente MarkerManager
        this.configManager = plugin.getConfigManager();
        this.bannerAnalyzer = new DefaultBannerAnalyzer();
    }

    /**
     * Événement de placement de bloc
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        // Vérifier si c'est une bannière (optimisé avec cache)
        if (!bannerAnalyzer.isBanner(block.getType())) {
            return;
        }

        // Vérifier les permissions (optimisé avec cache)
        if (!player.hasPermission("bluemapbannerpoint.banner")) {
            return;
        }

        // Récupérer le nom et la couleur de la bannière depuis l'item
        String bannerName = bannerAnalyzer.getBannerCustomName(event.getItemInHand());
        String bannerIcon = bannerAnalyzer.getBannerIconName(event.getItemInHand());

        // Vérifie si le joueur est actuellement en sneaky
        if (configManager.isSneakyEnabled()) {
            if (player.isSneaking()) {
                return;
            }
        }

        // Debug: afficher les informations de détection
        if (configManager.isDebugEnabled()) {
            plugin.getLogger().info(Messages.get("debug.banner_detection"));
            plugin.getLogger().info(Messages.get("debug.banner_type", new Object[]{block.getType().name()}));
            plugin.getLogger().info(Messages.get("debug.banner_name", new Object[]{bannerName != null ? bannerName : "Aucun nom personnalisé"}));
            plugin.getLogger().info(Messages.get("debug.banner_icon", new Object[]{bannerIcon}));
            plugin.getLogger().info(Messages.get("debug.banner_player", new Object[]{player.getName()}));
            plugin.getLogger().info(Messages.get("debug.banner_position", new Object[]{block.getLocation()}));
        }

        // Ajouter le marqueur avec l'icône correspondante
        markerManager.addBannerMarker(block.getLocation(), player, bannerName, bannerIcon);

        if (configManager.isNotifyEnabled()) {
            player.sendMessage(Messages.get("config.markers.notify.placed"));
        }

        if (configManager.isDebugEnabled()) {
            plugin.getLogger().info(Messages.get("debug.banner_placed", new Object[]{player.getName(), block.getLocation(), bannerIcon}));
        }
    }

    /**
     * Événement de destruction de bloc
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        // Vérifier si c'est une bannière
        if (bannerAnalyzer.isBanner(block.getType())) {
            // Vérifier si un marqueur existe à cet emplacement avant de le supprimer
            if (markerManager.hasMarkerAt(block.getLocation())) {
                // Supprimer le marqueur
                markerManager.removeBannerMarker(block.getLocation());

                if (configManager.isNotifyEnabled()) {
                    player.sendMessage(Messages.get("config.markers.notify.removed"));
                }

                if (configManager.isDebugEnabled()) {
                    plugin.getLogger().info(Messages.get("debug.banner_destroyed", new Object[]{block.getLocation()}));
                }
            } else {
                // Aucun marqueur à cet emplacement, pas de notification
                if (configManager.isDebugEnabled()) {
                    plugin.getLogger().info(Messages.get("debug.banner_destroyed_no_marker", new Object[]{block.getLocation()}));
                }
            }
        }
    }
}

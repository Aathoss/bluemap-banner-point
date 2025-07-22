package com.aathoss.bluemapbannerpoint.managers;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import de.bluecolored.bluemap.api.BlueMapAPI;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MarkerImageManager {

    private final BlueMapBannerPoint plugin;
    private final Map<String, String> imageCache; // iconName -> imagePath
    private File assetsFolder;

    public MarkerImageManager(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
        this.imageCache = new HashMap<>();
        initializeAssetsFolder();
        extractMarkerImages();
    }

    /**
     * Initialise le dossier des assets
     */
    private void initializeAssetsFolder() {
        this.assetsFolder = new File(plugin.getDataFolder(), "assets");
        if (!assetsFolder.exists()) {
            assetsFolder.mkdirs();
        }
    }

    /**
     * Extrait les images SVG de marqueurs depuis les ressources du plugin
     */
    private void extractMarkerImages() {
        // Noms des icônes disponibles
        String[] iconNames = {
            "white_banner", "orange_banner", "magenta_banner", "light_blue_banner",
            "yellow_banner", "lime_banner", "pink_banner", "gray_banner", "light_gray_banner",
            "cyan_banner", "purple_banner", "blue_banner", "brown_banner", "green_banner",
            "red_banner", "black_banner"
        };

        for (String iconName : iconNames) {
            String resourcePath = "/assets/markers/" + iconName + ".svg";
            File targetFile = new File(assetsFolder, iconName + ".svg");

            // Vérifier si le fichier existe déjà et a une taille > 0
            if (targetFile.exists() && targetFile.length() > 0) {
                imageCache.put(iconName, targetFile.getAbsolutePath());
                continue;
            }

            // Extraire depuis les ressources
            try (InputStream stream = getClass().getResourceAsStream(resourcePath)) {
                if (stream != null) {
                    targetFile.getParentFile().mkdirs();

                    // Copier le contenu du stream vers le fichier
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    try (java.io.FileOutputStream fos = new java.io.FileOutputStream(targetFile)) {
                        while ((bytesRead = stream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    imageCache.put(iconName, targetFile.getAbsolutePath());

                    if (plugin.getConfigManager().isDebugEnabled()) {
                        plugin.getLogger().info("Image SVG extraite: " + iconName + ".svg (" + targetFile.length() + " bytes)");
                    }
                } else {
                    plugin.getLogger().warning("Impossible de charger l'asset SVG: " + resourcePath);
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Erreur lors de l'extraction de l'image SVG " + iconName + ": " + e.getMessage());
            }
        }
    }

    /**
     * Charge les images SVG dans BlueMap en les copiant dans le dossier web
     */
    public void loadImagesToBlueMap(BlueMapAPI api) {
        // Les images sont toujours placées dans bluemap/web/assets/markers à la racine du serveur
        File webAssetsDir = new File("bluemap/web/assets/markers");

        // Vérifier si une mise à jour est nécessaire
        if (shouldUpdateWebAssets(webAssetsDir)) {
            // Supprimer le dossier existant pour garantir une mise à jour propre
            if (webAssetsDir.exists()) {
                deleteDirectory(webAssetsDir);
                plugin.getLogger().info("🗑️ Dossier existant supprimé: " + webAssetsDir.getAbsolutePath());
            }

            // Créer le nouveau dossier
            webAssetsDir.mkdirs();
            plugin.getLogger().info("📁 Création du dossier: " + webAssetsDir.getAbsolutePath());

            int copiedCount = 0;
            for (Map.Entry<String, String> entry : imageCache.entrySet()) {
                String iconName = entry.getKey();
                String imagePath = entry.getValue();

                try {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists() && imageFile.length() > 0) {
                        File webImageFile = new File(webAssetsDir, iconName + ".svg");

                        // Copier le fichier avec vérification
                        try (java.io.FileInputStream fis = new java.io.FileInputStream(imageFile);
                             java.io.FileOutputStream fos = new java.io.FileOutputStream(webImageFile)) {
                            byte[] buffer = new byte[8192]; // Buffer plus grand pour de meilleures performances
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                        }

                        copiedCount++;

                        if (plugin.getConfigManager().isDebugEnabled()) {
                            plugin.getLogger().info("✅ Image SVG copiée: " + iconName + ".svg -> " + webImageFile.getAbsolutePath() + " (" + webImageFile.length() + " bytes)");
                        }
                    } else {
                        plugin.getLogger().warning("⚠️  Fichier source introuvable ou vide: " + imagePath + " (taille: " + (imageFile.exists() ? imageFile.length() : "n'existe pas") + ")");
                    }
                } catch (IOException e) {
                    plugin.getLogger().severe("❌ Erreur lors du chargement de l'image SVG " + iconName + " dans BlueMap: " + e.getMessage());
                }
            }

            plugin.getLogger().info("🗺️ " + copiedCount + " images SVG de marqueurs copiées dans: " + webAssetsDir.getAbsolutePath());
        } else {
            plugin.getLogger().info("📁 Les assets web sont déjà à jour, aucune copie nécessaire");
        }
    }

    /**
     * Vérifie si une mise à jour des assets web est nécessaire
     */
    private boolean shouldUpdateWebAssets(File webAssetsDir) {
        if (!webAssetsDir.exists()) {
            return true;
        }

        File[] webFiles = webAssetsDir.listFiles((dir, name) -> name.endsWith(".svg"));
        if (webFiles == null || webFiles.length != imageCache.size()) {
            return true;
        }

        // Vérifier si tous les fichiers nécessaires existent
        for (String iconName : imageCache.keySet()) {
            File webFile = new File(webAssetsDir, iconName + ".svg");
            if (!webFile.exists()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Supprime récursivement un dossier et son contenu
     */
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    /**
     * Obtient le chemin de l'image pour un nom d'icône donné
     * Retourne le chemin relatif attendu par BlueMap (ex: assets/markers/red_banner.svg)
     */
    public String getImagePath(String iconName) {
        // Vérifier si l'icône existe dans le cache
        if (!imageCache.containsKey(iconName)) {
            iconName = "red_banner"; // Fallback sur red_banner par défaut
        }

        // Retourner le chemin relatif attendu par BlueMap
        return "assets/markers/" + iconName + ".svg";
    }

    /**
     * Vérifie si une image existe pour le nom d'icône donné
     */
    public boolean hasImage(String iconName) {
        return imageCache.containsKey(iconName);
    }

    /**
     * Obtient tous les noms d'icônes disponibles
     */
    public String[] getAvailableIconNames() {
        return imageCache.keySet().toArray(new String[0]);
    }
}

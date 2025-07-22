package com.aathoss.bluemapbannerpoint.utils;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BannerColorUtils {

    // Mapping des couleurs DyeColor vers les noms d'icônes
    private static final Map<DyeColor, String> COLOR_TO_ICON_MAP = new HashMap<>();

    // Cache pour les matériaux de bannières pour éviter les vérifications répétées
    private static final Set<Material> BANNER_MATERIALS = new HashSet<>();

    // Cache pour les couleurs de matériaux
    private static final Map<Material, DyeColor> MATERIAL_TO_COLOR_CACHE = new HashMap<>();

    // Référence au plugin pour le logging
    private static BlueMapBannerPoint plugin;

    static {
        COLOR_TO_ICON_MAP.put(DyeColor.WHITE, "white_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.ORANGE, "orange_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.MAGENTA, "magenta_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.LIGHT_BLUE, "light_blue_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.YELLOW, "yellow_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.LIME, "lime_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.PINK, "pink_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.GRAY, "gray_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.LIGHT_GRAY, "light_gray_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.CYAN, "cyan_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.PURPLE, "purple_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.BLUE, "blue_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.BROWN, "brown_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.GREEN, "green_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.RED, "red_banner");
        COLOR_TO_ICON_MAP.put(DyeColor.BLACK, "black_banner");

        // Initialiser le cache des matériaux de bannières
        initializeBannerMaterials();

        // Initialiser le cache des couleurs de matériaux
        initializeMaterialColorCache();
    }

    /**
     * Initialise la référence au plugin pour le logging
     */
    public static void setPlugin(BlueMapBannerPoint pluginInstance) {
        plugin = pluginInstance;
    }

    /**
     * Initialise le cache des matériaux de bannières
     */
    private static void initializeBannerMaterials() {
        for (Material material : Material.values()) {
            if (material.name().endsWith("_BANNER")) {
                BANNER_MATERIALS.add(material);
            }
        }
    }

    /**
     * Initialise le cache des couleurs de matériaux
     */
    private static void initializeMaterialColorCache() {
        for (Material material : Material.values()) {
            if (material.name().endsWith("_BANNER")) {
                String colorName = material.name().replace("_BANNER", "");
                try {
                    DyeColor color = DyeColor.valueOf(colorName);
                    MATERIAL_TO_COLOR_CACHE.put(material, color);
                } catch (IllegalArgumentException e) {
                    // Ignorer les matériaux qui ne correspondent pas à une couleur
                }
            }
        }
    }

    /**
     * Méthode utilitaire pour logger les messages en mode debug uniquement
     */
    private static void debugLog(String message) {
        if (plugin != null && plugin.getConfigManager().isDebugEnabled()) {
            plugin.getLogger().info(message);
        }
    }

    /**
     * Récupère la couleur de base d'une bannière depuis un ItemStack
     */
    public static DyeColor getBannerBaseColor(ItemStack item) {
        if (item == null) {
            return null;
        }

        // D'abord, essayer de déduire la couleur depuis le type de matériau
        DyeColor colorFromMaterial = getDyeColorFromMaterial(item.getType());
        if (colorFromMaterial != null) {
            return colorFromMaterial;
        }

        // Si on a des métadonnées de bannière, essayer de récupérer la couleur depuis les patterns
        if (item.hasItemMeta() && item.getItemMeta() instanceof BannerMeta) {
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            if (!meta.getPatterns().isEmpty()) {
                return meta.getPatterns().get(0).getColor();
            }
        }

        return null;
    }

        /**
     * Déduit la couleur de base à partir du type de matériau de la bannière
     */
    private static DyeColor getDyeColorFromMaterial(Material material) {
        if (material == null) {
            return null;
        }

        // Utiliser le cache pour une recherche plus rapide
        DyeColor cachedColor = MATERIAL_TO_COLOR_CACHE.get(material);
        if (cachedColor != null) {
            return cachedColor;
        }

        // Fallback pour les matériaux non mis en cache
        String materialName = material.name();
        if (materialName.endsWith("_BANNER")) {
            String colorName = materialName.replace("_BANNER", "");
            try {
                return DyeColor.valueOf(colorName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Récupère la couleur de base d'une bannière depuis un BlockState
     */
    public static DyeColor getBannerBaseColor(BlockState blockState) {
        if (blockState instanceof Banner) {
            Banner banner = (Banner) blockState;
            return banner.getBaseColor();
        }
        return null;
    }

    /**
     * Récupère le nom de l'icône correspondant à la couleur de la bannière
     */
    public static String getBannerIconName(DyeColor color) {
        if (color == null) {
            debugLog("⚠️  Couleur null détectée, utilisation de l'icône par défaut: red_banner");
            return "red_banner"; // Icône par défaut rouge
        }

        String iconName = COLOR_TO_ICON_MAP.get(color);
        if (iconName == null) {
            debugLog("⚠️  Couleur " + color.name() + " non trouvée dans le mapping, utilisation de l'icône par défaut: red_banner");
            return "red_banner";
        }

        debugLog("✅ Couleur " + color.name() + " mappée vers l'icône: " + iconName);
        return iconName;
    }

    /**
     * Récupère le nom de l'icône depuis un ItemStack de bannière
     */
    public static String getBannerIconName(ItemStack item) {
        if (item == null) {
            return "red_banner"; // Icône par défaut
        }

        DyeColor color = getBannerBaseColor(item);
        String iconName = getBannerIconName(color);

        // Debug: afficher la couleur détectée
        if (color != null) {
            debugLog("🎨 Bannière détectée: " + color.name() + " -> Icône: " + iconName);
        } else {
            debugLog("⚠️  Couleur de bannière non détectée, utilisation de l'icône par défaut: " + iconName);
        }

        return iconName;
    }

    /**
     * Récupère le nom de l'icône depuis un BlockState de bannière
     */
    public static String getBannerIconName(BlockState blockState) {
        DyeColor color = getBannerBaseColor(blockState);
        return getBannerIconName(color);
    }

    /**
     * Vérifie si un matériau est une bannière
     */
    public static boolean isBanner(Material material) {
        if (material == null) {
            return false;
        }
        // Utiliser le cache pour une vérification plus rapide
        return BANNER_MATERIALS.contains(material);
    }

    /**
     * Récupère le nom personnalisé d'une bannière depuis un ItemStack
     */
    public static String getBannerCustomName(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta() instanceof BannerMeta) {
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            if (meta.hasDisplayName()) {
                return meta.getDisplayName();
            }
        }
        return null;
    }

    /**
     * Récupère le nom personnalisé d'une bannière depuis un BlockState
     * Note: Les blocs Banner placés dans le monde n'ont pas de nom personnalisé
     */
    public static String getBannerCustomName(BlockState blockState) {
        // Les blocs Banner placés dans le monde n'ont pas de nom personnalisé
        // Seuls les ItemStack peuvent avoir un nom personnalisé
        return null;
    }
}

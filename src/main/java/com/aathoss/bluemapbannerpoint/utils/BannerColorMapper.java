package com.aathoss.bluemapbannerpoint.utils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe spécialisée dans le mapping des couleurs de bannières vers les icônes
 */
public class BannerColorMapper {

    // Mapping des couleurs DyeColor vers les noms d'icônes
    private static final Map<DyeColor, String> COLOR_TO_ICON_MAP = new HashMap<>();

    // Cache pour les couleurs de matériaux
    private static final Map<Material, DyeColor> MATERIAL_TO_COLOR_CACHE = new HashMap<>();

    static {
        initializeColorMapping();
        initializeMaterialColorCache();
    }

    /**
     * Initialise le mapping des couleurs vers les icônes
     */
    private static void initializeColorMapping() {
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
     * Récupère le nom de l'icône correspondant à une couleur
     * @param color La couleur de la bannière
     * @return Le nom de l'icône correspondante
     */
    public static String getBannerIconName(DyeColor color) {
        return COLOR_TO_ICON_MAP.getOrDefault(color, "white_banner");
    }

    /**
     * Récupère la couleur de base d'un matériau de bannière
     * @param material Le matériau de la bannière
     * @return La couleur de base, ou null si non trouvée
     */
    public static DyeColor getDyeColorFromMaterial(Material material) {
        return MATERIAL_TO_COLOR_CACHE.get(material);
    }

    /**
     * Récupère tous les mappings de couleurs disponibles
     * @return Map des couleurs vers icônes
     */
    public static Map<DyeColor, String> getColorMappings() {
        return new HashMap<>(COLOR_TO_ICON_MAP);
    }
}

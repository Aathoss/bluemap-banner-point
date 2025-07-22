package com.aathoss.bluemapbannerpoint.utils;

import org.bukkit.Material;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe spécialisée dans la détection des types de bannières
 */
public class BannerTypeDetector {

    // Cache pour les matériaux de bannières pour éviter les vérifications répétées
    private static final Set<Material> BANNER_MATERIALS = new HashSet<>();

    static {
        initializeBannerMaterials();
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
     * Vérifie si un matériau est une bannière
     * @param material Le matériau à vérifier
     * @return true si c'est une bannière, false sinon
     */
    public static boolean isBanner(Material material) {
        return BANNER_MATERIALS.contains(material);
    }

    /**
     * Récupère tous les matériaux de bannières
     * @return Set des matériaux de bannières
     */
    public static Set<Material> getBannerMaterials() {
        return new HashSet<>(BANNER_MATERIALS);
    }
}

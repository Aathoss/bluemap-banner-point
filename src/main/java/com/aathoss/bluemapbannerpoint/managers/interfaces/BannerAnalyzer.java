package com.aathoss.bluemapbannerpoint.managers.interfaces;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

public interface BannerAnalyzer {

    /**
     * Vérifie si un matériau est une bannière
     */
    boolean isBanner(Material material);

    /**
     * Récupère la couleur de base d'une bannière depuis un ItemStack
     */
    DyeColor getBannerBaseColor(ItemStack item);

    /**
     * Récupère la couleur de base d'une bannière depuis un BlockState
     */
    DyeColor getBannerBaseColor(BlockState blockState);

    /**
     * Récupère le nom de l'icône correspondant à une couleur
     */
    String getBannerIconName(DyeColor color);

    /**
     * Récupère le nom de l'icône d'une bannière depuis un ItemStack
     */
    String getBannerIconName(ItemStack item);

    /**
     * Récupère le nom de l'icône d'une bannière depuis un BlockState
     */
    String getBannerIconName(BlockState blockState);

    /**
     * Récupère le nom personnalisé d'une bannière depuis un ItemStack
     */
    String getBannerCustomName(ItemStack item);

    /**
     * Récupère le nom personnalisé d'une bannière depuis un BlockState
     */
    String getBannerCustomName(BlockState blockState);
}

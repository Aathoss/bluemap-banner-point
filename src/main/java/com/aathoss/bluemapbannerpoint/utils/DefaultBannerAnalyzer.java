package com.aathoss.bluemapbannerpoint.utils;

import com.aathoss.bluemapbannerpoint.managers.interfaces.BannerAnalyzer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

/**
 * Implémentation par défaut de BannerAnalyzer
 * Utilise les classes spécialisées pour respecter le principe de responsabilité unique
 */
public class DefaultBannerAnalyzer implements BannerAnalyzer {

    @Override
    public boolean isBanner(Material material) {
        return BannerTypeDetector.isBanner(material);
    }

        @Override
    public DyeColor getBannerBaseColor(ItemStack item) {
        if (item == null) {
            return null;
        }

        // Utiliser le mapping basé sur le matériau pour les ItemStack
        return BannerColorMapper.getDyeColorFromMaterial(item.getType());
    }

    @Override
    public DyeColor getBannerBaseColor(BlockState blockState) {
        if (blockState instanceof Banner) {
            Banner banner = (Banner) blockState;
            return banner.getBaseColor();
        }
        return null;
    }

    @Override
    public String getBannerIconName(DyeColor color) {
        return BannerColorMapper.getBannerIconName(color);
    }

    @Override
    public String getBannerIconName(ItemStack item) {
        DyeColor color = getBannerBaseColor(item);
        if (color == null) {
            // Fallback vers la couleur basée sur le matériau
            color = BannerColorMapper.getDyeColorFromMaterial(item.getType());
        }
        return getBannerIconName(color);
    }

    @Override
    public String getBannerIconName(BlockState blockState) {
        DyeColor color = getBannerBaseColor(blockState);
        return getBannerIconName(color);
    }

    @Override
    public String getBannerCustomName(ItemStack item) {
        return BannerNameExtractor.getBannerCustomName(item);
    }

    @Override
    public String getBannerCustomName(BlockState blockState) {
        return BannerNameExtractor.getBannerCustomName(blockState);
    }
}

package com.aathoss.bluemapbannerpoint.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

/**
 * Classe spécialisée dans l'extraction des noms personnalisés des bannières
 */
public class BannerNameExtractor {

    /**
     * Récupère le nom personnalisé d'une bannière depuis un ItemStack
     * @param item L'ItemStack de la bannière
     * @return Le nom personnalisé, ou null s'il n'y en a pas
     */
    public static String getBannerCustomName(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }

        BannerMeta meta = (BannerMeta) item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return null;
        }

        Component displayName = meta.displayName();
        if (displayName == null) {
            return null;
        }

        return PlainTextComponentSerializer.plainText().serialize(displayName).trim();
    }

    /**
     * Récupère le nom personnalisé d'une bannière depuis un BlockState
     * Note: Les blocs Banner placés dans le monde n'ont pas de nom personnalisé
     * @param blockState Le BlockState de la bannière
     * @return Toujours null car les blocs Banner n'ont pas de nom personnalisé
     */
    public static String getBannerCustomName(BlockState blockState) {
        if (blockState instanceof Banner) {
            // Les blocs Banner placés dans le monde n'ont pas de nom personnalisé
            // Seuls les ItemStack peuvent avoir un nom personnalisé
            return null;
        }
        return null;
    }
}

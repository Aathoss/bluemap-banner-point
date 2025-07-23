package com.aathoss.bluemapbannerpoint.utils;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Messages {

    private static BlueMapBannerPoint plugin;
    private static String currentLanguage = "fr";
    private static Map<String, FileConfiguration> languageFiles = new HashMap<>();

    public static void initialize(BlueMapBannerPoint pluginInstance) {
        plugin = pluginInstance;
        loadLanguages();
    }

    private static void loadLanguages() {
        String[] languages = {"fr", "en"};

        for (String lang : languages) {
            loadLanguageFile(lang);
        }
    }

    private static void loadLanguageFile(String language) {
        // Essayer de charger depuis le dossier du plugin d'abord
        File langFile = new File(plugin.getDataFolder(), "lang/" + language + ".yml");

        if (langFile.exists()) {
            languageFiles.put(language, YamlConfiguration.loadConfiguration(langFile));
        } else {
            // Charger depuis les ressources du plugin
            try {
                InputStream inputStream = plugin.getResource("lang/" + language + ".yml");
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    FileConfiguration config = YamlConfiguration.loadConfiguration(reader);
                    languageFiles.put(language, config);
                    reader.close();
                    inputStream.close();
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Impossible de charger le fichier de langue " + language + ": " + e.getMessage());
            }
        }
    }

    public static void setLanguage(String language) {
        if (languageFiles.containsKey(language)) {
            currentLanguage = language;
        } else {
            plugin.getLogger().warning("Langue '" + language + "' non trouvée, utilisation du français par défaut");
            currentLanguage = "fr";
        }
    }

    /**
     * Recharge les fichiers de langue pour s'assurer que les changements sont pris en compte
     */
    public static void reloadLanguageFiles() {
        languageFiles.clear();
        loadLanguages();
    }

    public static String get(String key) {
        return get(key, currentLanguage);
    }

    public static String get(String key, String language) {
        FileConfiguration config = languageFiles.get(language);
        if (config == null) {
            config = languageFiles.get("fr"); // Fallback vers français
        }

        if (config == null) {
            return "§c[CONFIGURATION MANQUANTE]";
        }

        String message = config.getString(key);
        if (message == null) {
            // Cache des messages manquants pour éviter les logs répétés
            if (plugin != null) {
                plugin.getLogger().warning("Clé de traduction manquante: " + key + " pour la langue: " + language);
            }
            return "§c[TRADUCTION MANQUANTE: " + key + "]";
        }

        return message;
    }

    public static String get(String key, Object... args) {
        String message = get(key);
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return message;
    }

    public static void saveDefaultLanguageFiles() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        String[] languages = {"fr", "en"};
        for (String lang : languages) {
            File langFile = new File(langFolder, lang + ".yml");
            if (!langFile.exists()) {
                plugin.saveResource("lang/" + lang + ".yml", false);
            }
        }
    }
}

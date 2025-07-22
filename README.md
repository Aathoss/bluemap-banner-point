# 🗺️ BlueMap Banner Point

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/thomas/bluemap-banner-point)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1+-green.svg)](https://www.minecraft.net/)
[![BlueMap](https://img.shields.io/badge/BlueMap-2.7.4+-orange.svg)](https://github.com/BlueMap-Minecraft/BlueMap)
[![Java](https://img.shields.io/badge/Java-21+-red.svg)](https://openjdk.java.net/)

Un plugin Minecraft qui ajoute automatiquement des marqueurs BlueMap lors du placement de bannières, avec des icônes SVG personnalisées et colorées.

## ✨ Fonctionnalités

- 🎨 **Marqueurs automatiques** : Création automatique de marqueurs BlueMap lors du placement de bannières
- 🎯 **Icônes SVG personnalisées** : Design unique avec mât en bois et drapeaux colorés selon la couleur Minecraft
- 🌍 **Support multilingue** : Interface en français et anglais
- ⚙️ **Configuration flexible** : Options personnalisables via `config.yml`
- 🔧 **Commandes d'administration** : Gestion facile via commandes in-game
- 🗑️ **Nettoyage automatique** : Suppression automatique des marqueurs lors de la destruction des bannières
- 🎭 **Mode furtif** : Option pour ne pas placer de marqueurs en mode sneak

## 📋 Prérequis

- **Minecraft** : 1.20.6 - 1.21.8 (comme BlueMap 5.9)
- **Serveur Minecraft** : Compatible avec Bukkit, Spigot, Paper, et autres forks
- **BlueMap** : Version 2.7.4+ (BlueMap 5.9+)
- **Java** : Version 21 ou supérieure

## 🚀 Installation

1. **Téléchargez** le fichier `.jar` depuis la section [Releases](https://github.com/thomas/bluemap-banner-point/releases)
2. **Placez** le fichier dans le dossier `plugins/` de votre serveur
3. **Redémarrez** votre serveur
4. **Configurez** le plugin via le fichier `plugins/BlueMapBannerPoint/config.yml`

## 🔧 Compatibilité

Ce plugin est compatible avec tous les serveurs Minecraft basés sur l'API Bukkit :

- ✅ **Bukkit** - Serveur vanilla Bukkit
- ✅ **Spigot** - Fork optimisé de Bukkit
- ✅ **Paper** - Fork optimisé de Spigot
- ✅ **Purpur** - Fork optimisé de Paper
- ✅ **Fabric** - Avec mods de compatibilité Bukkit
- ✅ **Forge** - Avec mods de compatibilité Bukkit

Le plugin utilise uniquement l'API Bukkit standard, garantissant une compatibilité maximale.

## ⚙️ Configuration

### Configuration par défaut (`config.yml`)

```yaml
# Configuration des marqueurs
marker:
  # Notifier le joueur lorsque le marqueur est placé
  notify: true

  # Ne pas placer de marqueur si le joueur est en sneaky
  sneaky: true

  # Visibilité du marqueur
  visible: true

# Suppression automatique des marqueurs quand la bannière est détruite
auto-remove: true

# Mode debug (affiche plus d'informations dans la console)
debug: false

# Langue du plugin (fr = français, en = anglais)
language: "en"
```

### Options de configuration

| Option           | Description                             | Valeur par défaut |
| ---------------- | --------------------------------------- | ----------------- |
| `marker.notify`  | Notifier le joueur lors du placement    | `true`            |
| `marker.sneaky`  | Ignorer le placement en mode sneak      | `true`            |
| `marker.visible` | Rendre les marqueurs visibles           | `true`            |
| `auto-remove`    | Supprimer automatiquement les marqueurs | `true`            |
| `debug`          | Mode debug pour les développeurs        | `false`           |
| `language`       | Langue du plugin (`fr` ou `en`)         | `en`              |

## 🎮 Commandes

| Commande      | Permission                 | Description                         |
| ------------- | -------------------------- | ----------------------------------- |
| `/bmp reload` | `bluemapbannerpoint.admin` | Recharge la configuration du plugin |
| `/bmp help`   | `bluemapbannerpoint.admin` | Affiche l'aide des commandes        |

## 🔐 Permissions

| Permission                  | Description                                         | Défaut |
| --------------------------- | --------------------------------------------------- | ------ |
| `bluemapbannerpoint.admin`  | Accès aux commandes d'administration                | `op`   |
| `bluemapbannerpoint.banner` | Permission pour placer des bannières avec marqueurs | `true` |

## 🎨 Icônes SVG personnalisées

Le plugin inclut des icônes SVG personnalisées pour chaque couleur de bannière Minecraft :

- **Design unique** : Mât en bois (#845832) avec support noir
- **Couleurs authentiques** : Drapeaux colorés selon les couleurs Minecraft officielles
- **Qualité vectorielle** : Icônes SVG pour une qualité optimale
- **Intégration automatique** : Chargement automatique par BlueMap

### Couleurs supportées

Toutes les couleurs de bannières Minecraft sont supportées :

- `white_banner`, `orange_banner`, `magenta_banner`, `light_blue_banner`
- `yellow_banner`, `lime_banner`, `pink_banner`, `gray_banner`
- `light_gray_banner`, `cyan_banner`, `purple_banner`, `blue_banner`
- `brown_banner`, `green_banner`, `red_banner`, `black_banner`

## 📝 Changelog

### Version 1.0.1

- 🔄 **Compatibilité étendue** : Support de tous les serveurs Bukkit/Spigot/Paper
- 🆕 **Minecraft 1.20.6+** : Support étendu comme BlueMap 5.9 (1.20.6 - 1.21.8)
- 🔧 **API Spigot** : Migration vers l'API Spigot pour une compatibilité maximale
- 📈 **Audience élargie** : Compatible avec plus de serveurs et versions

### Version 1.0.0

- ✨ Première version stable
- 🎨 Icônes SVG personnalisées pour toutes les couleurs de bannières
- 🌍 Support multilingue (français/anglais)
- ⚙️ Configuration flexible
- 🔧 Commandes d'administration
- 🗑️ Nettoyage automatique des marqueurs
- 🎭 Mode furtif (sneak)

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :

1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence GNU General Public License v3.0. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 🙏 Remerciements

- [BlueMap](https://github.com/BlueMap-Minecraft/BlueMap) - Pour l'excellent système de cartographie
- [Spigot](https://www.spigotmc.org/) - Pour l'API Minecraft standard
- La communauté Minecraft pour les retours et suggestions

## 📞 Support

- **Issues** : [GitHub Issues](https://github.com/thomas/bluemap-banner-point/issues)
- **Discussions** : [GitHub Discussions](https://github.com/thomas/bluemap-banner-point/discussions)

---

**BlueMap Banner Point** - Rendez votre monde Minecraft plus accessible avec des marqueurs automatiques ! 🗺️✨

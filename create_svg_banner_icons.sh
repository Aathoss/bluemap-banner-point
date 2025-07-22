#!/bin/bash

# Script pour créer des icônes de bannière SVG pour BlueMap avec toutes les couleurs Minecraft
# Utilise le design SVG personnalisé fourni

ICON_SIZE=32
RESOURCES_DIR="src/main/resources/assets/markers"
SVG_TEMPLATE="banner.svg"

echo "🎨 Création d'icônes de bannière SVG avec toutes les couleurs Minecraft..."

# Plus besoin d'ImageMagick pour les SVG

# Vérifier si le fichier SVG existe
if [ ! -f "$SVG_TEMPLATE" ]; then
    echo "❌ Erreur: Le fichier $SVG_TEMPLATE n'existe pas!"
    exit 1
fi

# Créer le dossier de ressources
mkdir -p "$RESOURCES_DIR"

# Couleurs Minecraft avec noms exacts de Minecraft
declare -A MINECRAFT_COLORS=(
    ["white_banner"]="#FFFFFF"
    ["orange_banner"]="#FF8F00"
    ["magenta_banner"]="#C74EBD"
    ["light_blue_banner"]="#3AAFD9"
    ["yellow_banner"]="#FFD83D"
    ["lime_banner"]="#80C71F"
    ["pink_banner"]="#F38BAA"
    ["gray_banner"]="#474F52"
    ["light_gray_banner"]="#9D9D97"
    ["cyan_banner"]="#169C9C"
    ["purple_banner"]="#8932B8"
    ["blue_banner"]="#3C44AA"
    ["brown_banner"]="#825432"
    ["green_banner"]="#5E7C16"
    ["red_banner"]="#B02E26"
    ["black_banner"]="#1D1C21"
)

# Créer des variantes SVG avec différentes couleurs Minecraft
echo "🎨 Création des variantes SVG colorées..."

for color_name in "${!MINECRAFT_COLORS[@]}"; do
    color="${MINECRAFT_COLORS[$color_name]}"

    echo "  Création de l'icône ${color_name}..."

    # Créer directement le SVG avec la couleur remplacée
    sed "s/fill=\"#C74EBD\"/fill=\"$color\"/g" "$SVG_TEMPLATE" > "$RESOURCES_DIR/${color_name}.svg"
done

# Créer un fichier de configuration avec toutes les couleurs disponibles
echo "📝 Création du fichier de configuration des couleurs..."
cat > "$RESOURCES_DIR/colors.yml" << EOF
# Couleurs disponibles pour les icônes de bannière (noms Minecraft)
# Utilisez ces noms dans config.yml: marker.icon = "<color>_banner"

available_colors:
  - "white_banner"        # Blanche
  - "orange_banner"       # Orange
  - "magenta_banner"      # Magenta
  - "light_blue_banner"   # Bleu clair
  - "yellow_banner"       # Jaune
  - "lime_banner"         # Vert lime
  - "pink_banner"         # Rose
  - "gray_banner"         # Gris
  - "light_gray_banner"   # Gris clair
  - "cyan_banner"         # Cyan
  - "purple_banner"       # Violet
  - "blue_banner"         # Bleu
  - "brown_banner"        # Marron
  - "green_banner"        # Vert
  - "red_banner"          # Rouge
  - "black_banner"        # Noir
EOF

# Créer un fichier de documentation pour l'API BlueMap
echo "📚 Création de la documentation API BlueMap..."
cat > "BLUEMAP_SVG_BANNER_GUIDE.md" << EOF
# 🗺️ Intégration API BlueMap - Bannières SVG

## Utilisation des ressources intégrées

Les icônes de bannière SVG sont maintenant intégrées dans le plugin et peuvent être utilisées directement via l'API BlueMap.

### Design SVG personnalisé
Les bannières utilisent un design SVG personnalisé avec :
- Mât en bois (#845832)
- Support noir
- Drapeau coloré selon la couleur Minecraft choisie

### Chargement automatique
BlueMap charge automatiquement les ressources depuis le plugin lors de l'initialisation.

### Noms des icônes
Utilisez les noms exacts de Minecraft dans votre configuration :
- \`red_banner\`
- \`blue_banner\`
- \`green_banner\`
- etc.

## Avantages
- ✅ Design SVG personnalisé et détaillé
- ✅ Pas de copie manuelle de fichiers
- ✅ Ressources toujours disponibles
- ✅ Mise à jour automatique
- ✅ Installation simplifiée
- ✅ Qualité vectorielle préservée
EOF

# Afficher le résumé
echo ""
echo "✅ Icônes SVG créées avec succès !"
echo "📁 Dossier: $RESOURCES_DIR"
echo "📏 Format: SVG vectoriel (qualité parfaite à toutes les tailles)"
echo "🎨 Nombre d'icônes: $((${#MINECRAFT_COLORS[@]} + 1))"
echo "🎨 Design: SVG personnalisé avec mât et support"
echo ""
echo "🎯 Pour utiliser ces icônes:"
echo "1. Dans config.yml, utilisez: marker.icon = \"<color>_banner.svg\""
echo "2. Exemples: red_banner.svg, blue_banner.svg, green_banner.svg, etc."
echo "3. Les icônes SVG sont intégrées dans le plugin et chargées automatiquement par BlueMap"
echo "4. Avantage: Qualité vectorielle parfaite à toutes les tailles d'affichage"
echo ""
echo "📋 Couleurs disponibles:"
cat "$RESOURCES_DIR/colors.yml"

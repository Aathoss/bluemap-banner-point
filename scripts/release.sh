#!/bin/bash

# Script de release pour BlueMap Banner Point
# Usage: ./scripts/release.sh [version] [message]

set -e

# Couleurs pour les messages
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PROJECT_NAME="bluemap-banner-point"
DEFAULT_MESSAGE="Release version"

# Fonctions utilitaires
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Vérification des prérequis
check_prerequisites() {
    log_info "Vérification des prérequis..."

    # Vérifier que git est disponible
    if ! command -v git &> /dev/null; then
        log_error "Git n'est pas installé"
        exit 1
    fi

    # Vérifier que nous sommes dans un repository git
    if ! git rev-parse --git-dir > /dev/null 2>&1; then
        log_error "Ce répertoire n'est pas un repository git"
        exit 1
    fi

    # Vérifier que nous sommes sur la branche main
    CURRENT_BRANCH=$(git branch --show-current)
    if [ "$CURRENT_BRANCH" != "main" ]; then
        log_warning "Vous n'êtes pas sur la branche main (actuellement sur $CURRENT_BRANCH)"
        read -p "Continuer quand même ? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            log_info "Annulation de la release"
            exit 0
        fi
    fi

    # Vérifier qu'il n'y a pas de modifications non commitées
    if ! git diff-index --quiet HEAD --; then
        log_error "Il y a des modifications non commitées. Committez-les d'abord."
        exit 1
    fi

    log_success "Prérequis vérifiés"
}

# Validation du format de version
validate_version() {
    local version=$1

    # Format sémantique: X.Y.Z
    if [[ ! $version =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
        log_error "Format de version invalide. Utilisez le format X.Y.Z (ex: 1.2.3)"
        exit 1
    fi

    log_success "Version validée: $version"
}

# Mise à jour du pom.xml
update_pom_version() {
    local version=$1

    log_info "Mise à jour de la version dans pom.xml..."

    # Utiliser sed pour mettre à jour UNIQUEMENT la version du projet (pas les plugins)
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS - cibler spécifiquement la version du projet
        sed -i '' "/<artifactId>bluemap-banner-point<\/artifactId>/,/<version>/s/<version>.*<\/version>/<version>$version<\/version>/" pom.xml
    else
        # Linux - cibler spécifiquement la version du projet
        sed -i "/<artifactId>bluemap-banner-point<\/artifactId>/,/<version>/s/<version>.*<\/version>/<version>$version<\/version>/" pom.xml
    fi

    log_success "Version mise à jour dans pom.xml"
}

# Création du commit de release
create_release_commit() {
    local version=$1
    local message=$2

    log_info "Création du commit de release..."

    # Ajouter les modifications
    git add pom.xml

    # Créer le commit
    git commit -m "chore: release version $version

$message"

    log_success "Commit de release créé"
}

# Création du tag
create_tag() {
    local version=$1
    local message=$2

    log_info "Création du tag v$version..."

    # Créer le tag annoté
    git tag -a "v$version" -m "Release version $version

$message"

    log_success "Tag v$version créé"
}

# Push des modifications
push_changes() {
    local version=$1

    log_info "Push des modifications et du tag..."

    # Push de la branche
    git push origin main

    # Push du tag
    git push origin "v$version"

    log_success "Modifications et tag poussés"
}

# Affichage des informations de release
show_release_info() {
    local version=$1

    log_success "Release $version créée avec succès !"
    echo
    echo "📦 Informations de la release :"
    echo "   Version: $version"
    echo "   Tag: v$version"
    echo "   Branche: main"
    echo
    echo "🚀 Prochaines étapes :"
    echo "   1. Le workflow CI/CD va se déclencher automatiquement"
    echo "   2. Le package JAR sera généré : $PROJECT_NAME-$version.jar"
    echo "   3. Les notifications Discord seront envoyées"
    echo "   4. Les artefacts seront disponibles dans Gitea Actions"
    echo
    echo "📋 Pour vérifier le statut :"
    echo "   - Consultez les workflows dans Gitea Actions"
    echo "   - Vérifiez les notifications Discord"
    echo "   - Téléchargez les artefacts une fois le build terminé"
}

# Fonction principale
main() {
    echo "🚀 Script de release pour $PROJECT_NAME"
    echo "========================================"
    echo

    # Vérifier les arguments
    if [ $# -eq 0 ]; then
        log_error "Version manquante"
        echo "Usage: $0 <version> [message]"
        echo "Exemple: $0 1.2.3 \"Correction de bugs\""
        exit 1
    fi

    VERSION=$1
    MESSAGE=${2:-"$DEFAULT_MESSAGE $VERSION"}

    log_info "Début de la release version $VERSION"
    log_info "Message: $MESSAGE"
    echo

    # Exécuter les étapes
    check_prerequisites
    validate_version "$VERSION"
    update_pom_version "$VERSION"
    create_release_commit "$VERSION" "$MESSAGE"
    create_tag "$VERSION" "$MESSAGE"
    push_changes "$VERSION"

    echo
    show_release_info "$VERSION"
}

# Gestion des erreurs
trap 'log_error "Une erreur est survenue. Annulation de la release."; exit 1' ERR

# Exécution du script
main "$@"

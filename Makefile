# Makefile pour BlueMap Banner Point

PLUGIN_NAME=bluemap-banner-point
PLUGIN_JAR=target/$(PLUGIN_NAME)-1.0.0.jar
SERVER_DIR=server-test
PLUGINS_DIR=$(SERVER_DIR)/plugins

.PHONY: all build clean package copy-jar run-test setup-server create-icon generate-icon

all: build

build:
	mvn clean compile

package:
	@echo "----- Compilation et packaging du plugin... -----"
	mvn clean package

clean:
	mvn clean

copy-jar:
	@echo "----- Copie du JAR dans le serveur de test... -----"
	mkdir -p $(PLUGINS_DIR)
	cp $(PLUGIN_JAR) $(PLUGINS_DIR)/

run-test:
	make package
	make copy-jar
	make setup-server
	@echo "----- Lancement du serveur de test... -----"
	cd $(SERVER_DIR) && ./start.sh

setup-server:
	@echo "----- Mise à jour du serveur de test... -----"
	make update-paper
	@echo "----- Serveur de test configuré ! -----"
	@echo "Pour démarrer: make run-test"

update-paper:
	mkdir -p $(SERVER_DIR)
	@echo "Téléchargement de Paper 1.21.7..."
	@PAPER_URL=$$(curl -s https://fill.papermc.io/v3/projects/paper/versions/1.21.7/builds/latest | grep -o '"url":"[^"]*"' | cut -d'"' -f4); \
	if [ -n "$$PAPER_URL" ]; then \
		wget -O $(SERVER_DIR)/paper.jar "$$PAPER_URL"; \
		echo "Paper téléchargé avec succès !"; \
	else \
		echo "Erreur: Impossible de récupérer l'URL de téléchargement"; \
		echo "Téléchargez Paper manuellement depuis https://papermc.io/downloads"; \
	fi

create-icon:
	@echo "Création d'une icône bannière simple..."
	@echo "Pour une icône personnalisée, créez un fichier SVG"
	@echo "et placez-le dans: BlueMap/web/assets/markers/banner.svg"
	@echo ""
	@echo "Puis dans config.yml, utilisez:"
	@echo "marker:"
	@echo "  icon: \"banner.svg\""

generate-icon:
	@echo "🎨 Génération automatique des icônes de bannière avec toutes les couleurs Minecraft..."
	@./create_banner_icon.sh

install-icons:
	@echo "📦 Les icônes sont intégrées dans le plugin et chargées automatiquement par BlueMap"
	@echo "🎯 Aucune installation manuelle nécessaire !"

install-icon:
	@echo "Installation de l'icône dans BlueMap..."
	@if [ -d "$(SERVER_DIR)/plugins/BlueMap" ]; then \
		mkdir -p "$(SERVER_DIR)/plugins/BlueMap/web/assets/markers/"; \
		echo "Dossier créé: $(SERVER_DIR)/plugins/BlueMap/web/assets/markers/"; \
		echo "Placez votre fichier banner.svg dans ce dossier"; \
	else \
		echo "BlueMap n'est pas installé. Lancez d'abord: make update-bluemap"; \
	fi

help:
	@echo "Commandes disponibles:"
	@echo "  build        - Compile le projet"
	@echo "  package      - Crée le JAR du plugin"
	@echo "  clean        - Nettoie le projet"
	@echo "  copy-jar     - Copie le JAR dans le serveur de test"
	@echo "  run-test     - Lance le serveur de test"
	@echo "  setup-server - Configure le serveur de test (Paper + BlueMap)"
	@echo "  create-icon  - Instructions pour créer une icône personnalisée"
	@echo "  generate-icon - Génère automatiquement toutes les icônes de bannière"
	@echo "  install-icons - Info sur l'intégration automatique des icônes"
	@echo "  install-icon - Prépare le dossier pour l'icône BlueMap"
	@echo "  help         - Affiche cette aide"

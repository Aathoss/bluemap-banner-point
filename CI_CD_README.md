# CI/CD - BlueMap Banner Point

Ce document décrit la configuration CI/CD mise en place pour le projet BlueMap Banner Point.

## 🏗️ Architecture des Workflows

### 1. **CI - Développement** (`.gitea/workflows/ci-develop.yml`)

**Déclencheurs :**

- Push sur les branches `develop` ou `dev`
- Pull Request vers `main` ou `develop`

**Étapes :**

- 🔍 Vérification des clés de traduction avec `check_translations.py`
- 🔨 Compilation du projet Maven
- 📦 Test de packaging
- 📤 Upload des artefacts de build
- 📢 Notifications Discord

### 2. **Release - Build et Package** (`.gitea/workflows/release.yml`)

**Déclencheurs :**

- Push de tags commençant par `v*` (ex: `v1.2.3`)
- Push de tags commençant par `release-*` (ex: `release-1.2.3`)

**Étapes :**

- 🏷️ Extraction automatique de la version depuis le tag
- 🔍 Vérification des traductions
- 📦 Build du package final
- 🏷️ Renommage du JAR avec la version
- 🔐 Création du checksum SHA256
- 📤 Upload des artefacts de release
- 📢 Notifications Discord

### 3. **Notifications Discord** (`.gitea/workflows/notifications.yml`)

**Déclencheurs :**

- Tous les événements du repository (push, PR, issues, releases)

**Fonctionnalités :**

- 📝 Notifications pour chaque type d'événement
- 🎨 Embeds Discord colorés selon le type d'événement
- 📊 Informations détaillées (auteur, branche, commit, etc.)

## 🔧 Configuration Requise

### Secrets Gitea

Configurez les secrets suivants dans votre repository Gitea :

```bash
DISCORD_WEBHOOK_URL=https://discord.com/api/webhooks/YOUR_WEBHOOK_ID/YOUR_WEBHOOK_TOKEN
```

### Variables d'Environnement

Les workflows utilisent ces variables par défaut :

- `PROJECT_NAME`: bluemap-banner-point
- `JAVA_VERSION`: 17

## 📋 Utilisation

### Développement

1. **Créer une branche de développement :**

   ```bash
   git checkout -b develop
   ```

2. **Développer et pousser :**

   ```bash
   git add .
   git commit -m "feat: nouvelle fonctionnalité"
   git push origin develop
   ```

3. **La CI se déclenche automatiquement** avec :
   - Compilation
   - Vérification des traductions
   - Notifications Discord

### Release

1. **Créer un tag de version :**

   ```bash
   git tag v1.2.3
   git push origin v1.2.3
   ```

2. **Le workflow de release se déclenche** et :
   - Extrait la version `1.2.3` du tag
   - Crée le package `bluemap-banner-point-1.2.3.jar`
   - Génère le checksum
   - Notifie Discord

## 📦 Artefacts Générés

### Build de Développement

- `build-artifacts/` : JAR et classes compilées
- Rétention : 7 jours

### Release

- `release-artifacts/` : JAR avec version et JAR latest
- `release-checksums/` : Checksum SHA256
- Rétention : 90 jours

## 🔍 Vérifications Automatiques

### Traductions

Le script `check_translations.py` vérifie :

- ✅ Toutes les clés dans les fichiers YAML sont utilisées dans le code Java
- ✅ Toutes les clés utilisées dans le code Java existent dans les fichiers YAML
- ✅ Cohérence entre les fichiers `fr.yml` et `en.yml`

### Qualité

- 📦 Intégrité du packaging

## 📢 Notifications Discord

### Types de Notifications

- 🚀 **Début de build** : Bleu
- ✅ **Succès** : Vert
- ❌ **Échec** : Rouge
- 🔀 **Pull Request** : Bleu
- 🐛 **Issues** : Rouge/Orange
- 🏷️ **Tags/Releases** : Orange

### Informations Incluses

- Nom du projet
- Branche/Tag
- Auteur
- Commit SHA
- Message de commit
- Statut du job

## 🛠️ Personnalisation

### Ajouter de nouveaux tests

1. Modifiez le workflow `ci-develop.yml`
2. Ajoutez une nouvelle étape dans le job `build-and-test`

### Modifier les notifications

1. Éditez le workflow `notifications.yml`
2. Personnalisez les embeds Discord selon vos besoins

## 🔄 Workflow de Branches

```
develop ──→ main (via Pull Request)
   │
   └──→ v1.2.3 (tag pour release)
```

### Branches Principales

- **`main`** : Code stable, suit les releases
- **`develop`** : Branche de développement
- **`dev`** : Branche de développement alternative

### Tags de Version

- **`v1.2.3`** : Version sémantique
- **`release-1.2.3`** : Version alternative

## 📈 Métriques et Monitoring

### Métriques Disponibles

- Temps de build
- Fréquence des releases

### Logs et Debugging

- Logs complets dans l'interface Gitea Actions
- Artefacts téléchargeables

## 🚀 Bonnes Pratiques

1. **Commits** : Utilisez des messages conventionnels
2. **Branches** : Développez sur `develop`, mergez vers `main`
3. **Tags** : Utilisez le format `vX.Y.Z` pour les releases

## 🔧 Dépannage

### Problèmes Courants

1. **Échec de compilation** : Vérifiez la syntaxe Java
2. **Traductions manquantes** : Exécutez `check_translations.py` localement

### Support

Pour toute question sur la CI/CD, consultez :

- Les logs des workflows dans Gitea
- Ce document de documentation
- Les rapports d'artefacts générés

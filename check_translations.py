#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script de vérification des clés de traduction
Vérifie que toutes les clés dans les fichiers de traduction sont utilisées et vice versa
"""

import os
import re

def extract_keys_from_yaml(file_path):
    """Extrait toutes les clés d'un fichier YAML de manière basique"""
    keys = set()

    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()

        current_path = []
        in_multiline = False

        for line_num, line in enumerate(lines, 1):
            original_line = line
            line = line.rstrip()

            # Ignorer les commentaires et les lignes vides
            if line.startswith('#') or not line:
                continue

            # Gérer les valeurs multilignes (commençant par |)
            if line.strip().startswith('|'):
                in_multiline = True
                continue

            # Si on est dans une valeur multiligne, ignorer jusqu'à la fin
            if in_multiline:
                if not line.startswith(' ') and not line.startswith('\t'):
                    in_multiline = False
                else:
                    continue

            # Compter l'indentation
            indent = len(original_line) - len(original_line.lstrip())
            level = indent // 2  # Supposons une indentation de 2 espaces

            # Nettoyer le chemin selon le niveau d'indentation
            while len(current_path) > level:
                current_path.pop()

            # Extraire la clé
            if ':' in line:
                key_part = line.split(':')[0].strip()

                # Ignorer les clés qui sont des valeurs HTML
                if key_part.startswith('<'):
                    continue

                current_path.append(key_part)

                # Construire le chemin complet
                full_key = '.'.join(current_path)
                keys.add(full_key)

                # Si c'est une valeur simple (pas un objet), retirer la clé du chemin
                if not line.rstrip().endswith(':'):
                    current_path.pop()

        return keys
    except Exception as e:
        print(f"Erreur lors de la lecture de {file_path}: {e}")
        return set()

def extract_keys_from_java_files(java_dir):
    """Extrait toutes les clés utilisées dans les fichiers Java"""
    keys = set()

    # Pattern pour trouver Messages.get("key") ou Messages.get("key", args)
    pattern = r'Messages\.get\("([^"]+)"'

    for root, dirs, files in os.walk(java_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                try:
                    with open(file_path, 'r', encoding='utf-8') as f:
                        content = f.read()
                        matches = re.findall(pattern, content)
                        keys.update(matches)
                except Exception as e:
                    print(f"Erreur lors de la lecture de {file_path}: {e}")

    return keys

def main():
    # Chemins des fichiers
    base_dir = "."
    lang_dir = os.path.join(base_dir, "src", "main", "resources", "lang")
    java_dir = os.path.join(base_dir, "src", "main", "java")

    # Extraire les clés des fichiers de traduction
    fr_keys = extract_keys_from_yaml(os.path.join(lang_dir, "fr.yml"))
    en_keys = extract_keys_from_yaml(os.path.join(lang_dir, "en.yml"))

    # Extraire les clés utilisées dans le code Java
    java_keys = extract_keys_from_java_files(java_dir)

    print("=== VÉRIFICATION DES CLÉS DE TRADUCTION ===\n")

    # Vérifier la cohérence entre fr.yml et en.yml
    print("1. COHÉRENCE ENTRE LES FICHIERS DE TRADUCTION:")
    fr_only = fr_keys - en_keys
    en_only = en_keys - fr_keys

    if fr_only:
        print(f"  ❌ Clés uniquement dans fr.yml ({len(fr_only)}):")
        for key in sorted(fr_only):
            print(f"    - {key}")
    else:
        print("  ✅ Toutes les clés de fr.yml sont présentes dans en.yml")

    if en_only:
        print(f"  ❌ Clés uniquement dans en.yml ({len(en_only)}):")
        for key in sorted(en_only):
            print(f"    - {key}")
    else:
        print("  ✅ Toutes les clés de en.yml sont présentes dans fr.yml")

    print()

    # Vérifier les clés utilisées dans le code mais manquantes dans les fichiers de traduction
    print("2. CLÉS UTILISÉES DANS LE CODE MAIS MANQUANTES DANS LES FICHIERS DE TRADUCTION:")
    missing_in_fr = java_keys - fr_keys
    missing_in_en = java_keys - en_keys

    if missing_in_fr:
        print(f"  ❌ Clés manquantes dans fr.yml ({len(missing_in_fr)}):")
        for key in sorted(missing_in_fr):
            print(f"    - {key}")
    else:
        print("  ✅ Toutes les clés utilisées dans le code sont présentes dans fr.yml")

    if missing_in_en:
        print(f"  ❌ Clés manquantes dans en.yml ({len(missing_in_en)}):")
        for key in sorted(missing_in_en):
            print(f"    - {key}")
    else:
        print("  ✅ Toutes les clés utilisées dans le code sont présentes dans en.yml")

    print()

    # Vérifier les clés définies mais non utilisées
    print("3. CLÉS DÉFINIES MAIS NON UTILISÉES DANS LE CODE:")
    unused_in_fr = fr_keys - java_keys
    unused_in_en = en_keys - java_keys

    if unused_in_fr:
        print(f"  ⚠️  Clés non utilisées dans fr.yml ({len(unused_in_fr)}):")
        for key in sorted(unused_in_fr):
            print(f"    - {key}")
    else:
        print("  ✅ Toutes les clés de fr.yml sont utilisées dans le code")

    if unused_in_en:
        print(f"  ⚠️  Clés non utilisées dans en.yml ({len(unused_in_en)}):")
        for key in sorted(unused_in_en):
            print(f"    - {key}")
    else:
        print("  ✅ Toutes les clés de en.yml sont utilisées dans le code")

    print()

    # Résumé
    print("4. RÉSUMÉ:")
    print(f"  - Clés dans fr.yml: {len(fr_keys)}")
    print(f"  - Clés dans en.yml: {len(en_keys)}")
    print(f"  - Clés utilisées dans le code: {len(java_keys)}")
    print(f"  - Clés manquantes au total: {len(missing_in_fr) + len(missing_in_en)}")
    print(f"  - Clés non utilisées au total: {len(unused_in_fr) + len(unused_in_en)}")

    # Statut global
    total_issues = len(fr_only) + len(en_only) + len(missing_in_fr) + len(missing_in_en)
    if total_issues == 0:
        print("\n🎉 TOUTES LES CLÉS DE TRADUCTION SONT COHÉRENTES ET COMPLÈTES !")
    else:
        print(f"\n⚠️  {total_issues} problème(s) détecté(s) dans les clés de traduction")

if __name__ == "__main__":
    main()

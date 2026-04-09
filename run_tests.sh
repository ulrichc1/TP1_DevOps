#!/bin/bash

set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "================================================"
echo "  TP1 DevOps - Lancement des tests JUnit + MockMvc"
echo "================================================"
echo ""

echo "[1/3] Nettoyage du build précédent..."
cd "$PROJECT_DIR"
./gradlew clean --quiet
echo "  OK"
echo ""

echo "[2/3] Compilation et execution des tests..."
echo ""
./gradlew test

echo ""
echo "[3/3] Résultats..."
REPORT="$PROJECT_DIR/build/reports/tests/test/index.html"
if [ -f "$REPORT" ]; then
    echo "  Rapport HTML disponible : $REPORT"
    echo "  Pour l'ouvrir : open $REPORT"
fi

echo ""
echo "================================================"
echo "  Tests terminés avec succès !"
echo "================================================"

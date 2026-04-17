# TP1 DevOps

Application Spring Boot CRUD avec une base H2 en mémoire, conteneurisée via Docker.

## Architecture

```
src/main/java/com/ulrich/dockerjavaspringboot/
├── model/          Entités JPA (Produit, Utilisateur)
├── repository/     Interfaces Spring Data JPA
├── service/        Logique métier
├── controller/     Contrôleurs REST (/api/produits, /api/utilisateurs)
│                   Contrôleurs Thymeleaf (/produits, /utilisateurs)
└── config/         Initialisation des données au démarrage
```

## Stack

- Java 17, Spring Boot 3.2
- Base de données H2 en mémoire
- Thymeleaf pour les vues HTML
- Docker pour la conteneurisation

## Tests

```bash
./run_tests.sh
```

3 couches de tests : entités (JUnit), services (Mockito), contrôleurs REST (MockMvc).

## Qualite et couverture

- Couverture JaCoCo activee dans Gradle (rapport HTML + XML) avec seuil minimal de 60% sur les lignes.
- Le controle de couverture est integre a `check`, ce qui fait echouer le pipeline si le seuil n'est pas atteint.
- Rapport local JaCoCo : `build/reports/jacoco/test/html/index.html`.

Commande locale complete :

```bash
./gradlew clean check jacocoTestReport
```

## SonarCloud

Le workflow GitHub Actions lance une analyse SonarCloud sur chaque `push` vers `main/master`.

Variables et secrets requis dans GitHub :

- `SONAR_TOKEN` (secret)

Configuration Sonar utilisee :

- `sonar.projectKey`: `ulrichc1_TP1_DevOps`
- `sonar.organization`: `ulrichc1`
- rapport de couverture lu depuis `build/reports/jacoco/test/jacocoTestReport.xml`

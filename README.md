# DataStreaming2022

Projet prêt à l'emploi permettant d'illustrer le talk "Edgar Alan Poe appliqué au data streaming - Toutes sont bonnes ou mauvaises par comparaison".

Il se présente sous la forme d'un projet docker-compose qui lance plusieurs comopsants:
- Une base de données postgres (datareference) contenant un référentiel de données (liste de compteurs)
- Un bus kafka et son zookeeper associé
- L'utilitaire web kafdrop permettant de visionner le contenu du bus kafka
- Apache Nifi contenant 2 process groups :
  - l'un permettant de générer des données
  - l'autre permettant d'implémenter un change data capture
- Un registre Nifi (nifi-registry)
- Un cluster Apache Flink qui implémente un traitement métier simple
- Un Elasticsearch permettant de recueillir les données traitées par Flink
- Kibana pour visualiser le contenu d'Elasticsearch


Pour lancer le projet:
----

```
docker-compose up -d
```
- Ensuite, il faut paramétrer le registre nifi : http://nifi-registry:18080 
- Importer les 2 process groupes de chargement de données et CDC.

Pour arrêter les composants:
----
```
docker-compose stop
```


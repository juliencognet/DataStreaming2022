# DataStreaming2022

Projet prêt à l'emploi permettant d'illustrer le talk "Edgar Alan Poe appliqué au data streaming - Toutes choses sont bonnes ou mauvaises par comparaison" de Jean-Michel DURAND et Julien COGNET.

Il se présente sous la forme d'un projet docker-compose qui lance plusieurs comopsants:
- Une base de données postgres (datareference) contenant un référentiel de données (liste de compteurs)
- Un bus kafka et son zookeeper associé
- L'utilitaire web AKHQ permettant de visionner le contenu des topics kafka
- Apache Nifi contenant 2 process groups :
  - l'un permettant de générer des données
  - l'autre permettant d'implémenter un change data capture
- Un registre Nifi (nifi-registry)
- Un cluster Apache Flink qui implémente un traitement métier simple (jointure du flux de données compteur avec les données de référence) de 2 manières différentes
  - via l'API Java Stream
  - via Flink SQL (API table) 
- 


Pour lancer le projet:
----

```
bin/startup.bat
```

- Il est nécessaire ensuite de déployer le générateur de données et le Change Data Capture dans Nifi.
- Accéder à la page d'administration de Nifi: https://localhost:8443/nifi/
- Ensuite, il faut paramétrer le registre nifi. Pour cela, il faut aller dans le menu de Nifi (en haut à droite), puis sélectionner "Controller settings", puis l'onglet "Registry clients". Ajouter un registry avec le bouton "+", saisir un nom tel que "Nifi Registry" puis dans URL ajouter l'adresse suivante: http://nifi-registry:18080 
- Recharger la page d'administration de Nifi (F5).
- Ajouter un Process Group (4ème icone du menu)
- Sélectionner "Import from registry"
- Choisir la version la plus récente du flow "SampleDataCreator"
- Clic droit sur le process group juste déployé
- Choisir Configure
- Dans l'onglet Controller Services, paramétrer le pool DBCP en cliquant sur l'icone d'administration (engrenages)
- Dans l'onglet Properties du Detail du Controller Service, saisir le mot de passe de l'utilisateur (password)
- Valider puis activer (Enable) le service controlleur.
- Démarrer tous les processeurs 
- Le générateur de données est alors démarré ainsi que la détection de changement.
- Il est alors possible de visualiser les données générées et transformées dans AKHQ (http://localhost:8090/ui/docker-kafka-server/topic)
- Pour faciliter la visulation des données générées, vous pouvez exécuter la commande ci-dessous puis copier les requêtes SQL présentes dans le dossier ksqldb-cli/command_ksql.sql

```
 docker exec -it ksqldb-cli ksql http://ksqldb-server:8088
```

Pour arrêter les composants:
----
```
bin/stop.bat
```

IHM d'administration
----
- Administration Nifi: https://localhost:8443/nifi/
- accès à Nifi Registry: http://localhost:18080/nifi-registry
- Visualisation des topics Kafka: http://localhost:8090/ui/docker-kafka-server/topic 
- Flink: http://localhost:8085/
- Accès à InfluxDB: http://localhost:8086
- Accès à Confluent Control Center: http://localhost:9021


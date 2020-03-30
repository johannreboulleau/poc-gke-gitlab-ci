# POC GCP : App Engine Standard Java 11

Environnement :
* Java 11
* Spring-boot 2.2.11
* SDK GCLOUD
* Free Trial GCP with 300$ for 1 year

Doc principale :
* https://cloud.google.com/appengine/docs/standard/java11/

**Remarque** : des différences entre GAE Standard Java 8, GAE Standard Java 11 et GAE Flexible. 
Attention de ne pas tout confondre.

# Outils 

## SDK GCLOUD

Le SDK permet de gérer tout en ligne de commande : instances, App, BDD, Datastore, deploy, etc

# Features

## Créer un projet GCP

``gcloud projects create poc-demo-johann-asia-south1``

Configurer la facturation.

## Créer une application Ap Engine Standard java 11

``gcloud app create --project=poc-demo-johann-asia-south1``

Attention bien choisir la région. Toutes les fonctionnalités ne sont pas disponible sur tous les datacenters, dont les VPC Connector pour Redis.

## Init du projet Java  avec SpringBoot

1. Init d'un projet Spring Boot : https://start.spring.io/
2. Suivre cette doc : https://cloud.google.com/appengine/docs/standard/java11/quickstart
3. Deploy : ``mvn clean package appengine:deploy``

## Logging SLF4J

1. Dépendances maven
2. Configuration `logback.xml`
3. Endpoint `LoggerRestController`

On retrouve les logs dans les journaux dans l'UI de la console GCP.

## PostgresSQL (JDBC)

1. Dépendances maven
2. Créer une instance PostgresSQL sous GCP
``gcloud sql instances create demo-johann --cpu=1 --memory=3840MiB --database-version=POSTGRES_9_6``
3. Créer les bons rôles aux comptes de services (IAM)
4. Configuration dans `application.yml` et `app.yaml`
5. Endpoint avec un CRUD `PostgresSqlRestController`

Possibilité d'utiliser un ORM.

## Datastore (recommandé par GCP)

Instance Datastore à disposition automatiquement.

1. Dépendances maven
2. Endpoint avec CRUD `DatastoreRestController` 
 
## Objectify

ORM pour utiliser Datastore.

1. Dépendances maven
2. Configuration d'un filter Objectify `ObjectifyFilterServlet`
3. Init du ObjectifyService + register des Entity : `ObjectifyRestController`
4. Endpoint avec CRUD `ObjectifyRestController`

En local : emulateur

Possibilité de mettre un émulateur en local, mais sinon par défaut se connecte directement au remote.

`gcloud components install cloud-datastore-emulator`

`gcloud beta emulators datastore start [flags]
`

## MemoryStore : MemCache / Redis

2 solutions distinctes : MemCache / Redis (plus complet)

### Redis

1. Démarrer et configurer une instance d'API Redis sous GCP.
`gcloud redis instances create redis-demo-johann --size=1 --region=asia-south1  --redis-version=redis_3_2`
2. Créer un Connecteur VPC 
``gcloud compute networks vpc-access connectors create connector-johann-demo --network default --region asia-south1 --range  10.8.0.0/28``
**Attention** : le connector VPC n'est pas utile pour App Engine Standard Java 8
3. Ajouter la configuration du Connector VPC dans app.yaml
4. Dépendance maven
5. Configuration Listener `AppServletContextListener`
6. Endpoint pour tester `RedisController`

## Mail

javax.mail compatible avec API de GCP uniquement pour Java 8

Java 11 : SendGrid, Mailgun, or Mailjet. 

1. Dépendance maven
2. Endpoint `MailRestController`

## Auth

1. Permissions / activation dans la console GCP API et Services
2. Configuration app.yaml

# Local

`mvn spring-boot:run`

ou

`mvn appengine:devserver`
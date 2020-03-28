# POC GCP : App Engine Flex

Pré-requis :
* Java 11
* SDK GCLOUD

# Features

## SDK GCLOUD

Le SDK permet de gérer tout en ligne de commande : instances, App, BDD, Datastore, deploy, etc

## Logging SLF4J

1. dépendances maven
2. configuration `logback.xml`
3. Endpoint `LoggerRestController`

On retrouve les logs dans les journaux dans l'UI de la console GCP.

## Postgres (JDBC)

1. dépendances maven
2. démarrer une instance PostgresSQL sous GCP
3. configuration dans `application.yml` et `app.yaml
4. Endpoint avec un CRUD `PostgresSqlRestController`

Possibilité d'utiliser un ORM.

## Datastore (recommandé par GCP)

1. dépendances maven
2. Endpoint `DatastoreRestController` 
 
## Objectify

ORM pour utiliser Datastore.

1. dependances maven
2. Configuration d'un filter Objectify `ObjectifyFilterServlet`
3. Init du ObjectifyService + register des Entity : `ObjectifyRestController`
4. Endpoint avec CRUD `ObjectifyRestController`

En local : emulateur

Possibilité de mettre un émulateur en local, mais sinon par défaut se connecte directement au remote.

`gcloud components install cloud-datastore-emulator`

`gcloud beta emulators datastore start [flags]
`

## MemoryStore : MemCache / Redis

2 solutions distinctes : MemCache / Redis

### Redis

1. Démarrer et configurer une instance d'API Redis sous GCP.
2. dépendance maven
3. Configuration Listener `AppServletContextListener`
4. Endpoint pour tester ``

## Mail

javax.mail compatible avec API de GCP uniquement pour Java 8

Java 11 : SendGrid, Mailgun, or Mailjet. 

1. dependance maven
2. Endpoint `MailRestController`

## Auth



# Local

`mvn spring-boot:run`
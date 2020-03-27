# POC GCP : App Engine Flex

* Java 11
* SDK GCLOUD

## SDK GCLOUD

permet de gérer tout : instances, App, BDD, Datastore, deploy, etc

## Logging SLF4J

Slf4j : `LoggerRestController`

On retrouve les logs dans les journaux

## Postgres

`PostgresSqlRestController` (JDBC)

Possibilité d'utiliser un ORM.

## Datastore

Recommandé à PostgresSQL et MySQL

`DatastoreRestController` 
 
### Objectify

* dependances maven
* filter Objectify
* init du ObjectifyService + register des Entity

`ObjectifyRestController`

`ObjectifyFilterServlet`

En local : emulateur

`gcloud components install cloud-datastore-emulator`

`gcloud beta emulators datastore start [flags]
`

## MemoryStore : MemCache / Redis

2 solutions distinctes

## Mail

javax.mail compatible avec API de GCP

`MailRestController`

## Auth


# Local

`mvn spring-boot:run`
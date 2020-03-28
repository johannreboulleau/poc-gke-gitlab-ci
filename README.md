# POC GCP : App Engine Flex

* Java 11
* SDK GCLOUD

## SDK GCLOUD

permet de gérer tout : instances, App, BDD, Datastore, deploy, etc

## Logging SLF4J

cf : `LoggerRestController`

On retrouve les logs dans les journaux

## Postgres

cf `PostgresSqlRestController` (JDBC)

Possibilité d'utiliser un ORM.

## Datastore

Recommandé à PostgresSQL et MySQL

cf `DatastoreRestController` 
 
### Objectify

* dependances maven
* filter Objectify
* init du ObjectifyService + register des Entity

cf `ObjectifyRestController` + `ObjectifyFilterServlet`

En local : emulateur

Possibilité de mettre un émulateur en local, mais sinon par défaut se connecte directement au remote.

`gcloud components install cloud-datastore-emulator`

`gcloud beta emulators datastore start [flags]
`

## MemoryStore : MemCache / Redis

2 solutions distinctes

## Mail

javax.mail compatible avec API de GCP uniquement pour Java 8

Java 11 : SendGrid, Mailgun, or Mailjet. 

ex : https://github.com/sendgrid/sendgrid-java

cf `MailRestController`

## Auth



# Local

`mvn spring-boot:run`
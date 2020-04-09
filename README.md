# POC Google Kubernetes Engine (GKE)

Doc principale : https://cloud.google.com/kubernetes-engine/docs

**Pré-requis**

* SDK gcloud
* kubectl

# Features de GKE

## Création d un cluster

`gcloud container clusters create cluster-gke-poc-johann`

## Configuration de Kubectl

`gcloud container clusters get-credentials cluster-gke-poc-johann`

## Création projet SpringBoot

## Création Image Docker

1. Création du fichier `Dockerfile`
4. Activer les API sous GCP https://console.cloud.google.com/flows/enableapi?apiid=cloudbuild.googleapis.com

## Push Image sur Registry Docker GCP
 
1. `sudo gcloud builds submit --tag gcr.io/poc-demo-johann-asia-south1/springboot-poc-gke-docker-image .`
Equivalent à `docker build .` + `docker push`

## Création Deployment

1. Création du fichier de configuration d'un object Kubernetes de type Deployment `deployment.yaml`
2. Application du déploiment dans GKE `kubectl apply -f deployment.yaml`
3. Vérification `kubectl get deployments` , `kubectl get pods`

## Déployer un Service

Les services fournissent un point d'accès unique à un ensemble de pods.

1. Création du fichier de configuration d'un object Kubernetes de type Service `service.yaml`
2. Application du service `kubectl apply -f service.yaml`
3. Vérification `kubectl get services`
4. Accéder à l'application `curl http://34.93.58.136/` 

## Déployer une nouvelle version d'une image

1. Builder et pusher la nouvelle image Docker
2. Puis 
2.1 Soit mettre à jour le fichier `deployment.yaml` avec la nouvelle version,
2.2 soit si la version est latest et le replica = 1, supprimer le pod => un nouveau se créera avec la dernière version
`kubectl get pods` ``

## Volume

1. La configuration se fait dans `deployment.yaml`
2. Configuration `logback.xml` pour écrire dans un fichier de ce volume
3. Vérifier le fichier de logs `kubectl exec -it poc-gke-johann-7c44cb66fc-gzl6l -- /bin/ash` puis `less /logs/poc-gke.log`

## Accès Shell au pod

1. `kubectl exec -it poc-gke-johann-7c44cb66fc-gzl6l -- /bin/ash`

## Cloud DNS

Pour pouvoir utiliser des noms de domaine avec un service ou un équilibreur de requêtes HTTP, il faut configurer :
 - une zone avec un enregistrement qui pointe vers l'adresse IP de notre service/ingress dans Cloud DNS
 - puis mettre à jour les NS (Name Server) du registar de notre nom de domaine

 Documentation : https://cloud.google.com/dns/docs/quickstart#update_your_domain_name_servers

## Ingress NGINX - équilibreur de charge HTTP(S) 

Pour les besoins du POC, nous allons déployer un 2e service.

**Attention** : le **rewrite-target** ne fonctionne pas avec le Ingress par défaut de GKE, utiliser au autre ingress, comme nginx.

1. Création du NGINX Ingress Controller - suivre cette documentation https://cloud.google.com/community/tutorials/nginx-ingress-gke	
2. Création du fichier de configuration d'un object Kubernetes de type Ingress `my-ingress.yaml`
3. Attention : la route racine du/des PODs "/" doit retourner une 200, sinon le pod est considéré comme KO.
4. Application du Ingress `kubectl apply -f my-ingress.yaml`
5. Tester en récupérant l'adresse IP du service nginx-ingress-controller `kubectl get service nginx-ingress-controller`

**Résultats** :
* http://external-ip-of-ingress-controller/ redirige bien vers le service 1
* http://external-ip-of-ingress-controller/v2/ redirige bien vers le service 2

# En pratique avec Gitlab-CI


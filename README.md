# POC Google Kubernetes Engine (GKE)

Ce projet est personnel et n'est pas destiné à une formation ou autres.

Le but est d'explorer et monter en compétences sur Kubernetes et Google Kubernetes Engine (GKE).

Documentation principale : 
* https://cloud.google.com/kubernetes-engine/docs
* https://kubernetes.io/docs/home/

**Pré-requis**

* SDK gcloud
* kubectl
* projet Spring Boot 2
* un compte GCP avec un projet et la facturation activée

**Info** : nous pourrions utiliser un projet Java simple ou une autre techno.

# Features expérimentées

## Création d un cluster

`gcloud container clusters create cluster-gke-poc-johann`

## Configuration de Kubectl

`gcloud container clusters get-credentials cluster-gke-poc-johann`

## Création Image Docker

1. Création du fichier `Dockerfile`
4. Activer les API sous GCP https://console.cloud.google.com/flows/enableapi?apiid=cloudbuild.googleapis.com

## Push Image sur Registry Docker GCP
 
1. `sudo gcloud builds submit --tag gcr.io/poc-demo-johann-asia-south1/springboot-poc-gke-docker-image .`

Equivalent à `docker build .` + `docker push`

## Création d'un object K8S de Deployment

1. Création du fichier de configuration d'un object Kubernetes de type Deployment `deployment.yaml`
2. Application du déploiment dans GKE `kubectl apply -f deployment.yaml`
3. Vérification `kubectl get deployments` , `kubectl get pods`

## Création d'un object K8S de Service. 

Les services fournissent un point d'accès unique à un ensemble de pods.
Il expose un Deployment.

1. Création du fichier de configuration d'un object Kubernetes de type Service `service.yaml`
2. Application du service `kubectl apply -f service.yaml`
3. Vérification `kubectl get services`
4. Accéder à l'application `curl http://ADRESSE_IP/` 

## Déployer une nouvelle version d'une image

1. Builder et pusher la nouvelle image Docker
2. Puis :
    * soit mettre à jour le fichier `deployment.yaml` avec la nouvelle version
    * soit si la version est latest et le replica = 1, supprimer le pod => un nouveau se créera avec la dernière version
    `kubectl get pods` et `kubectl delete pods name`

## Volume

1. La configuration se fait dans `deployment.yaml`
2. Configuration `logback.xml` pour écrire dans un fichier de ce volume
3. Vérifier le fichier de logs `kubectl exec -it POD_NAME -- /bin/ash` puis `less /logs/poc-gke.log`
ou `kubectl logs POD_NAME`

## Accès Shell au pod

1. `kubectl exec -it POD_NAME -- /bin/ash`

## Describe POD (lifecycle)

1. `kubectl describe pods POD_NAME`


## Cloud DNS

Pour pouvoir utiliser des noms de domaine avec un service ou un équilibreur de requêtes HTTP (Ingress), il faut configurer :
 - une zone avec un enregistrement qui pointe vers l'adresse IP de notre Service/Ingress dans Cloud DNS
 - puis mettre à jour les NS (Name Server) du registar de notre nom de domaine

 Documentation : https://cloud.google.com/dns/docs/quickstart#update_your_domain_name_servers

## Ingress NGINX - équilibreur de charge HTTP(S) 

**Info :** Pour les besoins du POC, nous allons déployer un 2e service.

**Attention** : le **rewrite-target** ne fonctionne pas avec le Ingress par défaut de GKE, c'est pour cela que j'utilise Nginx.
De plus pour le besoin d'une CI, il est préférable d'utiliser l'architecture de Ingress Nginx (cf doc https://cloud.google.com/community/tutorials/nginx-ingress-gke	).

1. Création du NGINX Ingress Controller - suivre cette documentation https://cloud.google.com/community/tutorials/nginx-ingress-gke	
2. Création du fichier de configuration d'un object Kubernetes de type Ingress `ingress.yaml`
3. Attention : la route "/" de chaque Service doit retourner une 200, sinon le pod est considéré comme KO.
4. Application du Ingress `kubectl apply -f ingress.yaml`
5. Tester en récupérant l'adresse IP du service nginx-ingress-controller `kubectl get service nginx-ingress-controller`

**Résultats** :
* http://external-ip-of-ingress-controller/ redirige bien vers le service 1
* http://external-ip-of-ingress-controller/v2/ redirige bien vers le service 2

## Kubectl avec plusieurs cluster

Possibilité d'avoir plusieurs configurations de cluster. Voici quelques commandes utiles :

* kubectl config get-contexts
* kubectl config use-context gke_gitlab-project-273605_asia-south1-a_cluster-1
* kubectl config set-context default --cluster=cluster-1 --user=cluster-admin
* Voir les secrets : kubectl describe secrets
* Configurer un nouveau cluster : kubectl config set-cluster cluster-1  --server="https://35.244.6.171" --insecure-skip-tls-verify=true
* Configurer un nouveau credential : kubectl config set-credentials cluster-admin --username="xxx" --password="xxx"
* etc

# En pratique avec Gitlab-CI

## Objectifs 

Le but est de mettre en pratique les concepts ci-dessus en mettant en place une CI/CD pour des développeur. 
A chaque nouvelle branche pushée, la CI/CD doit :
 - build d'une image docker
 - push sur le Registry de GCP (gcr.io)
 - déployer sur GCP (objet K8S de type Deployment + Service)
 - accéder à la version de l'application deployée via une route unique (objet K8S de type Ingress)
 
Manuellement, le job proposera aussi de détruire l'environnement (Ingress, Service, Deployment, Pods). 

J'ai suivi ce tutoriel https://medium.com/@davivc/how-to-set-up-gitlab-ci-cd-with-google-cloud-container-registry-and-kubernetes-fa88ab7b1295

Toutes les étapes ci-dessous se trouvent dans ce tutoriel.

## Pré-requis
 
* créer un projet Gitlab
    * j'ai voulu utiliser le Gitlab de la marketplace de GKE, mais souci de certificat chronophage à résoudre
    * du coup, j'ai créer un projet sur www.gitlab.com
* Projet Spring Boot 2

## Etapes

1. GCP : création/configuration du cluster pour accéder à l'API K8S de l'extérieur 
2. GCP : création d'un compte de Service avec les rôles : Lecture sur tout + Editor de Cloud Build + Admin sur GCS (storage, registry)
3. Gitlab : ajouter le cluster GCP
4. Gitlab : installer les applications Helm Tiller, Ingress, GitlabRunner (Gitlab va installer un équilibreur de charge HTTP Ingress NGINX sur notre cluster)
5. Création des variables pour la CI/CD (cf tutoriel)
6. Création des 3 fichiers suivants : `gitlab-deployment.yaml`, `gitlab-service.yaml` et `gitlab-ingress.yaml`
7. Création de `gitlab-ci.yml` avec 3 jobs : build + deploy dans GKE + Remove All
8. Dans Cloud DNS, ajouter l'entrée : le nom `*.nomdedomaine.fr` doit pointer vers l'IP du Controller de l'Ingress NGINX. C'est l'Ingress qui va se charger de router les sous-domaines.

**Autre documentation utile :**
* https://docs.gitlab.com/ee/ci/variables/predefined_variables.html

## Résultat :

Gitlab :
![Pipeline gitlab](images/result-gitlab-ci.png)

L'application déployée avec une URL propre :
![Application](images/result-appli.png)

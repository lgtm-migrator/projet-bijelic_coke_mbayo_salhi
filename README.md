# Introduction

Static est un générateur de site statique. Il permet de générer des pages au
format html à partir de fichiers Markdown. Cela permet d'éditer facilement et
rapidement du contenu grâce à la syntaxe simple du format Markdown sans avoir
besoin de connaître le HTML.

## Construit avec
- Maven
- Picocli

## Auteurs
- Alen Bijelic
- Anthony Coke
- Guilain Mbayo
- Mehdi Salhi

## Prérequis
- Installation de Maven
- Java version 11


# Mise en place

Cloner le [dépôt GitHub](https://github.com/dil-classroom/projet-bijelic_coke_mbayo_salhi).
```
git clone https://github.com/dil-classroom/projet-bijelic_coke_mbayo_salhi.git
```
Se rendre dans le répertoire du projet.

```
cd projet-bijelic_coke_mbayo_salhi 
```
Exécuter les commandes suivantes :

```
mvn clean install
unzip -o target/static.zip

export PATH=$PATH:`pwd`/static/bin
```

Une fois cela fait, l'application peut être utilisée grâce aux commandes dans la section suivante.

# Liste des commandes

## Init

Initialise l'arborescence d'un nouveau site avec des fichiers markdown et json
d'exemple.


```
static init mon/site 
```

Deux fichiers `config.json` et `index.md` seront créés automatiquement par le programme. Les valeurs du fichier json peuvent être modifiées. Dans le fichier markdown, les valeurs des métadonnées ainsi que le cod
e markdown sont également modifiables.

## Build
Construit le site web à partir des fichiers de configuration et des pages
markdown.


```
static build mon/site
```

Crée un dossier `build` contenant le site généré.

### Paramètres

```                                                                             
--watch                                                                         
```                                                                             

```                                                                             
-w                                                                              
```                                                                             

Permet de régénérer le site à la volée lorsque des changements sont 
effectués dans le système de fichiers.

## Serve

Crée un serveur web local et héberge le site statique pour pouvoir le tester.
(Disponible à l'adresse localhost:7070).

```
static serve mon/site
```

### Paramètres

```
--watch
```

```
-w
```

Permet de régénérer le site à la volée lorsque des changements sont effectués
dans le système de fichiers.

## Clean
Efface le répertoire `build` du site

```
static clean mon/site
```

## Help
Affiche l'aide et la liste des commandes

```
static help
```

## Publish

Note: cette commande n'est pas fonctionnelle.

Publie un site sur un repository GitHub distant

```
static publish mon/site
```

## Version
Affiche la version du programme

```
static --version
```

ou

```
static -V
```

## New
Affiche "New" dans la console

```
static new
```

# Utilisation typique

1. Initialiser le dossier actuel pour créer un site modèle

```
static init mon/site
```

2. Modifier les pages suivantes selon les paramètres voulus
   - mon/site/config.json
   - mon/site/index.md

3. Ajouter des pages au format Markdown (.md) en prenant comme modèle la page
   index.md

4. Construire le site avec la commande build

```
static build mon/site
```

Un répertoire `build` se crée dans le dossier mon/site. Il contient le site web
convertit au format HTML. Ce répertoire peut être upload sur un serveur web.


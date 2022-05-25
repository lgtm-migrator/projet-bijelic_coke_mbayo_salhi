# projet-bijelic_coke_mbayo_salhi
## Authors
- Alen Bijelic
- Anthony Coke
- Guilain Mbayo
- Mehdi Salhi

## About the project

This project consists in the creation of a static website generator. It is given within the lecture DIL in HEIG-VD.

## Built with
- Maven
- Picocli

## Getting started

### Prerequisites

### Installation

### Usage

## Contact

## Acknowledgments

# User Manual

## Build
Construit le site web à partir des fichiers de configuration et des pages
markdown.


```
statique build
```

## Clean
## Help
Affiche l'aide et la liste des commandes

Utilisation:

```
statique help
```

## Init

Initialise l'arborescence d'un nouveau site avec des fichier markdown d'exemple.


```
statique init /mon/site
```

## New
## Publish

Publie un site sur un repository GitHub distant


```
statique publish
```

## Serve
## Version
Affichage la version du programme

```
statique --version
```

# Utilisation typique

1. Initialiser le dossier actuel pour créer un site modèle

```
statique init /mon/site
```

2. Modifier les pages suivantes selon les paramètres voulus
    - /mon/site/config.yaml
    - /mon/site/index.md

3. Ajouter des pages au format Markdown (.md) en prenant comme modèle la page
   index.md

4. Construire le site avec la commande build

```
statique build /mon/site
```

Un répertoire "build" se crée dans le dossier /mon/site. Il contient le site web
convertit au format HTML. Ce répertoire peut être upload sur un serveur web.

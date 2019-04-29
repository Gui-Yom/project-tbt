# project-rpg
Un petit tactical rpg dans la console.

#### Inspirations
Librement inspiré de :
- Final Fantasy Tactics
- Fire Emblem
- Dofus
- Dragon Warrior I-IV NES

### Cahier des charges
Aucune idée de ce qu'il faut mettre ici ...

### Cadre du projet
Nous avons choisi le 5ème problème, le jeu de plateau, où l'objectif 
est de simuler un combat entre deux entités.

La rédaction du cahier des charges, la planification du développement 
et le développent lui-même s'étaleront sur une période d'un mois, 
du 13 Avril au 13 Mai 2019, date de rendu.

## Principe du jeu
Le jeu proposé s'appuie sur des mécaniques tirées de Tactical-RPG et 
de combats au tour-par-tour.

Sur une grande grille, chaque joueur contrôle le déplacement de ses 
personnages: à chaque tour, un joueur prend le contrôle de l'un de ses 
personnages (dans un ordre choisi avant la partie), 
et réalise l'une des actions suivantes:

- Se déplacer
- Se déplacer et attaquer un personnage adverse
- Se protéger

Lorsqu'un combat est enclenché, un deuxième aspect du jeu 
apparaît: le mode duel. Les deux participants au duel s'engagent 
dans un combat qui ne peut avoir que deux issues: la défaite ou la 
retraite d'un des deux personnages.

Gagne le joueur qui parvient à vaincre en premier tous les 
personnages contrôlés par son adversaire.

La carte du jeu sera choisie au préalable parmi celles prédéfinies.
Le joueur aura aussi la possibilité de ramasser des items au sol afin
de lui donner un avantage.

Un mode Joueur vs IA est envisagé.

---

### Répartition du travail
C ambigüe mais ça passe

### Planification
oklm

### Organisation du code
La classe principale est `lorganisation.projectrpg.Game`. 
`lorganisation.projectrpg.map.LevelMap` représente une carte du jeu.
Tout le rendu visuel est effectué dans `lorganisation.projectrpg.TerminalGameRenderer`.
Le diagramme UML donne toutes les informations quand aux relations 
entre les classes.

### Détails techniques
Nous nous limiterons à l'interface de commande.

Nous utiliserons la bibliothèque [JLine3](https://github.com/jline/jline3)
qui, sans rentrer dans les détails, nous permettra de gérer la console
dans les détails, comme la couleur ou la position du curseur.

Nous utiliserons aussi la bibliothèque [Gson](https://github.com/google/gson)
afin de faciliter la lecture des fichiers JSON utilisés pour la
configuration.

---

### Compilation
Ce projet utilise [Gradle](https://gradle.org/ "Site web de Gradle") 
pour gérer toutes les tâches relatives à la compilation et aux librairies. 
Par conséquent, il est nécessaire d'avoir la dernière version de gradle 
installée ou d'utiliser le gradle wrapper fourni.

Test du jeu :
```bash
$ gradlew play
```

Création du jar exécutable du jeu et des scripts de lancement :
```bash
$ gradlew shadowJar copyScripts
```

### Note sur la compatibilité
Sera compatible tout système supporté par JLine3, ce qui inclus :
 - Windows
 - Linux
 - OS X
 - Solaris
 - FreeBSD

D'une manière générale, tout système doté d'une console supportant les 
caractères de contrôle ANSI (VT100) est compatible.

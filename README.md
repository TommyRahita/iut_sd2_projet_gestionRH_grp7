# iut_sd2_projet_gestionRH

## 📁 Structure du projet
- **`src/JavaProject/`** : Contient les fichiers sources Java.
- **`bin/JavaProject/`** : Contient les fichiers compilés.
- **`doc/`** : Documentation générée du projet.
- **`resources/`** : Contient les fichiers de données (CSV, icônes, etc.).
- **`.git/`** : Répertoire de gestion de versions Git.

## 📦 Librairies utilisées
Les bibliothèques externes sont gérées directement dans Eclipse. Elles incluent :
- **iText7 (version 7.2.5)** : Génération et manipulation de fichiers PDF. [Lien 7.2.5](https://github.com/itext/itext-java/releases/tag/7.2.5) | [Lien 7.2.6](https://github.com/itext/itext-java/releases/tag/7.2.6)
- **SLF4J (version 2.0.7)** : API de logging. [Lien API 2.0.7](https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.7/) | [Lien Simple 1.7.36](https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/)

## 🔧 Installation et exécution
### Installation
1. **Télécharger et installer Eclipse** si ce n'est pas déjà fait : [Télécharger Eclipse](https://www.eclipse.org/downloads/).
2. **Cloner le dépôt Git** (si applicable) ou extraire le projet dans un dossier de votre choix.
3. **Ouvrir Eclipse** et aller dans `File` > `Import`.
4. Sélectionner `Existing Projects into Workspace` puis cliquer sur `Next`.
5. Choisir le dossier du projet puis cliquer sur `Finish`.
6. Vérifier que toutes les bibliothèques externes sont bien ajoutées dans le classpath (`Build Path` > `Configure Build Path`).

### Exécution
1. Aller dans `src/JavaProject/`.
2. Trouver la classe `InterfaceConnexion.java`.
3. Faire un clic droit sur la classe et sélectionner `Run As` > `Java Application`.
4. L'application démarre et affiche l'interface de connexion.

## 👥 Contributeurs
- **Tommy RAHITA**
- **Yanis THOLLET**
- **Anthony SON**


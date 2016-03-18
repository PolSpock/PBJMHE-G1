# PBJMHE-G1

Pour l'importation du projet Android, il faut réaliser au préalable ces commandes :

- Il faut modifier le fichier : android/ProjetTwitter/app/build.gradle
et supprimer les lignes suivantes (lignes 26-27-28) :

    compile files('C:/Users/Paul/AndroidStudioProjects/ProjetTwitter/libs/commons-codec-1.10.jar')
    compile files('C:/Users/Paul/AndroidStudioProjects/ProjetTwitter/libs/httpclient-4.0-beta2.jar')
    compile files('C:/Users/Paul/AndroidStudioProjects/ProjetTwitter/libs/httpcore-4.1.jar')
	
- Une fois le projet importé sur Android Studio, il faut changer la vue du projet : Android à Projet.
- Une fois cela effectué, vous êtes normalement sur la vue de la racine du projet.
Il faut étendre la vue du projet en cliquant sur le nom du projet (ProjetTwitter)
- Le dossier "libs" doit donc être visible (il est accompagné gradle, build, app, .idea, .gradle)
- Il faut étendre le dossier "libs" afin de voir les .jar :
	commons-codec-1.10.jar
	httpclient-4.0-beta2.jar
	httpcore-4-1.jar
- Sur chacun de ces fichiers il faut effectuer un clic droit et faire :
"Add as Library"
// On importe les extensions qui serviront pour les sauvegardes et les affichages fixes
import extensions.File;
import extensions.CSVFile;

class LesDesDuSavoir extends Program{
    char joueur = 'J';
    char chemin = '_';
    char epreuve = '?';

    String couleur_epreuve = "red";
    String couleur_base = "white";

    final int TAILLE_TABLEAU = 152;
    final int LARGEUR_CLASSEMENT = 7; // Affichage du TOP 7

    final double PROBA_EPREUVE = 0.33; // Chance qu'une épreuve soit générée
    double PROBA_BONUS = 0.0;

    
    final String FIXAGE = "../ressources/fixage.txt";
    final String TITLE = "../ressources/savoir.txt";
    final String MAIN_MENU = "../ressources/menu.txt";
    final String MENU_DIFFICULTE = "../ressources/difficulte.txt";
    final String MENU_JOUEUR = "../ressources/creerjoueurmenu.txt";
    final String MENU_INVENTAIRE = "../ressources/inventaire.txt";
    final String MENU_QUITTER = "../ressources/quitter_partie.txt";
    final String MENU_STATISTIQUES = "../ressources/statistiques.txt";
    final String MENU_CLASSEMENT = "../ressources/classement.txt";
    final String MENU_PARAMETRE = "../ressources/parametre.txt";
    final String MENU_COULEUR = "../ressources/couleurs.txt";
    final String MENU_REGLE_1 = "../ressources/regles.txt";
    final String MENU_REGLE_2 = "../ressources/regles2.txt";
    final String DEFAITE = "../ressources/defaite.txt";

    final String SAVE_PROFIL = "../ressources/save_profil.csv";
    final String QUESTION_EPREUVE = "../ressources/list_question.csv";

    final String[] NOM_DIFFICULTE = new String[]{"FACILE", "MOYEN", "DIFFICLE"};

    // Génère un lancé de dé à 6 faces
    int movement(){
        return (int) ((random()*6) +1);
    }

    // Crée un plateau de cases pouvant être une épreuve, un chemin où le joueur
    Cases[]creerPlateau(int taille , int case_actuelle){
        Cases [] plateau = new Cases[taille];

        for (int i = 0 ; i<taille; i++){
            if (random()<PROBA_EPREUVE && i>6){
                plateau[i]=Cases.EPREUVE;
            }else{
                plateau[i]=Cases.CHEMIN;
            }
        }

        plateau[case_actuelle] = Cases.JOUEUR;

        return plateau;
    }
    
    // Renvoie l'affichage en caractère de la case choisie
    char casesToChar(Cases cases){
            if(cases==Cases.JOUEUR){
                return joueur;
            }
            else if(cases==Cases.EPREUVE){
                return epreuve;
            }
            else{
                return chemin;
            }
    }
    
    // Transforme un plateau de cases en chaîne de caractère à afficher
    String toString(Cases[] plateau){
        String res = "";
        int len = length(plateau);

        for(int i = 0;i<len ;i++){
            res += casesToChar(plateau[i]);
        }

        return res;
    }
    
    // Déplace le joueur sur la case suivante
    void deplacerJoueur(Cases[] plateau , int case_actuelle, int prochaine){
        Cases tmp = plateau[prochaine];

        println(plateau[case_actuelle]);
        println(tmp);
        plateau[prochaine] = Cases.JOUEUR;
        plateau[case_actuelle] = tmp;
    }
    
    // Génère la case suivante a partir du lancer de dé et de la position du joueur, si il arrive au bout du plateau le replace au début
    int prochaineCase(int case_actuelle , int mouv){
        if (case_actuelle+mouv>=TAILLE_TABLEAU){
            return case_actuelle+mouv-TAILLE_TABLEAU;
        }
        else{
            return case_actuelle+mouv;
        }
    }

    // Joue le tour en forçant le joueur à appuyer sur entrée pour continuer, il peut également entrer "inventaire" ou "quitter"
    String jouerTour(Cases[] plateau, int case_actuelle, int prochaine){
        println("Veuillez lancer le dé en appuyant sur \"Entrée\", entrer \"quitter\" pour quitter et \"inventaire\" pour accéder à votre inventaire ");

        String res = readString();
        int resultat = 0;

        if(equals(res ,"quitter") || equals(res,"inventaire")){
            deplacerJoueur(plateau , case_actuelle , case_actuelle);
            return res;
        }
        else{
            deplacerJoueur(plateau , case_actuelle , prochaine);
            return "";
        }
    }
    
    // Tire aléatoirement une épreuve, la pose et renvoie si la réponse est valide
    boolean doEpreuve(int difficulte){
        double proba = random();

        if(proba < 0.4){
            return epreuveMath(difficulte);
        }
        else{
            return epreuveQuestion(difficulte);
        }
    }
    
    // Génère une épreuve de mathématique, puis la pose au joueur et renvoie si la réponse est valide
    boolean epreuveMath(int difficulte){
        int nb1 = 0,
            nb2 = 0,
            res = 0, // Résultat de la question
            reponse = 0; // Réponse du joueur

        double proba = random();

        if (difficulte == 3){
            nb1 = (int) (random()*1000);
            nb2 = (int) (random()*1000);
        }
        else if (difficulte == 2){
            nb1 = (int) (random()*100);
            nb2 = (int) (random()*100);
        }
        else {
            nb1 = (int) (random()*10);
            nb2 = (int) (random()*10);     
        }

        if(proba<0.25){
            text(couleur_epreuve);
            println("           "+nb1+"/"+(nb2 + 1));
            text(couleur_base);

            res = (nb1/(nb2+1));
        }
        else if (proba<0.5){
            text(couleur_epreuve);
            println("           "+nb1+"*"+nb2);
            text(couleur_base);

            res = (nb1*nb2);
        }
        else if (proba<0.75){
            text(couleur_epreuve);
            println("           "+nb1+"+"+nb2);
            text(couleur_base);

            res = (nb1+nb2);
        }
        else{
            text(couleur_epreuve);
            println("           "+nb1+"-"+nb2);
            text(couleur_base);

            res = (nb1-nb2);
        }

        println();
        print("Entrez une réponse : ");
        reponse = readInt();
        println();

        if(!resultatEstCorrect(res , reponse)){
            println("Mauvaise réponse, c'était " + res);

            return false;
        }
        else{
            return true;
        }
    }

    // Génère une épreuve de type question, la pose puis renvoie le résultat
    boolean epreuveQuestion(int difficulte){
        CSVFile question = loadCSV(QUESTION_EPREUVE);

        int nbL = rowCount(question),
            nbCol = columnCount(question),
            choixQ = (int)(random() * (nbL-1) + 1),
            choixR = 0,
            idx = 0,
            res = 0,
            reponse = 0;

        int[] tabInit = new int[]{1,2,3,4,5};

        // Affiche la question
        text(couleur_epreuve);
        println("           " + getCell(question, choixQ, 0) );

        if(difficulte == 3){ // DIFFICILE
            int[] tabMel = new int[]{-1,-1,-1,-1,-1};

            // Mélange aléatoirement les réponses
            for(int i=0; i<length(tabMel); i++){
                choixR = (int) (random() * length(tabMel));

                while(tabMel[choixR] != -1){
                    choixR = (int) (random() * length(tabMel));
                }
                
                if(i == 0){
                    idx = choixR;
                }
                tabMel[choixR] = tabInit[i];
            }

            // Affiche les différentes réponses possible
            for(int i=0; i<5; i++){
                println("           " + (i+1) + " - " + getCell(question, choixQ, tabMel[i]) );
            }
            text(couleur_base);

            res = idx+1;
        }
        else if(difficulte == 2){ // MOYEN
            int[] tabMel = new int[]{-1,-1,-1,-1};

            // Mélange aléatoirement les réponses
            for(int i=0; i<length(tabMel); i++){
                choixR = (int) (random() * length(tabMel));

                while(tabMel[choixR] != -1){
                    choixR = (int) (random() * length(tabMel));
                }

                if(i == 0){
                    idx = choixR;
                }
                tabMel[choixR] = tabInit[i];
            }

            // Affiche les différentes réponses possible
            for(int i=0; i<4; i++){
                println("           " + (i+1) + " - " + getCell(question, choixQ, tabMel[i]) );
            }
            text(couleur_base);

            res = idx+1;
        }
        else{ // FACILE
            int[] tabMel = new int[]{-1,-1,-1};

            // Mélange aléatoirement les réponses
            for(int i=0; i<length(tabMel); i++){
                choixR = (int) (random() * length(tabMel));

                while(tabMel[choixR] != -1){
                    choixR = (int) (random() * length(tabMel));
                }

                if(i == 0){
                    idx = choixR;
                }
                tabMel[choixR] = tabInit[i];
            }

            // Affiche les différentes réponses possible
            for(int i=0; i<3; i++){
                println("           " + (i+1) + " - " + getCell(question, choixQ, tabMel[i]) );
            }
            text(couleur_base);

            res = idx+1;
        }

        println();
        print("Entrez une réponse : ");
        reponse = readInt();
        println();

        if (!resultatEstCorrect(res , reponse)){
            println("Mauvaise réponse, c'était " + getCell(question, choixQ, 1));

            return false;
        }
        else{
            return true;
        }
    }

    // Choisit un multiplieur en fonction de la difficulté
    int scoreMultiplieur(int difficulte){
        if(difficulte == 3){ // DIFFICILE
            return 9;
        }
        else if(difficulte == 2){ // MOYEN
            return 3;
        }
        else{ // FACILE
            return 1;
        }
    }
    
    // Sert à creer un nouveau profil pour agrémenter la base de données
    Profil creerProfil(String pseudo, int score, int nb_tours){
        Profil p = new Profil();
        p.pseudo = pseudo;

        p.nb_partie = 1;
        p.partie_facile = 0;
        p.partie_moyen = 0;
        p.partie_difficile = 0;

        p.total_score = score;
        p.best_score = score;

        p.total_tour_joues = nb_tours;
        p.best_tour_joues = nb_tours;

        return p;
    }

    // Affiche un fichier texte à partir du chemin mis en paramètre
    void afficherText(String fichier){
        File f = newFile(fichier);

        while(ready(f)){
            println(readLine(f));
        }
    }

    // Affiche la liste des joueur à partir du fichier csv "save_profil.csv"
    void afficherListeJoueur(){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);
        int nbL = rowCount(profilJoueur);

        for(int l=1; l<nbL; l++){
            String cell = getCell(profilJoueur, l, 0);
            print("            " + (l) + " : " + cell);

            println();
        }
    }

    // Sert à afficher un joueur à partir d'un choix rentré dans un menu
    String getJoueur(int choixJoueur){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        return getCell(profilJoueur, choixJoueur, 0);
    }

    // Sauvegarde un profil sur le fichier csv "save_profil.csv"
    void saveProfil(int choixProfil, String pseudo, int difficulte, int score, int nb_tours){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        int nbL = rowCount(profilJoueur),
            nbCol = columnCount(profilJoueur);

        Profil p = creerProfil(pseudo, score, nb_tours);

        // Créer un profil
        if(choixProfil == 1){
            String[][] chaines = new String[nbL+1][nbCol];

            for(int l=0; l<nbL; l++){
                for(int col=0; col<nbCol; col++){
                    chaines[l][col] = getCell(profilJoueur, l, col);
                } 
            }

            if(difficulte == 1){ // FACILE
                p.partie_facile = 1;
            }
            else if(difficulte == 2){ // MOYEN
                p.partie_moyen = 1;
            }
            else if(difficulte == 3){ // DIFFICILE
                p.partie_difficile = 1;
            }

            chaines[nbL][0] = p.pseudo;

            chaines[nbL][1] = p.nb_partie + "";
            chaines[nbL][2] = p.partie_facile + "";
            chaines[nbL][3] = p.partie_moyen + "";
            chaines[nbL][4] = p.partie_difficile + "";

            chaines[nbL][5] = p.total_score + "";
            chaines[nbL][6] = p.best_score + "";

            chaines[nbL][7] = p.total_tour_joues + "";
            chaines[nbL][8] = p.best_tour_joues + "";

            saveCSV(chaines, SAVE_PROFIL);
        }

        // Sauvegarde sur un profil existant
        else if(choixProfil == 2){
            String[][] chaines = new String[nbL][nbCol];

            for(int l=0; l<nbL; l++){
                
                if(equals(getCell(profilJoueur, l, 0), pseudo) ){
                    p.pseudo = pseudo;

                    p.nb_partie = 1 + stringToInt(getCell(profilJoueur, l, 1) );
                    if(difficulte == 1){
                        p.partie_facile = 1 + stringToInt(getCell(profilJoueur, l, 2) );
                    }
                    else if(difficulte == 2){
                        p.partie_moyen = 1 + stringToInt(getCell(profilJoueur, l, 3) );
                    }
                    else if(difficulte == 3){
                        p.partie_difficile = 1 + stringToInt(getCell(profilJoueur, 1, 4) );
                    }

                    p.total_score = score + stringToInt(getCell(profilJoueur, l, 5) );
                    if(stringToInt(getCell(profilJoueur, l, 6) ) > score){
                        p.best_score = stringToInt(getCell(profilJoueur, l, 6) );
                    }
                    else{
                        p.best_score = score;
                    }

                    p.total_tour_joues = nb_tours + stringToInt(getCell(profilJoueur, l, 7) );
                    if(stringToInt(getCell(profilJoueur, l, 8) ) > nb_tours){
                        p.best_tour_joues = stringToInt(getCell(profilJoueur, l, 8) );
                    }
                    else{
                        p.best_tour_joues = nb_tours;
                    }

                    chaines[l][0] = p.pseudo;

                    chaines[l][1] = p.nb_partie + "";
                    chaines[l][2] = p.partie_facile + "";
                    chaines[l][3] = p.partie_moyen + "";
                    chaines[l][4] = p.partie_difficile + "";

                    chaines[l][5] = p.total_score + "";
                    chaines[l][6] = p.best_score + "";

                    chaines[l][7] = p.total_tour_joues + "";
                    chaines[l][8] = p.best_tour_joues + "";
                }
                else{
                    for(int col=0; col<nbCol; col++){
                        chaines[l][col] = getCell(profilJoueur, l, col);
                    }
                }
            }

            saveCSV(chaines, SAVE_PROFIL);
        }
    }
  
    // Renvoie un tableau composé des statistiques enregistrées d'un joueur dans le fichier "save_profil.csv"
    String[] getPlayerStat(int choixStat){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        int col = columnCount(profilJoueur);

        String[] stat = new String[col];
        
        for(int i=0; i<col; i++){
            stat[i] = getCell(profilJoueur, choixStat, i);
        }

        return stat;
    }

    // Affiche les statistiques d'un joueur en utilisant le tableau définit précédemment
    void afficherPlayerStat(String[] playerStat){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        int nbCol = columnCount(profilJoueur);

        println("            Joueur : " + playerStat[0] + "\n");

        println("            Parties totales : " + playerStat[1]);
        print("            Parties joués en facile : " + playerStat[2] + "\t");
        print("            Parties joués en moyen : " + playerStat[3] + "\t");
        println("            Parties jouées en difficile : " + playerStat[4]);

        print("            Score total : " + playerStat[5] + "\t");
        println("            Meilleur score effectué : " + playerStat[6]);

        print("            Tour joués : " + playerStat[7] + "\t");
        println("            Tour joués en une partie : " + playerStat[8]);
    }

    // Insère une marge permettant d'aligner correctement le classement dans la fonction centrerText
    String marge(int longColX, int longChaine){
        String m = "";

        for(int i=0; i<longColX-longChaine; i++){
            m += " ";
        }

        return m;
    }

    // Sert à centrer le texte pour l'affichage du classement
    String centrerText(int longLine, int longMot, String mot){
        String marge = "";

        int mid = (longLine - longMot) / 2;

        for(int i=0; i<mid; i++){
            marge += " ";
        }

        return marge + mot + marge;
    }

    // Affiche le classement 
    // 3 colonnes: "PSEUDO" "BEST_SCORE" "BEST_TOURS_JOUES"
    // !!! ATTENTION la première ligne du fichier save_profil est désignée au nom des colonnes !!!
    void afficherClassement(){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        int nbL = rowCount(profilJoueur),
            nbCol = columnCount(profilJoueur),

            longCol = 13,
            longColPseudo = 0, longlongColBestScore = 0, longColBestTourJoues = 0,

            idx = 0;

        String[][] stats = new String[nbL-1][3],
            triStats = new String[nbL-1][3];

        for(int i=1; i<nbL; i++){ // Calcul longueur

            // pour colonne "PSEUDO"
            if(length(getCell(profilJoueur, i, 0) ) > longColPseudo){
                longColPseudo = length(getCell(profilJoueur, i, 0) );
            }

            // pour colonne "BEST_SCORE"
            if(length(getCell(profilJoueur, i, 6) ) > longlongColBestScore){
                longlongColBestScore = length(getCell(profilJoueur, i, 6) );
            }

            // pour colonne "BEST_TOURS_JOUES"
            if(length(getCell(profilJoueur, i, 8) ) > longColBestTourJoues){
                longColBestTourJoues = length(getCell(profilJoueur, i, 8) );
            }
        }

        longCol += longColPseudo + longlongColBestScore + longColBestTourJoues;

        // Tri des données
        for(int i=1; i<nbL; i++){
            stats[i-1][0] = getCell(profilJoueur, i, 0);
            stats[i-1][1] = getCell(profilJoueur, i, 6);
            stats[i-1][2] = getCell(profilJoueur, i, 8);
        }

        String[][] chaine = new String[1][3];
        for(int i=0; i<length(stats, 1); i++){
            chaine[0][0] = stats[i][0]; 
            chaine[0][1] = stats[i][1];
            chaine[0][2] = stats[i][2];

            for(int y=0; y<length(stats, 1); y++){
                if(stringToInt(chaine[0][1]) >= stringToInt(stats[y][1]) ){
                    if(stringToInt(chaine[0][1]) == stringToInt(stats[y][1])
                        && stringToInt(chaine[0][2]) >= stringToInt(stats[y][2]) ){

                        idx += 1;
                    }
                    if(stringToInt(chaine[0][1]) > stringToInt(stats[y][1]) ){
                        idx += 1;
                    }
                }
            }

            triStats[nbL-1-idx][0] = chaine[0][0];
            triStats[nbL-1-idx][1] = chaine[0][1];
            triStats[nbL-1-idx][2] = chaine[0][2];

            idx = 0;
        }

        // Affichage du Classement
        println("              " + centrerText(longColPseudo+3, length("PSEUDO"), "PSEUDO") + "   "
            + centrerText(longlongColBestScore, length("SCORE"), "SCORE") + "   "
            + centrerText(longColBestTourJoues, length("TOURS"), "TOURS") );

        print("             ");
        for(int i=0; i<longCol; i++){
            print("-");
        }

        println();

        for(int l=0; l<LARGEUR_CLASSEMENT; l++){
            print("             | " + (l+1) + ". " + triStats[l][0] +  marge(longColPseudo, length(triStats[l][0]) ) + " | "
                + triStats[l][1] +  marge(longlongColBestScore, length(triStats[l][1]) ) + " | "
                + triStats[l][2] +  marge(longColBestTourJoues, length(triStats[l][2]) ) + " |");

            println();
        }

        print("             ");
        for(int i=0; i<longCol; i++){
            print("-");
        }

        println();
    }

    // Sélectionne la chance qu'une épreuve soit contienne un bonus en fonction de la difficulté
    double bonus_difficulte(int difficulte){
        if(difficulte==1){
            return 0.4;
        }else if(difficulte==2){
            return 0.2;
        }else{
            return 0.01;
        }
    }
    
    // Renvoie le nombre de bonus présent dans l'inventaire
    int nbBonusInventaire(BONUS[] inventaire){
        int res = 0;
        int len = length(inventaire);
        for(int i=0;i<len;i++){
            if (inventaire[i]!=null){
                res += 1;
            }
        }
        return res;
    }
    
    // Choisit et renvoie un bonus de manière aléatoire
    BONUS selectionBonus(){
        double tmp = random();
        if (tmp<0.2){
            return BONUS.AsDeCoeur;
        }else if(tmp<0.4){
            return BONUS.AsDeCarreau;
        }else if(tmp<0.6){
            return BONUS.AsDePic;
        }else if(tmp<0.8){
            return BONUS.AsDeTrefle;
        }else{
            return BONUS.ValetDeTrefle;
        }
    }

    // Génère et renvoie un inventaire de bonus vide sous la forme d'un tableau
    BONUS[] creerInventaireVide(int taille){
        BONUS[] inventaire = new BONUS[taille];
        return inventaire;
    }

    // Transforme un bonus en son affichage
    String toStringBonus(BONUS bonus){
        if (bonus==BONUS.AsDeCoeur){
            return "As de Coeur";
        }
        else if (bonus==BONUS.AsDeCarreau){
            return "As de Carreau";
        }
        else if (bonus==BONUS.AsDePic){
            return "As de Pic";
        }
        else if (bonus==BONUS.AsDeTrefle){
            return "As de Trefle";
        }
        else if (bonus==BONUS.ValetDeTrefle){
            return "Valet de Trefle";
        }
        else{
            return "#ERR0R";
        }
    }

    // Affiche l'inventaire avec les options de choix avant chaque bonus
    String afficherInventaire(BONUS[] inventaire){
        String res = "";
        int len = length(inventaire);

        for(int i=0;i<len;i++){
            if (inventaire[i]==BONUS.AsDeCoeur){
                res += "             "+(i+1) + " : As de Coeur \n";
            }
            else if (inventaire[i]==BONUS.AsDeCarreau){
                res += "             "+(i+1) + " : As de Carreau \n";
            }
            else if (inventaire[i]==BONUS.AsDePic){
                res += "             "+(i+1) + " : As de Pic \n";
            }
            else if (inventaire[i]==BONUS.AsDeTrefle){
                res += "             "+(i+1) + " : As de Trefle \n";
            }
            else if (inventaire[i]==BONUS.ValetDeTrefle){
                res += "             "+(i+1) + " : Valet de Trefle \n";
            }
        }

        // Si l'inventaire est vide, affiche qu'il est vide
        if (inventaire[0]==null){
            println("            Votre inventaire est vide, quel dommage (sarcasme)...");
        }

        return res;
    }

    // Décale l'inventaire vert la gauche pour éviter d'avoir des trous dans le tableau
    BONUS [] inventaireDecalage(BONUS [] inventaire){
        int len = length(inventaire);int cpt=0;
        BONUS [] res = new BONUS[len];

        for (int i=0;i<len;i++){
            if (inventaire[i]!=null && inventaire[i]!=BONUS.Rien){
                res[cpt] = inventaire[i];
                cpt+= 1;
            }
        }

        return res;
    }

    //Ajoute un bonus à l'inventaire à la première case vide
    void ajouterBonus(BONUS [] inventaire, BONUS bonus){
        int i = 0; int len = length(inventaire);

        while(inventaire[i]!=null){
            i+=1;
        }

        inventaire[i]=bonus;
    }
    
    // Enlève un bonus de l'inventaire à partir de sa position et renvoie l'inventaire une fois décalé
    BONUS [] enleverBonus(BONUS[] inventaire, int choix){
        inventaire[choix]=null;

        return inventaireDecalage(inventaire);
    }

    ////// FONCTION DE VERIFICATIONS /////

    // Renvoie True si la réponse donnée est correcte
    boolean resultatEstCorrect(int res, int reponse){
        return (res==reponse);
    }

    // Vérifie que le paramètre entré pour le menu est valide
    boolean menuEntree(int menu){
        return (menu >= 0 && menu <=5);
    }

    // Vérifie que le paramètre entré pour le profil est valide
    boolean choixProfilValide(int choixProfil){
        return choixProfil >= 0 && choixProfil <= 2;
    }

    // Vérifie que le paramètre entré pour le joueur est valide
    boolean choixJoueurValide(int choixJoueur){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);
        int nbL = rowCount(profilJoueur);

        return choixJoueur >= 0 && choixJoueur < nbL;
    }

    // Vérifie que le paramètre entré pour la difficulté est valide
    boolean difficulteValide(int difficulte){
        return (difficulte>=0 && difficulte <=3);
    }

    // Vérifie que le paramètre entré pour les statistiques est valide
    boolean choixStatValide(int choixStat){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);
        int nbL = rowCount(profilJoueur);

        return choixStat >= 1 && choixStat < nbL;
    }

    // Vérifie que le paramètre entré pour les paramètres est valide
    boolean paramValide(int param){
        return (param>=0 && param <=4);
    }

    // Vérifie que le paramètre entré pour la couleur est valide
    boolean couleurValide(int couleur){
        return (couleur>=0 && couleur <= 6);
    }

    // Vérifie que le paramètre entré pour quitter est valide
    boolean valide_quitter(int quitter){
        return (quitter>=0 && quitter <= 2);
    }

    // Vérifie que le paramètre entré pour l'inventaire est valide
    boolean inventaireValide(int inv , BONUS []inventaire){
        return (inv>= 0 && inv <= nbBonusInventaire(inventaire));
    }
    
    // Renvoie si l'inventaire est plein ou non
    boolean inventairePlein(BONUS[] inventaire){
        return (inventaire[length(inventaire)-1] != null);
    }

    ////// FONCTION DE TESTS /////

    void testGetJoueur(){
        assertEquals("Ruxo", getJoueur(2) );
    }

    void testGetPlayerStat(){
        String[] chaine = new String[]{"PoA", "3162", "252", "789", "2121", "9999999", "99999", "99999", "9999"};

        assertArrayEquals(chaine, getPlayerStat(1));
    }

    void testMarge(){
        assertEquals("          ", marge(15, 5));
        assertEquals("  ", marge(8, 6));
    }

    void testCentrerText(){
        assertEquals("    PSEUDO    ", centrerText(15, 6, "PSEUDO"));

        assertEquals("  SCORE  ", centrerText(9, 5, "SCORE"));
        assertEquals(" SCORE ", centrerText(8, 5, "SCORE"));

        assertEquals("TOURS", centrerText(3, 5, "TOURS"));
    }

    ///// FONCTION PRINCIPALE /////
    
    void algorithm(){
        afficherText(FIXAGE); // Fixe le jeu en bas du terminal

        couleur_base = "white"; 
        couleur_epreuve = "red";
        reset();

        // Initialise tout les paramètres de menu à des valeurs impossibles
        int difficulte = -1, parametre = -1, couleur = -1,
            quitter_menu = -1, inventaire_menu = -1,
            choixProfil = -1, choixJoueur = -1, choixStat = -1, choixRanking = -1,
            regle = 1,
            
        // Initialise les variables principales qui seront utilisée dans le jeu
            case_actuelle = 0, mouv = 0, nb_tours = 1,
            vies = 5, score = 0,
            
            menu = -1;

        String pseudo = "",
            choixTour = "";
        
        // Crée un plateau vides ainsi que des variables annexes utilisées dans le jeu
        Cases[] plateau = new Cases[TAILLE_TABLEAU]; boolean carreau = false; boolean pic = false; BONUS bonusChoisi=BONUS.Rien; int multicarreau = 1;
        double multitrefle = 1.0; boolean trefle = false; BONUS newBonus = BONUS.Rien; boolean epreuveProchaine = false;
        int tailleInventaire = 5; BONUS[] inventaire = creerInventaireVide(tailleInventaire);

        // Boucle infinie pour que le jeu se joue en boucle
        while(true){
            
            // Réinitialisation de toutes les valeurs après le retour au menu
            difficulte = -1; parametre = -1; choixProfil = -1; choixJoueur = -1; choixStat = -1; choixRanking = -1;
            pseudo = ""; nb_tours = 1; score = 0; vies = 5;
            epreuveProchaine = false; regle = 1;
            multitrefle=1; carreau = false; pic = false; trefle=false;

            clearScreen();
            afficherText(TITLE);
            println();
            afficherText(MAIN_MENU); // Affiche le menu avec les choix
            
            while (!menuEntree(menu)){ // Demande au joueur tant que ce n'est pas valide
                println();
                print("Veuillez entrez un entier valide : ");
                menu = readInt();
                println();
            }

            if (menu == 1){ // Si le joueur entre 1 pour une nouvelle partie
                plateau = creerPlateau(TAILLE_TABLEAU , case_actuelle);
                inventaire = creerInventaireVide(tailleInventaire);

                while(!choixProfilValide(choixProfil)){ // Redemande jusqu'à ce que le joueur entre un profil valide
                    clearScreen();
                    afficherText(MENU_JOUEUR);

                    println();
                    print("Entrez un choix valide : ");
                    choixProfil = readInt();
                    println();

                    if(choixProfil == 0){ // RETOUR
                        menu = -1;
                    }
                    else if(choixProfil == 1){ // Créer un joueur
                        clearScreen();

                        print("Entrez un pseudonyme : ");
                        pseudo = readString();
                    }
                    else if(choixProfil == 2){ // Joue sur un profil existant
                        while(!choixJoueurValide(choixJoueur)){
                            clearScreen();
                            afficherListeJoueur();

                            println();
                            println("            0 : Retour");
                            println("\n\n\n");

                            print("Choisir le joueur : ");
                            choixJoueur = readInt();
                            
                            if(choixJoueur == 0){
                                choixProfil = -1;
                            }
                            else if(choixJoueurValide(choixJoueur)){
                                pseudo = getJoueur(choixJoueur); // Initialise qui est le joueur
                                
                            }
                        }
                        
                        choixJoueur = -1;
                    }
                }

                while(!difficulteValide(difficulte) && choixProfil != 0){ // Redemande jusqu'à ce que la difficulté soit valide
                    clearScreen();
                    afficherText(MENU_DIFFICULTE);

                    println();
                    print("Entrez une difficulté valide : ");
                    difficulte = readInt();
                    println();

                    if (difficulte == 0){ // Retour au menu
                        menu = -1;
                    }
                }    
                PROBA_BONUS = bonus_difficulte(difficulte);

                while(menu==1){ // Boucle du jeu
                    quitter_menu = -1; choixTour = "";newBonus= BONUS.Rien;
                    
                    // Affichage principal du jeu à chaque tour
                    clearScreen();
                    println(toString(plateau));
                    println();
                    println("            Joueur : " + pseudo);
                    println("            Difficulté : " + NOM_DIFFICULTE[difficulte-1]);
                    if(vies == 1){
                        println("            Vie restante : " + vies);
                    }
                    else{
                        println("            Vies restantes : " + vies);
                    }
                    println("            Tour : " + nb_tours);
                    println("            Score : " + score);
                    println();
                    mouv = movement();
                    nb_tours += 1;

                    // Si la prochaine case est une épreuve et que le bonus Pic n'est pas actif
                    if (epreuveProchaine && !pic && (!equals(choixTour, "quitter") || !equals(choixTour, "inventaire"))){ 
                        if (carreau){ // Effet du bonus carreau actif
                            multicarreau = nb_tours;
                            carreau = false;
                        }
                        if (trefle){ // Effet du bonus Trefle actif
                            multitrefle = multitrefle *1.2;
                            trefle = false;
                        }

                        if (!doEpreuve(difficulte)){ // Si la réponse à l'épreuve est fausse
                            if(random()<PROBA_BONUS){
                                vies -= 1;
                                score -= scoreMultiplieur(difficulte)*multicarreau *multitrefle+ 100;

                                println("Il s'agissait d'un bonus mais vous ne l'aurez pas...");

                            }
                            else{
                                vies -= 1;
                                score -= scoreMultiplieur(difficulte)*multicarreau *multitrefle+ 100;

                                println();
                            }

                        }
                        else{
                            println("Bonne réponse");

                            if(random()<PROBA_BONUS){
                                score += ( mouv + scoreMultiplieur(difficulte)*multicarreau*multitrefle ) + 100; // Augmente le score avec les multiplieurs
                                newBonus = selectionBonus();

                                if (!inventairePlein(inventaire)){ // Ajoute un bonus si l'inventaire n'est pas plein
                                    ajouterBonus(inventaire, newBonus);
                                    println();
                                    println("Vous ajoutez "+ toStringBonus(newBonus)+" à votre inventaire !");
                                    println();
                                    println();
                                }
                                else{ // Sinon force le joueur à utiliser un bonus pour le remplacer
                                    clearScreen();
                                    afficherText(MENU_INVENTAIRE);

                                    println("Votre inventaire est plein veuillez utiliser un bonus");
                                    println();
                                    println(afficherInventaire(inventaire));
                                    println();
                                    
                                    while(!inventaireValide(inventaire_menu,inventaire) && inventaire_menu!=0){
                                        println();
                                        print("Veuillez choisir un bonus à échanger avec "+toStringBonus(newBonus)+" : ");
                                        inventaire_menu = readInt();
                                    }

                                    bonusChoisi = inventaire[inventaire_menu-1];
                                    inventaire = enleverBonus(inventaire , inventaire_menu-1);
                                    ajouterBonus(inventaire, newBonus);

                                    if(bonusChoisi!=BONUS.Rien){
                                        if (bonusChoisi==BONUS.AsDeCoeur){
                                            vies += 1;
                                        }
                                        else if(bonusChoisi==BONUS.AsDeCarreau){
                                            carreau = true;
                                        }
                                        else if(bonusChoisi==BONUS.AsDePic){
                                            pic = true;
                                        }
                                        else if(bonusChoisi==BONUS.AsDeTrefle){
                                            trefle = true;
                                        }
                                        else if(bonusChoisi==BONUS.ValetDeTrefle){
                                            score=score/2;
                                        }
                                        bonusChoisi=BONUS.Rien;
                                    }
                                    inventaire_menu = -1;
                                }
                            }
                            else{
                                score += ( mouv + scoreMultiplieur(difficulte)*multicarreau*multitrefle ) + 100; // Augmente le score avec les multiplieurs
                                println();
                            }
                        }
                        multicarreau = 1;pic = false; // Réinitialise le multiplieurs de trèfle et reinitialise pic
                    }
                    
                    epreuveProchaine = (plateau[prochaineCase(case_actuelle,mouv)] == Cases.EPREUVE);
                    choixTour = jouerTour(plateau , case_actuelle, prochaineCase(case_actuelle,mouv));
                    
                    if(!equals(choixTour,"quitter") && !equals(choixTour, "inventaire")){ // Modifie la case actuelle du joueur à celle de la prochaine case
                        case_actuelle=prochaineCase(case_actuelle,mouv);
                    }
                    
                    // Ouvre le menu pour quitter une partie en cours
                    while ((equals(choixTour, "quitter") )&& !valide_quitter(quitter_menu)){
                        clearScreen();
                        afficherText(MENU_QUITTER);
                        println();
                        print("Entrez un entier valide : ");
                        quitter_menu = readInt();
                        if (quitter_menu == 1){
                            case_actuelle = 0;
                            menu = -1;
                        } else if(quitter_menu == 2){
                            saveProfil(choixProfil, pseudo, difficulte, score, nb_tours);

                            println("Sauvegarde effectuée !");
                            delay(1500);
                            case_actuelle = 0;
                            menu = -1;                        println();
                        }else if (quitter_menu == 0){
                            quitter_menu = -1;
                            choixTour = "";
                        }
                    }
                    
                    // Permets d'accéder à l'inventaire et utiliser un bonus
                    while ((equals(choixTour, "inventaire") )&& !inventaireValide(inventaire_menu, inventaire)){
                        clearScreen();
                        afficherText(MENU_INVENTAIRE);
                        println(afficherInventaire(inventaire));
                        println();
                        println("            0 : Retour");
                        println();
                        print("Entrez un entier valide pour utiliser un bonus : ");
                        inventaire_menu = readInt();

                        if (inventaire_menu == 0){
                            inventaire_menu = -1;
                            choixTour = "";
                        }else if (inventaireValide(inventaire_menu, inventaire)){
                            bonusChoisi = inventaire[inventaire_menu-1];
                            inventaire = enleverBonus(inventaire , inventaire_menu-1);
                            choixTour ="";
                        }
                    }
                    if(bonusChoisi!=BONUS.Rien){
                        if (bonusChoisi==BONUS.AsDeCoeur){
                            vies += 1;
                        } else if(bonusChoisi==BONUS.AsDeCarreau){
                            carreau = true;
                        }else if(bonusChoisi==BONUS.AsDePic){
                            pic = true;
                        }else if(bonusChoisi==BONUS.AsDeTrefle){
                            trefle = true;
                        }else if(bonusChoisi==BONUS.ValetDeTrefle){
                            score=score/2;
                        }
                        bonusChoisi=BONUS.Rien;
                    }
                    inventaire_menu = -1;
                    
                    if (vies == 0){ // Si jamais le joueur n'a plus de vie, il perd, cela sauvegarde et coupe la boucle
                        clearScreen();
                        afficherText(DEFAITE);
                        println(toString(plateau));
                        println();
                        println("           Joueur : " + pseudo);
                        println("           Tour : " + nb_tours);
                        println("           Score : " + score);
                        println();
                        println("Sauvegarde en cours...");
                        delay(2000);

                        saveProfil(choixProfil, pseudo, difficulte, score, nb_tours);
                        menu = -1;
                    }
                }
            }

            // STATISTIQUES
            else if(menu == 2){
                while(!choixStatValide(choixStat) && choixStat != 0){
                    clearScreen();
                    afficherText(MENU_STATISTIQUES);
                    println();
    
                    afficherListeJoueur();
                    println();
                    println("            0 : Retour");
                    println("\n\n\n");
                    print("Choisir le profil à afficher : ");

                    choixStat = readInt();

                    if(choixStat == 0){
                        menu = -1;
                    }
                    else if(choixStatValide(choixStat)){
                        clearScreen();
                        afficherText(MENU_STATISTIQUES);
                        println();

                        afficherPlayerStat(getPlayerStat(choixStat) );
                        println("\n\n\n\n\n\n");

                        print("Mettez \"Entrée\" pour retourner au menu : ");
                        readString();
                        menu = 2;
                    }
                }
            }

            // CLASSEMENT
            else if(menu == 3){
                while(choixRanking != 0){
                    clearScreen();
                    afficherText(MENU_CLASSEMENT);
                    println();

                    afficherClassement();
                    println("\n\n\n");
                    println("            0 : Retour");
                    println("\n\n\n");

                    print("Entrez un entier valide : ");
                    choixRanking = readInt();

                    if(choixRanking == 0){
                        menu = -1;
                    }
                }
            }

            // PARAMETRES
            else if (menu == 4){
                while(!paramValide(parametre)){
                    couleur = -1;

                    // Affichage du menu des paramètres
                    clearScreen();
                    afficherText("parametre.txt");
                    println();
                    println("            1 : Modifier caractère joueur : "+joueur);
                    println("            2 : Modifier caractère chemin : "+chemin);
                    println("            3 : Modifier caractère épreuve : "+ epreuve);
                    println("            4 : Modifier les couleurs");
                    println();
                    println("            0 : Retour");
                    println();
                    println();
                    println();
                    println();
                    print("Entrez un entier valide : ");
                    parametre = readInt();

                    if (parametre == 0){
                        menu = -1;
                    }
                    else if (parametre == 1){
                        println();
                        print("Entrez un caractère : ");
                        joueur = readChar();
                        parametre = -1;
                    }
                    else if (parametre == 2){
                        println();
                        print("Entrez un caractère : ");
                        chemin = readChar();
                        parametre = -1;
                    }
                    else if (parametre == 3){
                        println();
                        print("Entrez un caractère : ");
                        epreuve = readChar();
                        parametre = -1;
                    }
                    else if (parametre == 4){
                        // Sert à modifier les couleurs
                        while(!couleurValide(couleur) && parametre == 4){
                            clearScreen();
                            afficherText(MENU_COULEUR);

                            println();
                            print("Entrez un entier valide : ");
                            couleur = readInt();

                            if (couleur == 0){
                                parametre = -1;
                            }
                            else if(couleur == 6){
                                reset(); couleur_base = "white";
                                couleur = -1;
                            }
                            else if (couleur == 1){
                                couleur_base = "red";
                                couleur_epreuve = "blue";
                                text(couleur_base);
                                background("black");
                                couleur = -1;
                            }
                            else if (couleur == 2){
                                couleur_base = "blue";
                                text(couleur_base);
                                background("white");
                                couleur = -1;
                            }
                            else if (couleur == 3){
                                couleur_base = "green";
                                text(couleur_base);
                                background("black");
                                couleur = -1;
                            }
                            else if (couleur == 4){
                                couleur_base = "white";
                                text(couleur_base);
                                background("black");
                                couleur = -1;
                            }
                            else if (couleur == 5){
                                couleur_base = "black";
                                text(couleur_base);
                                background("white");
                                couleur = -1;
                            }
                        }
                    }
                }
            }

            // REGLES
            else if(menu == 5){
                while(regle!=0){ // Permet de sélectionner quel page de règle
                    if (regle==1){
                        clearScreen();
                        afficherText(MENU_REGLE_1);
                        print("Entrez un entier valide : ");
                        regle = readInt();
                    }
                    else if(regle==2){
                        clearScreen();
                        afficherText(MENU_REGLE_2);
                        print("Entrez un entier valide : ");
                        regle = readInt();
                    }
                }
                menu = -1;
            }
            else if(menu == 0){ // Casse la boucle while(true) pour quitter le jeu après avoir nettoyer l'écran
                clearScreen();
                break;
            } 
        }
    }
}
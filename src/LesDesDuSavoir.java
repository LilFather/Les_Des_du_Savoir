import extensions.File;
import extensions.CSVFile;

class LesDesDuSavoir extends Program{
    char joueur = 'J';
    char chemin = '_';
    char epreuve = '?';
    // LANG langue = LANG.FR;

    String couleur_epreuve = "red";
    String couleur_base = "white";

    final int TAILLE_TABLEAU = 152;
    final int LARGEUR_CLASSEMENT = 7; // Affichage du TOP 7

    final double PROBA_EPREUVE = 0.33; double PROBA_BONUS = 0.0;

    final String FIXAGE = "../ressources/fixage.txt";
    final String TITLE = "../ressources/savoir.txt";
    final String MAIN_MENU = "../ressources/menu.txt";
    final String MENU_DIFFICULTE = "../ressources/difficulte.txt";
    final String MENU_JOUEUR = "../ressources/creerjoueurmenu.txt";
    final String MENU_INVENTAIRE = "../ressources/inventaire.txt";
    final String MENU_QUITTER = "../ressources/quitter_partie.txt";
    final String MENU_STATISTIQUES = "../ressources/statistique.txt";
    final String MENU_CLASSEMENT = "../ressources/classement.txt";
    final String MENU_PARAMETRE = "../ressources/parametre.txt";
    final String MENU_COULEUR = "../ressources/couleurs.txt";
    final String MENU_REGLE_1 = "../ressources/regle.txt";
    final String MENU_REGLE_2 = "../ressources/regle2.txt";
    final String DEFAITE = "../ressources/defaite.txt";

    final String SAVE_PROFIL = "../ressources/save_profil.csv";
    final String QUESTION_EPREUVE = "../ressources/list_question.csv";


    final String[] NOM_DIFFICULTE = new String[]{"FACILE", "MOYEN", "DIFFICLE"};

    int movement(){
        return (int) ((random()*6) +1);
    }

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

    char casesToChar(Cases cases){
            if(cases==Cases.JOUEUR){
                return joueur;
            }else if(cases==Cases.EPREUVE){
                return epreuve;
            }else{
                return chemin;
            }
    }
    String toString(Cases[] plateau){
        String res = "";
        int len = length(plateau);
        for(int i = 0;i<len ;i++){
            res += casesToChar(plateau[i]);
        }
        return res;
    }

    void deplacerJoueur(Cases[] plateau , int case_actuelle, int prochaine){
        Cases tmp = plateau[prochaine];
        println(plateau[case_actuelle]);
        println(tmp);
        plateau[prochaine] = Cases.JOUEUR;
        plateau[case_actuelle] = tmp;
    }

    int prochaineCase(int case_actuelle , int mouv){
        if (case_actuelle+mouv>=TAILLE_TABLEAU){
            return case_actuelle+mouv-TAILLE_TABLEAU;
        }else{
            return case_actuelle+mouv;
        }
    }
/*
    void deplacerJoueur(char[] plateau , int case_actuelle, int mouv){
        plateau[prochaineCase(case_actuelle,mouv)] = joueur;
        plateau[case_actuelle]=chemin;
    }
*/
    String jouerTour(Cases[] plateau, int case_actuelle, int prochaine){
        println("Veuillez lancer le dé en appuyant sur \"Entrée\", entrer \"quitter\" pour quitter et \"inventaire\" pour accéder à votre inventaire ");
        String res = readString();
        int resultat = 0;
        if (equals(res ,"quitter") || equals(res,"inventaire")){
            deplacerJoueur(plateau , case_actuelle , case_actuelle);
            return res;
        } else {
            deplacerJoueur(plateau , case_actuelle , prochaine);
            return "";
        }
    }

boolean doEpreuve(int difficulte){ // Tire aléatoirement une épreuve
        double proba = random();

        if(proba < 0.4){
            return epreuveMath(difficulte);
        }
        else{
            return epreuveQuestion(difficulte);
        }
    }

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

        if (!testerResultat(res , reponse)){
            println("Mauvaise réponse, c'était " + res);

            return false;
        }
        else{
            return true;
        }
    }

    boolean epreuveQuestion(int difficulte){
        CSVFile question = loadCSV(QUESTION_EPREUVE);

        int nbL = rowCount(question),
            nbCol = columnCount(question),
            choixQ = (int)(random() * nbL),
            choixR = 0,
            idx = 0,
            res = 0,
            reponse = 0;

        if(difficulte == 3){
            int[] tabInit = new int[]{1,2,3,4,5},
                tabMel = new int[]{-1,-1,-1,-1,-1};

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

            text(couleur_epreuve);
            println("           " + getCell(question, choixQ, 0) );
            println("           " + 1 +" - " + getCell(question, choixQ, tabMel[0]) );
            println("           " + 2 +" - " + getCell(question, choixQ, tabMel[1]) );
            println("           " + 3 +" - " + getCell(question, choixQ, tabMel[2]) );
            println("           " + 4 +" - " + getCell(question, choixQ, tabMel[3]) );
            println("           " + 5 +" - " + getCell(question, choixQ, tabMel[4]) );
            text(couleur_base);

            res = idx+1;
        }
        else if(difficulte == 2){
            int[] tabInit = new int[]{1,2,3,4,5},
                tabMel = new int[]{-1,-1,-1,-1};

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

            text(couleur_epreuve);
            println("           " + getCell(question, choixQ, 0) );
            println("           " + 1 +" - " + getCell(question, choixQ, tabMel[0]) );
            println("           " + 2 +" - " + getCell(question, choixQ, tabMel[1]) );
            println("           " + 3 +" - " + getCell(question, choixQ, tabMel[2]) );
            println("           " + 4 +" - " + getCell(question, choixQ, tabMel[3]) );
            text(couleur_base);

            res = idx+1;
        }
        else{
            int[] tabInit = new int[]{1,2,3,4,5},
                tabMel = new int[]{-1,-1,-1};

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

            text(couleur_epreuve);
            println("           " + getCell(question, choixQ, 0) );
            println("           " + 1 +" - " + getCell(question, choixQ, tabMel[0]) );
            println("           " + 2 +" - " + getCell(question, choixQ, tabMel[1]) );
            println("           " + 3 +" - " + getCell(question, choixQ, tabMel[2]) );
            text(couleur_base);

            res = idx+1;
        }

        println();
        print("Entrez une réponse : ");
        reponse = readInt();
        println();

        if (!testerResultat(res , reponse)){
            println("Mauvaise réponse, c'était " + getCell(question, choixQ, 1));

            return false;
        }
        else{
            return true;
        }
    }

    int scoreMultiplieur(int difficulte){
        if(difficulte == 3){
            return 9;
        }
        else if(difficulte == 2){
            return 3;
        }
        else{
            return 1;
        }
    }

    boolean testerResultat(int res, int reponse){
        return (res==reponse);
    }

    boolean estUnEntier(String chaine){
        boolean oui = false; int cpt = 0;
        int len = length(chaine);
        while(cpt<len && !oui){
            oui = (charAt(chaine, cpt)>'0' && charAt(chaine,cpt)<'9');
        }
        return oui;
    }

    Epreuve creerCategorie(String categorie, String[] questions, String[] reponses, int nb_questions){
        Epreuve c = new Epreuve();
        c.categorie = categorie;
        c.questions = questions;
        c.reponses = reponses;
        return c;
    }

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

/*
    int resultat(String math){
        int len = length(math);
        int i = 2;
        char car = charAt(math , i);
        String nb1 = 0; String nb2 = 0;
        while (!(car != '/') || !(car != '*') || !(car != '+') || !(car != '-')){
            car = charAt(math , i);
            i++;
        }
        nb1 = (substring(math,0,i-1));
        nb2 = (substring(math,i+1,len-1));
        println(nb1 + " " + nb2);
        if (car == '/'){
            return (nb1 / nb2);
        } else if (car == '*'){
            return (nb1 * nb2);
        } else if(car == '+'){
            return (nb1 + nb2);
        }else {
            return (nb1 - nb2);
        }
    }
*/  
/*  
    void algorithm(){
        int difficulte = -1;
        int case_actuelle = 0;
        int mouv = 0;
        int res_epreuve = 0; int reponse = 0;
        char[] plateau = creerPlateau(TAILLE_TABLEAU , case_actuelle);
        while(true){
            println(toString(plateau));
            println();
            println(vies);
            mouv = movement();
            if (plateau[prochaineCase(case_actuelle,mouv)] == epreuve){
                res_epreuve = epreuveMath(0);
                print("Entrez une réponse : ");
                reponse = readInt();
                if (!testerResultatMath(res_epreuve , reponse)){
                    println("Mauvaise réponse");
                    vies -= 1;
                }else{
                    println("Bonne réponse");
                }
            }
            jouerTour(plateau , case_actuelle, prochaineCase(case_actuelle,mouv));
            case_actuelle=prochaineCase(case_actuelle,mouv);
        }
    }
    */
    void afficherText(String fichier){
        File f = newFile(fichier);

        while(ready(f)){
            println(readLine(f));
        }
    }

    void afficherListeJoueur(){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);
        int nbL = rowCount(profilJoueur);

        for(int l=0; l<nbL; l++){
            String cell = getCell(profilJoueur, l, 0);
            print("            " + (l+1) + " : " + cell);

            println();
        }
    }

    String getJoueur(int choixJoueur){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        return getCell(profilJoueur, choixJoueur-1, 0);
    }

    void saveProfil(int choixProfil, String pseudo, int difficulte, int score, int nb_tours){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        int nbL = rowCount(profilJoueur),
            nbCol = columnCount(profilJoueur);

        Profil p = creerProfil(pseudo, score, nb_tours);

        if(choixProfil == 1){ // Créer un profil
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
        else if(choixProfil == 2){ // Sauvegarde sur un profil existant
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

    String[] getPlayerStat(int choixStat){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        int col = columnCount(profilJoueur);

        String[] stat = new String[col];
        
        for(int i=0; i<col; i++){
            stat[i] = getCell(profilJoueur, choixStat-1, i);
        }

        return stat;
    }

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

    String marge(int longCol_X, int longChaine){
        String m = "";

        for(int i=0; i<longCol_X-longChaine; i++){
            m += " ";
        }

        return m;
    }

    String centrerText(int longLine, int longMot, String mot){
        String marge = "";

        int mid = (longLine - longMot) / 2;

        for(int i=0; i<mid; i++){
            marge += " ";
        }

        return marge + mot + marge;
    }

    void afficherClassement(){ // 3 colonnes: "PSEUDO" "BEST_SCORE" "BEST_TOURS_JOUES"
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);

        int nbL = rowCount(profilJoueur),
            nbCol = columnCount(profilJoueur),

            longCol = 13,
            longCol_Pseudo = 0, longCol_BestScore = 0, longCol_BestTourJoues = 0,

            idx = 0;

        String[][] stats = new String[nbL][3],
            triStats = new String[nbL][3];

        for(int i=0; i<nbL; i++){ // Calcul longueur

            // pour colonne "PSEUDO"
            if(length(getCell(profilJoueur, i, 0) ) > longCol_Pseudo){
                longCol_Pseudo = length(getCell(profilJoueur, i, 0) );

                //println(longCol_Pseudo);
            }

            // pour colonne "BEST_SCORE"
            if(length(getCell(profilJoueur, i, 6) ) > longCol_BestScore){
                longCol_BestScore = length(getCell(profilJoueur, i, 6) );

                //println(longCol_BestScore);
            }

            // pour colonne "BEST_TOURS_JOUES"
            if(length(getCell(profilJoueur, i, 8) ) > longCol_BestTourJoues){
                longCol_BestTourJoues = length(getCell(profilJoueur, i, 8) );

                //println(longCol_BestTourJoues);
            }
        }

        longCol += longCol_Pseudo + longCol_BestScore + longCol_BestTourJoues;

        //println(longCol);

        // Tri des données
        for(int i=0; i<nbL; i++){
            stats[i][0] = getCell(profilJoueur, i, 0);
            stats[i][1] = getCell(profilJoueur, i, 6);
            stats[i][2] = getCell(profilJoueur, i, 8);
        }

        String[][] chaine = new String[1][3];
        for(int i=0; i<nbL; i++){
            chaine[0][0] = stats[i][0]; 
            chaine[0][1] = stats[i][1]; 
            chaine[0][2] = stats[i][2];

            for(int y=0; y<nbL; y++){
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

            triStats[(nbL-idx)][0] = chaine[0][0];
            triStats[nbL-idx][1] = chaine[0][1];
            triStats[nbL-idx][2] = chaine[0][2];

            idx = 0;
        }

        // Affichage du Classement
        println("              " + centrerText(longCol_Pseudo+3, length("PSEUDO"), "PSEUDO") + "   "
            + centrerText(longCol_BestScore, length("SCORE"), "SCORE") + "   "
            + centrerText(longCol_BestTourJoues, length("TOURS"), "TOURS") );

        print("             ");
        for(int i=0; i<longCol; i++){
            print("-");
        }

        println();

        for(int l=0; l<LARGEUR_CLASSEMENT; l++){
            print("             | " + (l+1) + ". " + triStats[l][0] +  marge(longCol_Pseudo, length(triStats[l][0]) ) + " | "
                + triStats[l][1] +  marge(longCol_BestScore, length(triStats[l][1]) ) + " | "
                + triStats[l][2] +  marge(longCol_BestTourJoues, length(triStats[l][2]) ) + " |");

            println();
        }

        print("             ");
        for(int i=0; i<longCol; i++){
            print("-");
        }

        println();
    }

    boolean menuEntree(int menu){
        return (menu >= 0 && menu <=5);
    }

    boolean choixProfilValide(int choixProfil){
        return choixProfil >= 0 && choixProfil <= 2;
    }

    boolean difficulteValide(int difficulte){
        return (difficulte>=0 && difficulte <=3);
    }

    boolean choixStatValide(int choixStat){
        CSVFile profilJoueur = loadCSV(SAVE_PROFIL);
        int nbL = rowCount(profilJoueur);

        return choixStat >= 0 && choixStat <= nbL;
    }

    boolean paramValide(int param){
        return (param>=0 && param <=4);
    }

    boolean couleurValide(int couleur){
        return (couleur>=0 && couleur <= 6);
    }

    boolean valide_quitter(int quitter){
        return (quitter>=0 && quitter <= 2);
    }

    boolean inventaireValide(int inv , BONUS []inventaire){
        return (inv>= 0 && inv <= nbBonusInventaire(inventaire));
    }

    double bonus_difficulte(int difficulte){
        if(difficulte==1){
            return 0.4;
        }else if(difficulte==2){
            return 0.2;
        }else{
            return 0.01;
        }
    }
    
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

    BONUS[] creerInventaireVide(int taille){
        BONUS[] inventaire = new BONUS[taille];
        return inventaire;
    }
    String toStringBonus(BONUS bonus){
        if (bonus==BONUS.AsDeCoeur){
            return "As de Coeur";
        } else if (bonus==BONUS.AsDeCarreau){
            return "As de Carreau";
        }else if (bonus==BONUS.AsDePic){
            return "As de Pic";
        }else if (bonus==BONUS.AsDeTrefle){
            return "As de Trefle";
        }else if (bonus==BONUS.ValetDeTrefle){
            return "Valet de Trefle";
        }else{
            return "#ERR0R(paul corrige ça c'est pas sérieux frérot tkt j'te trust";
        }
    }
    String afficherInventaire(BONUS[] inventaire){
        String res = "";
        int len = length(inventaire);
        for(int i=0;i<len;i++){
            if (inventaire[i]==BONUS.AsDeCoeur){
                res += "             "+(i+1) + " : As de Coeur \n";
            } else if (inventaire[i]==BONUS.AsDeCarreau){
                res += "             "+(i+1) + " : As de Carreau \n";
            }else if (inventaire[i]==BONUS.AsDePic){
                res += "             "+(i+1) + " : As de Pic \n";
            }else if (inventaire[i]==BONUS.AsDeTrefle){
                res += "             "+(i+1) + " : As de Trefle \n";
            }else if (inventaire[i]==BONUS.ValetDeTrefle){
                res += "             "+(i+1) + " : Valet de Trefle \n";
            }
        }
        if (inventaire[0]==null){
            println("            Votre inventaire est vide, quel dommage (sarcasme)...");
        }
        return res;
    }

    BONUS [] inventaireDecalage(BONUS [] inventaire){
        int len = length(inventaire);int cpt=0;
        BONUS [] res = new BONUS[len];
        for (int i=0;i<len;i++){
            if (inventaire[i]!=null){
                res[cpt] = inventaire[i];
                cpt+= 1;
            }
        }
        return res;
    }

    boolean inventairePlein(BONUS[] inventaire){
        return (inventaire[length(inventaire)-1] != null);
    }

    void ajouterBonus(BONUS [] inventaire, BONUS bonus){
        int i = 0; int len = length(inventaire);
        while(inventaire[i]!=null){
            i+=1;
        }
        inventaire[i]=bonus;
    }
    void enleverBonus(BONUS[] inventaire, int choix){
        inventaire[choix]=null;
        inventaireDecalage(inventaire);
    }

    void algorithm(){
        afficherText(FIXAGE);

        couleur_base = "white";
        couleur_epreuve = "red";
        reset();

        int difficulte = -1, parametre = -1, couleur = -1,
            quitter_menu = -1, inventaire_menu = -1,
            choixProfil = -1, choixStat = -1, choixRanking = -1,
            regle = 1,
            
            case_actuelle = 0, mouv = 0, nb_tours = 1,
            vies = 5, score = 0,
            
            menu = -1;

        String pseudo = "",
            choixTour = "";

        Cases[] plateau = new Cases[TAILLE_TABLEAU]; boolean carreau = false; boolean pic = false; BONUS bonusChoisi=BONUS.Rien; int multicarreau = 1;
        double multitrefle = 1.0; boolean trefle = false; BONUS newBonus = BONUS.Rien; boolean epreuveProchaine = false;
        int tailleInventaire = 5; BONUS[] inventaire = creerInventaireVide(tailleInventaire);

        while(true){
            difficulte = -1; parametre = -1; choixProfil = -1; choixStat = -1; choixRanking = -1;
            pseudo = ""; nb_tours = 1; score = 0; vies = 5;
            epreuveProchaine = false; regle = 1;
            multitrefle=1; carreau = false; pic = false; trefle=false;

            clearScreen();
            afficherText(TITLE);
            println();
            /*println("1 : Nouvelle partie");
            println("2 : Reprendre une partie en cours");objet
            println("3 : Paramètres");
            println();
            println("0 : Quitter");
            */
            afficherText(MAIN_MENU);
            while (!menuEntree(menu)){
                println();
                print("Veuillez entrez un entier valide : ");
                menu = readInt();
                println();
            }

            if (menu == 1){
                plateau = creerPlateau(TAILLE_TABLEAU , case_actuelle);
                inventaire = creerInventaireVide(tailleInventaire);
                while(!choixProfilValide(choixProfil)){
                    clearScreen();
                    afficherText(MENU_JOUEUR);
                    /*
                    println("1 : Créer un joueur");
                    println("2 : Jouer avec un joueur existant");

                    println();
                    println("0 : Retour");
                    */
                    println();
                    print("Entrez un choix valide : ");
                    choixProfil = readInt();
                    println();

                    if(choixProfil == 0){
                        menu = -1;
                    }
                    else if(choixProfil == 1){
                        clearScreen();

                        print("Entrez un pseudonyme : ");
                        pseudo = readString();
                    }
                    else if(choixProfil == 2){
                        clearScreen();
                        afficherListeJoueur();

                        println();
                        println("            0 : Retour");
                        println("\n\n\n");

                        print("Choisir le joueur : ");
                        int choixJoueur = readInt();
                        
                        if(choixJoueur == 0){
                            choixProfil = -1;
                        }
                        else{
                            pseudo = getJoueur(choixJoueur);
                        }
                    }
                }

                while(!difficulteValide(difficulte) && choixProfil != 0){
                    clearScreen();
                    afficherText(MENU_DIFFICULTE);
                    /*
                    println("1 : Facile");
                    println("2 : Moyen");
                    println("3 : Difficile");
                    println();
                    println("0 : Retour");
                    */
                    println();
                    print("Entrez une difficulté valide : ");
                    difficulte = readInt();
                    println();
                    if (difficulte == 0){
                        menu = -1;
                    }
                }    
                PROBA_BONUS = bonus_difficulte(difficulte);
                while(menu==1){
                    quitter_menu = -1; choixTour = "";newBonus= BONUS.Rien;
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

                    if (epreuveProchaine && !pic && (!equals(choixTour, "quitter") || !equals(choixTour, "inventaire"))){
                        if (carreau){
                            multicarreau = nb_tours;
                            carreau = false;
                        }
                        if (trefle){
                            multitrefle = multitrefle *1.2;
                            trefle = false;
                        }

                        if (!doEpreuve(difficulte)){
                            if(random()<PROBA_BONUS){
                                vies -= 1;
                                score -= scoreMultiplieur(difficulte)*multicarreau *multitrefle+ 100;
                                println("Il s'agissait d'un bonus mais vous ne l'aurez pas...");

                            }else{
                                vies -= 1;
                                score -= scoreMultiplieur(difficulte)*multicarreau *multitrefle+ 100;
                                println();
                            }

                        }else{
                            println("Bonne réponse");

                            if(random()<PROBA_BONUS){
                                score += ( mouv + scoreMultiplieur(difficulte)*multicarreau*multitrefle ) + 100;
                                newBonus = selectionBonus();

                                if (!inventairePlein(inventaire)){
                                    ajouterBonus(inventaire, newBonus);
                                    println();
                                    println("Vous ajoutez "+ toStringBonus(newBonus)+" à votre inventaire !");
                                    println();
                                    println();
                                }else{
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
                                    enleverBonus(inventaire , inventaire_menu-1);
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
                                }
                            }else{
                                score += ( mouv + scoreMultiplieur(difficulte)*multicarreau*multitrefle ) + 100; 
                                println();
                            }
                        }
                        multicarreau = 1;pic = false;
                    }
                    epreuveProchaine = (plateau[prochaineCase(case_actuelle,mouv)] == Cases.EPREUVE);
                    choixTour = jouerTour(plateau , case_actuelle, prochaineCase(case_actuelle,mouv));
                    println(case_actuelle);
                    if(!equals(choixTour,"quitter") && !equals(choixTour, "inventaire")){
                        case_actuelle=prochaineCase(case_actuelle,mouv);
                    }
                    while ((equals(choixTour, "quitter") )&& !valide_quitter(quitter_menu)){
                        clearScreen();
                        afficherText(MENU_QUITTER);
                        /*
                        println("1 : Quitter sans sauvegarder");
                        println("2 : Quitter en sauvegardant");
                        println();
                        println("0 : Retour");
                        */
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
                            enleverBonus(inventaire , inventaire_menu-1);
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
                    


                    if (vies == 0){
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
            else if(menu == 2){ // STATISTIQUES
                while(!choixStatValide(choixStat)){
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
                    else{
                        clearScreen();
                        afficherText(MENU_STATISTIQUES);
                        println();

                        afficherPlayerStat(getPlayerStat(choixStat) );
                        println("\n\n\n");
                        println("            0 : Retour");
                        println("\n\n\n");

                        print("Mettez \"Entrée\" pour retourner au menu : ");
                        readString();
                        menu = 2;   
                        }
                    }
                }
            else if(menu == 3){ // CLASSEMENT
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
            else if (menu == 4){
                while(!paramValide(parametre)){
                    couleur = -1;
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
                    } else if (parametre == 1){
                        println();
                        print("Entrez un caractère : ");
                        joueur = readChar();
                        parametre = -1;
                    } else if (parametre == 2){
                        println();
                        print("Entrez un caractère : ");
                        chemin = readChar();
                        parametre = -1;
                    } else if (parametre == 3){
                        println();
                        print("Entrez un caractère : ");
                        epreuve = readChar();
                        parametre = -1;
                    } else if (parametre == 4){
                        while(!couleurValide(couleur) && parametre == 4){
                            clearScreen();
                            afficherText(MENU_COULEUR);
                            /*
                            println("Couleurs : ");
                            println();
                            println("1 : Rose");
                            println("2 : Bleu");
                            println("3 : Vert");
                            println("4 : Blanc");
                            println("5 : Noir");
                            println("6 : Reset");
                            println();
                            println("0 : Retour");
                            */
                            println();
                            print("Entrez un entier valide : ");
                            couleur = readInt();

                            if (couleur == 0){
                                parametre = -1;
                            } else if(couleur == 6){
                                reset(); couleur_base = "white";
                                couleur = -1;
                            } else if (couleur == 1){
                                couleur_base = "red";
                                couleur_epreuve = "blue";
                                text(couleur_base);
                                background("black");
                                couleur = -1;
                            } else if (couleur == 2){
                                couleur_base = "blue";
                                text(couleur_base);
                                background("white");
                                couleur = -1;
                            } else if (couleur == 3){
                                couleur_base = "green";
                                text(couleur_base);
                                background("black");
                                couleur = -1;
                            } else if (couleur == 4){
                                couleur_base = "white";
                                text(couleur_base);
                                background("black");
                                couleur = -1;
                            } else if (couleur == 5){
                                couleur_base = "black";
                                text(couleur_base);
                                background("white");
                                couleur = -1;
                            }
                        }
                    }
                }
            }else if(menu == 5){
                //print("Mettez \"Entrée\" pour retourner au menu : ");
                while(regle!=0){
                    if (regle==1){
                        clearScreen();
                        afficherText(MENU_REGLE_1);
                        print("Entrez un entier valide : ");
                        regle = readInt();
                    }else if(regle==2){
                        clearScreen();
                        afficherText(MENU_REGLE_2);
                        print("Entrez un entier valide : ");
                        regle = readInt();
                    }
                }
                menu = -1;
            }
            
            else if(menu == 0){
                clearScreen();
                break;
            } 
        }
    }
}

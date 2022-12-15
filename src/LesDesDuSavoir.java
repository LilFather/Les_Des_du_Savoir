import extensions.File;
import extensions.CSVFile;

class LesDesDuSavoir extends Program{
    char joueur = 'J';
    char chemin = '_';
    char epreuve = '?';

    String couleur_epreuve = "red";
    String couleur_base = "white";

    final int TAILLE_TABLEAU = 152;
    final double PROBA_EPREUVE = 0.33;

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
        println("Veuillez lancer le dé en appuyant sur \"Entrée\" ou entrer \"quitter\" pour quitter");
        String res = readString();
        int resultat = 0;
        if (equals(res ,"quitter")){
            deplacerJoueur(plateau , case_actuelle , prochaine);
            return res;
        } else {
            deplacerJoueur(plateau , case_actuelle , prochaine);
            return "";
        }
    }

    int epreuveMath(int difficulte){
        int nb1 = 0;
        int nb2 = 0;
        double res = 0.0;
        double proba = random();
        if (difficulte == 3){
            nb1 = (int) (random()*1000);
            nb2 = (int) (random()*1000);
        } else if (difficulte == 2){
            nb1 = (int) (random()*100);
            nb2 = (int) (random()*100);
        } else {
            nb1 = (int) (random()*10);
            nb2 = (int) (random()*10);     
        }

        if(proba<0.25){
            text(couleur_epreuve);
            println("           "+nb1+"/"+(nb2 + 1));
            text(couleur_base);
            return (nb1/(nb2+1));
        }else if (proba<0.5){
            text(couleur_epreuve);
            println("           "+nb1+"*"+nb2);
            text(couleur_base);
            return (nb1*nb2);
        }else if (proba<0.75){
            text(couleur_epreuve);
            println("           "+nb1+"+"+nb2);
            text(couleur_base);
            return (nb1+nb2);
        }else{
            text(couleur_epreuve);
            println("           "+nb1+"-"+nb2);
            text(couleur_base);
            return (nb1-nb2);
        }
    }
    boolean testerResultatMath(int res, int reponse){
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
        CSVFile profilJoueur = loadCSV("save_profil.csv");
        int nbL = rowCount(profilJoueur);

        for(int l=0; l<nbL; l++){
            String cell = getCell(profilJoueur, l, 0);
            print("            " + (l+1) + " : " + cell);

            println();
        }
    }

    String getJoueur(int choixJoueur){
        CSVFile profilJoueur = loadCSV("save_profil.csv");

        return getCell(profilJoueur, choixJoueur-1, 0);
    }

    String[] getPlayerStat(int choixStat){
        CSVFile profilJoueur = loadCSV("save_profil.csv");
        int col = columnCount(profilJoueur);

        String[] stat = new String[col];
        
        for(int i=0; i<col; i++){
            stat[i] = getCell(profilJoueur, choixStat-1, i);
        }

        return stat;
    }

    void afficherPlayerStat(String[] playerStat){
        CSVFile profilJoueur = loadCSV("save_profil.csv");
        int nbCol = columnCount(profilJoueur);

        println("Joueur : " + playerStat[0] + "\n");

        println("Parties totales : " + playerStat[1]);
        print("Parties joués en facile : " + playerStat[2] + "\t");
        print("Parties joués en moyen : " + playerStat[3] + "\t");
        println("Parties jouées en difficile : " + playerStat[4]);

        print("Score total : " + playerStat[5] + "\t");
        println("Meilleur score effectué : " + playerStat[6]);

        print("Tour joués : " + playerStat[7] + "\t");
        println("Tour joués en une partie : " + playerStat[8]);
    }

    void saveProfil(int choixProfil, String pseudo, int difficulte, int score, int nb_tours){
        CSVFile profilJoueur = loadCSV("save_profil.csv");
        int nbL = rowCount(profilJoueur);
        int nbCol = columnCount(profilJoueur);

        Profil p = creerProfil(pseudo, score, nb_tours);

        if(choixProfil == 1){ // Créer un profil
            String[][] chaines = new String[nbL+1][nbCol];

            for(int l=0; l<nbL; l++){
                for(int col=0; col<nbCol; col++){
                    chaines[l][col] = getCell(profilJoueur, l, col);
                } 
            }

            if(difficulte == 0){ // FACILE
                p.partie_facile = 1;
            }
            else if(difficulte == 1){ // MOYEN
                p.partie_moyen = 1;
            }
            else if(difficulte == 2){ // DIFFICILE
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

            saveCSV(chaines, "save_profil.csv");
        }
        else if(choixProfil == 2){ // Sauvegarde sur un profil existant
            String[][] chaines = new String[nbL][nbCol];

            for(int l=0; l<nbL; l++){
                
                if(equals(getCell(profilJoueur, l, 0), pseudo) ){
                    p.pseudo = pseudo;

                    p.nb_partie = 1 + stringToInt(getCell(profilJoueur, l, 1) );
                    if(difficulte == 0){
                        p.partie_facile = 1 + stringToInt(getCell(profilJoueur, l, 2) );
                    }
                    else if(difficulte == 1){
                        p.partie_moyen = 1 + stringToInt(getCell(profilJoueur, l, 3) );
                    }
                    else if(difficulte == 2){
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

            saveCSV(chaines, "save_profil.csv");
        }
    }

    int scoreMultiplieur(int difficulte){
        if(difficulte == 2){
            return 9;
        }
        else if(difficulte == 1){
            return 3;
        }
        else{
            return 1;
        }
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
        CSVFile profilJoueur = loadCSV("save_profil.csv");
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

    void algorithm(){
        afficherText("fixage.txt"); couleur_base = "white";
        reset();
        int difficulte = -1; int parametre = -1; int couleur = -1; int quitter_menu = -1;
        int choixProfil = -1; String pseudo = ""; int choixStat = -1;
        int case_actuelle = 0; String quit = "";
        int mouv = 0, nb_tours = 1;
        int res_epreuve_math = 0; int reponse_math = 0;
        int vies = 5; int menu = -1; int score = 0;
        Cases[] plateau = new Cases[TAILLE_TABLEAU];

        while(true){
            difficulte = -1; parametre = -1;
            choixProfil = -1; choixStat = -1; pseudo = "";
            nb_tours = 1; score = 0;
            clearScreen();
            afficherText("savoir.txt");
            println();
            /*println("1 : Nouvelle partie");
            println("2 : Reprendre une partie en cours");
            println("3 : Paramètres");
            println();
            println("0 : Quitter");
            */
            afficherText("menu.txt");
            while (!menuEntree(menu)){
                println();
                print("Veuillez entrez un entier valide : ");
                menu = readInt();
                println();
            }

            if (menu == 1){
                plateau = creerPlateau(TAILLE_TABLEAU , case_actuelle);

                while(!choixProfilValide(choixProfil)){
                    clearScreen();
                    afficherText("creerjoueurmenu.txt");
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
                        print("Choisir le joueur : ");
                        int choixJoueur = readInt();
                        pseudo = getJoueur(choixJoueur);
                    }
                }

                while(!difficulteValide(difficulte)){
                    clearScreen();
                    afficherText("difficulte.txt");
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

                while(menu==1){
                    quitter_menu = -1; quit = "";
                    clearScreen();
                    println(toString(plateau));
                    println();
                    println("           Joueur : " + pseudo);
                    println("           Vies restantes : " + vies);
                    println("           Tour : " + nb_tours);
                    println("           Score : " + score);
                    println();
                    mouv = movement();
                    nb_tours += 1;

                    if (plateau[prochaineCase(case_actuelle,mouv)] == Cases.EPREUVE){
                        res_epreuve_math = epreuveMath(difficulte);
                        println();
                        print("Entrez une réponse : ");
                        reponse_math = readInt();
                        println();

                        if (!testerResultatMath(res_epreuve_math , reponse_math)){
                            println("Mauvaise réponse, c'était " +res_epreuve_math);
                            vies -= 1;
                            score -= scoreMultiplieur(difficulte) + 100;
                            println();
                        }else{
                            println("Bonne réponse");
                            score += ( mouv + scoreMultiplieur(difficulte) ) + 100; 
                            println();
                        }
                    }
                    quit = jouerTour(plateau , case_actuelle, prochaineCase(case_actuelle,mouv));
                    while (equals(quit, "quitter") && !valide_quitter(quitter_menu)){
                        clearScreen();
                        afficherText("quitter_partie.txt");
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
                            menu = -1;
                        }
                        else if (quitter_menu == 0){
                            quitter_menu = -1;
                            quit = "";
                        }
                    }
                    case_actuelle=prochaineCase(case_actuelle,mouv);
                    if (vies == 0){
                        clearScreen();
                        afficherText("defaite.txt");
                        println(toString(plateau));
                        println();
                        println("           Joueur : " + pseudo);
                        println("           Tour : " + nb_tours);
                        println("           Score : " + score);
                        println();
                        println("Sauvegarde en cours...");
                        delay(2000);
                        saveProfil(choixProfil, pseudo, difficulte, score, nb_tours);
                    }
                }
            }
            else if(menu == 2){ // STATISTIQUES
                while(!choixStatValide(choixStat)){
                    clearScreen();
                    afficherText("statistiques.txt");
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
                        afficherText("statistiques.txt");
                        println();

                        afficherPlayerStat(getPlayerStat(choixStat) );
                        println("\n\n\n");
                        println("            0 : Retour");
                        println("\n\n\n");

                        print("Entrez un entier valide : ");
                        choixStat = readInt();
                        if(choixStat == 0){
                            menu = 2;
                        }
                    }
                }

            }
            else if(menu == 3){ // CLASSEMENT
                
            }
            else if (menu == 4){
                while(!paramValide(parametre)){
                    couleur = -1;
                    clearScreen();
                    afficherText("parametre.txt");
                    println();
                    println("           1 : Modifier caractère joueur : "+joueur);
                    println("           2 : Modifier caractère chemin : "+chemin);
                    println("           3 : Modifier caractère épreuve : "+ epreuve);
                    println("           4 : Modifier les couleurs");
                    println();
                    println("           0 : Retour");
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
                            afficherText("couleurs.txt");
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
                clearScreen();
                afficherText("regle.txt");
                print("Mettez \"Entrée\" pour retourner au menu : ");
                readString();
                menu = -1;
            }
            
            else if(menu == 0){
                clearScreen();
                break;
            } 
        }
    }
}
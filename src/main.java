import extensions.File;
import extensions.CSVFile;

class main extends Program{
    char joueur = 'J';
    char chemin = '#';
    char epreuve = '?';
    final int taille_tableau = 190;
    final double proba_epreuve = 0.33;


    int movement(){
        return (int) ((random()*6) +1);
    }

    char[]creerPlateau(int taille , int case_actuelle){
        char [] plateau = new char[taille];
        for (int i = 0 ; i<taille; i++){
            plateau[i]=chemin;
            if (random()<proba_epreuve && i!=0){
                plateau[i]=epreuve;
            }
        }
        plateau[case_actuelle] = joueur;
        return plateau;
    }

    String toString(char[] plateau){
        String res = "";
        int len = length(plateau);
        for(int i = 0;i<len ;i++){
            res += plateau[i];
        }
        return res;
    }

    void deplacerJoueur(char[] plateau , int case_actuelle, int prochaine){
        int resultat = 0;
        char tmp = plateau[prochaine];
        plateau[prochaine] = joueur;
        plateau[case_actuelle] = tmp;
    }

    int prochaineCase(int case_actuelle , int mouv){
        if (case_actuelle+mouv>=taille_tableau){
            return case_actuelle+mouv-taille_tableau;
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
    String jouerTour(char[] plateau, int case_actuelle, int prochaine){
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
        if (difficulte == 2){
            nb1 = (int) (random()*1000);
            nb2 = (int) (random()*1000);
        } else if (difficulte == 1){
            nb1 = (int) (random()*100);
            nb2 = (int) (random()*100);
        } else {
            nb1 = (int) (random()*10);
            nb2 = (int) (random()*10);     
        }

        if(proba<0.25){
            println(""+nb1+"/"+nb2 + 1);
            return (nb1/nb2);
        }else if (proba<0.5){
            println(""+nb1+"*"+nb2);
            return (nb1*nb2);
        }else if (proba<0.75){
            println(""+nb1+"+"+nb2);
            return (nb1+nb2);
        }else{
            println(""+nb1+"-"+nb2);
            return (nb1-nb2);
        }
    }
    boolean testerResultatMath(int res, int reponse){
        return (res==reponse);
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
        char[] plateau = creerPlateau(taille_tableau , case_actuelle);
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
    void afficherLogo(){
        File logo = newFile("savoir.txt");
        while(ready(logo)){
            println(readLine(logo));
        }
    }

    void afficherListeJoueur(){
        CSVFile profilJoueur = loadCSV("save_profil.csv");
        int nbL = rowCount(profilJoueur);

        for(int l=0; l<nbL; l++){
            String cell = getCell(profilJoueur, l, 0);
            print(l+1 + " : " + cell);
        }

        println();
    }

    String getJoueur(int choixJoueur){
        CSVFile profilJoueur = loadCSV("save_profil.csv");

        return getCell(profilJoueur, choixJoueur-1, 0);
    }

    void afficherProfil(){
        CSVFile profilJoueur = loadCSV("save_profil.csv");
        int nbL = rowCount(profilJoueur);
        int nbCol = columnCount(profilJoueur);

        for(int l=0; l<nbL; l++){
            for(int col=0; col<nbCol; col++){
                String cell = getCell(profilJoueur, l, col);
                print(cell + " ");
            }

            println();
        }
    }

    void saveProfil(int choixProfil, String pseudo, int difficulte, int score, int nb_tours){
        CSVFile profilJoueur = loadCSV("save_profil.csv");
        int nbL = rowCount(profilJoueur);

        String[][] chaines = new String[1][9];

        if(choixProfil == 1){ // Créer un profil
            Profil p = creerProfil(pseudo, score, nb_tours);

            if(difficulte == 0){ // FACILE
                p.partie_facile = 1;
            }
            else if(difficulte == 1){ // MOYEN
                p.partie_moyen = 1;
            }
            else if(difficulte == 2){ // DIFFICILE
                p.partie_difficile = 1;
            }

            chaines[0][0] = p.pseudo;
            chaines[0][1] = p.nb_partie + "";
            chaines[0][2] = p.partie_facile + "";
            chaines[0][3] = p.partie_moyen + "";
            chaines[0][4] = p.partie_difficile + "";
            chaines[0][5] = p.total_score + "";
            chaines[0][6] = p.best_score + "";
            chaines[0][7] = p.total_tour_joues + "";
            chaines[0][8] = p.best_tour_joues + "";

            saveCSV(chaines, "save_profil.csv");
        }
        else if(choixProfil == 2){ // Sauvegarde sur un profil existant

        }
    }

    int scoreMultiplieur(int difficulte){
        if(difficulte == 2){
            return 4;
        }
        else if(difficulte == 1){
            return 2;
        }
        else{
            return 1;
        }
    }

    boolean menuEntree(int menu){
        return (menu >= 0 && menu <=3);
    }

    boolean choixProfilValide(int choixProfil){
        return choixProfil >= 0 && choixProfil <= 2;
    }

    boolean difficulteValide(int difficulte){
        return (difficulte>=0 && difficulte <=3);
    }

    boolean paramValide(int param){
        return (param>=0 && param <=4);
    }
    boolean couleurValide(int couleur){
        return (couleur>=0 && couleur <= 6);
    }

    boolean valide_quitter(int quitter){
        return (quitter>=0 && quitter <= 1);
    }

    void algorithm(){
        int difficulte = -1; int parametre = -1; int couleur = -1; int quitter_menu = -1;
        int choixProfil = -1; String pseudo = "";
        int case_actuelle = 0; String quit = "";
        int mouv = 0, nb_tours = 1;
        int res_epreuve_math = 0; int reponse_math = 0;
        int vies = 5; int menu = -1; int score = 0;
        char[] plateau = new char[taille_tableau];

        while(true){
            difficulte = -1; parametre = -1;
            clearScreen();
            afficherLogo();
            println();
            println("0 : Nouvelle partie");
            println("1 : Reprendre une partie en cours");
            println("2 : Paramètres");
            println("3 : Quitter");

            while (!menuEntree(menu)){
                println();
                print("Veuillez entrez un entier valide : ");
                menu = readInt();
                println();
            }

            if (menu == 0){
                plateau = creerPlateau(taille_tableau , case_actuelle);

                while(!choixProfilValide(choixProfil)){
                    clearScreen();

                    println("1 : Créer un joueur");
                    println("2 : Jouer avec un joueur existant");

                    println();
                    println("0 : Retour");

                    println();
                    print("Entez un choix valide: ");
                    choixProfil = readInt();
                    println();

                    if(choixProfil == 0){
                        menu = -1;
                    }
                    else if(choixProfil == 1){
                        clearScreen();

                        println("Entrez un pseudonyme:\n");
                        pseudo = readString();
                    }
                    else if(choixProfil == 2){
                        clearScreen();
                        afficherListeJoueur();

                        println();
                        println("Choisir le joueur:\n");
                        int choixJoueur = readInt();
                        pseudo = getJoueur(choixJoueur);
                    }
                }

                while(!difficulteValide(difficulte)){
                    clearScreen();

                    println("0 : Facile");
                    println("1 : Moyen");
                    println("2 : Difficile");
                    println();
                    println("3 : Retour");
                    println();
                    print("Entrez une difficulté valide : ");
                    difficulte = readInt();
                    println();
                    if (difficulte == 3){
                        menu = -1;
                    }
                }    

                while(vies>0 && menu==0){
                    quitter_menu = -1; quit = "";
                    clearScreen();
                    println(toString(plateau));
                    println();
                    println("Joueur : " + pseudo);
                    println("Vies restantes : " + vies);
                    println("Tour : " + nb_tours);
                    println("Score : " + score);
                    println();
                    mouv = movement();
                    nb_tours += 1;

                    if (plateau[prochaineCase(case_actuelle,mouv)] == epreuve){
                        res_epreuve_math = epreuveMath(difficulte);
                        println();
                        print("Entrez une réponse : ");
                        reponse_math = readInt();
                        println();

                        if (!testerResultatMath(res_epreuve_math , reponse_math)){
                            println("Mauvaise réponse");
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
                        println("0 : Quitter sans sauvegarder");
                        println("1 : Quitter en sauvegardant");
                        println();
                        println("2 : Retour");
                        println();
                        print("Entrez un entier valide : ");
                        quitter_menu = readInt();
                        if (quitter_menu == 0){
                            menu = -1;
                        } else if(quitter_menu == 1){
                            saveProfil(choixProfil, pseudo, difficulte, score, nb_tours);
                            println("Sauvegarde effectuée !");
                            menu = -1;
                        }
                        else if (quitter_menu == 2){
                            quitter_menu = -1;
                            quit = "";
                        }
                    }
                    case_actuelle=prochaineCase(case_actuelle,mouv);
                }
            }

            else if (menu == 1){
                println("Pas encore fait");
            }

            else if (menu == 2){
                while(!paramValide(parametre)){
                    couleur = -1;
                    clearScreen();
                    println("Paramètres : ");
                    println();
                    println("0 : Modifier caractère joueur : "+joueur);
                    println("1 : Modifier caractère chemin : "+chemin);
                    println("2 : Modifier caractère épreuve : "+ epreuve);
                    println("3 : Modifier les couleurs");
                    println();
                    println("4 : Retour");
                    println();
                    print("Entrez un entier valide : ");
                    parametre = readInt();
                    if (parametre == 4){
                        menu = -1;
                    } else if (parametre == 0){
                        println();
                        print("Entrez un caractère : ");
                        joueur = readChar();
                        parametre = -1;
                    } else if (parametre == 1){
                        println();
                        print("Entrez un caractère : ");
                        chemin = readChar();
                        parametre = -1;
                    } else if (parametre == 2){
                        println();
                        print("Entrez un caractère : ");
                        epreuve = readChar();
                        parametre = -1;
                    } else if (parametre == 3){
                        while(!couleurValide(couleur) && parametre == 3){
                            clearScreen();
                            println("Couleurs : ");
                            println();
                            println("0 : Rouge");
                            println("1 : Bleu");
                            println("2 : Vert");
                            println("3 : Blanc");
                            println("4 : Noir");
                            println("5 : Reset");
                            println();
                            println("6 : Retour");
                            println();
                            print("Entrez un entier valide : ");
                            couleur = readInt();

                            if (couleur == 6){
                                parametre = -1;
                            } else if(couleur == 5){
                                reset();
                                couleur = -1;
                            } else if (couleur == 0){
                                text("red");
                                background("black");
                                couleur = -1;
                            } else if (couleur == 1){
                                text("blue");
                                background("white");
                                couleur = -1;
                            } else if (couleur == 2){
                                text("green");
                                background("black");
                                couleur = -1;
                            } else if (couleur == 3){
                                text("white");
                                background("black");
                                couleur = -1;
                            } else if (couleur == 4){
                                text("black");
                                background("white");
                                couleur = -1;
                            }
                        }
                    }
                }
            }else if(menu == 3){
                clearScreen();
                break;
            } 
        }
    }
}
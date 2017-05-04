package classifier;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Bayes {

    private Dictionary dictionary;
    private HashMap<String, Double> vectorHam;
    private HashMap<String, Double> vectorSpam;

    private HashMap<String, Double> vectorBjHam;
    private HashMap<String, Double> vectorBjSpam;
    public static final int epsilon = 1;

    private final static String BASE_APP_HAM = "./base/baseapp/ham/";
    private final static String BASE_APP_SPAM = "./base/baseapp/spam/";

    private double pSpam;

    public Bayes(Dictionary d) {
        this.dictionary = d;

        // Indique le nombre se Ham (ou Spam) contenant le mot correspondant
        this.vectorHam = new HashMap<>();
        this.vectorSpam = new HashMap<>();
        this.vectorBjHam = new HashMap<>();
        this.vectorBjSpam = new HashMap<>();

        for (String word : dictionary.getBase()) {
            vectorHam.put(word, 0.0);
            vectorSpam.put(word, 0.0);

            // probabilité de voir le mot j dans un Ham (ou Spam)
            vectorBjHam.put(word, 0.0);
            vectorBjSpam.put(word, 0.0);
        }
    }

    public void analysisBaseApp(String pathBaseTest, int spamApp, int hamApp, int spamTest, int hamTest) {

        pSpam = (double) spamApp / (double) (hamApp + spamApp);

        this.calculStatHam(hamApp);

        this.calculStatSpam(spamApp);

        int errSpam = 0;
        int errHam = 0;

        System.out.println("\nTest :");
        String[] filesName = new File(pathBaseTest+"/spam").list();
        for(int i = 0; i < spamTest; i++){
            Message m = new Message(pathBaseTest+File.separator+"/spam/"+filesName[i], this.dictionary);
            if(!filtreGeneratif(m.getVector())){
                System.out.println("SPAM numéro "+i+" identifié comme un SPAM");
            }else{
                System.out.println("SPAM numéro "+i+" identifié comme un HAM  *** erreur ***");
                errSpam++;
            }
        }

        filesName = new File(pathBaseTest+File.separator+"/ham").list();
        for(int i = 0; i < spamTest; i++){
            Message m = new Message(pathBaseTest+File.separator+"/ham/"+filesName[i], this.dictionary);
            if(filtreGeneratif(m.getVector())){
                System.out.println("HAM numéro "+i+" identifié comme un HAM");
            }else{
                System.out.println("HAM numéro "+i+" identifié comme un SPAM *** erreur ***");
                errHam++;
            }
        }

        System.out.println("\nErreur de test sur les "+spamTest+" SPAM      : "+(errSpam/spamTest)*100+"%");
        System.out.println("Erreur de test sur les "+hamTest+" HAM       : "+(errHam/hamTest)*100+"%");
        System.out.println("Erreur de test globale sur "+(spamTest+hamTest)+" mails : "+(errHam+errSpam)/(spamTest+hamTest)+"%\n");
        System.out.println("Fin d'apprentissage");
    }

    public void calculStatHam(int mHam) {
        // HAM  ---------- ---------- ---------- ---------- ---------- ---------- ---------- ----------
        System.out.println("Apprentissage HAM ...");

        // recuperation des fichers d'apprentissage des HAM
        String[] filesName = new File(Bayes.BASE_APP_HAM).list();

        // Pour chaque mail on lit le message e
        for (int cmpt = 0; cmpt < mHam; cmpt++) {
            // on lit le message
            Message m = new Message(Bayes.BASE_APP_HAM + filesName[cmpt], this.dictionary);
            // on regarde les mots du message
            for (Map.Entry<String, Integer> entry : m.getVector().entrySet())
                if (entry.getValue() != 0)
                    this.vectorHam.put(entry.getKey(), this.vectorHam.get(entry.getKey()) + 1);
        }


        // Calcul des stats des mots des HAM -> BjHAM
        for (Map.Entry<String, Double> entry : this.vectorHam.entrySet())
            // Si c'est egale à 0 pas besoin de faire un put car vecteur déjà initilisé
            if (entry.getValue() != 0)
                this.vectorBjHam.put(entry.getKey(), (this.vectorHam.get(entry.getKey()) + epsilon) / (mHam + 2 * epsilon));


    }

    public void calculStatSpam(int mSpam) {
        // SPAM ---------- ---------- ---------- ---------- ---------- ---------- ---------- ----------
        System.out.println("Apprentissage SPAM ...");

        // recuperation des fichers d'apprentissage des SPAM
        String[] filesName = new File(Bayes.BASE_APP_SPAM).list();

        for (int cmpt = 0; cmpt < mSpam; cmpt++) {
            // on lit le message
            Message m = new Message(Bayes.BASE_APP_SPAM + filesName[cmpt], this.dictionary);
            // on regarde les mots du message
            for (Entry<String, Integer> entry : m.getVector().entrySet())
                if (entry.getValue() != 0)
                    this.vectorSpam.put(entry.getKey(), this.vectorSpam.get(entry.getKey()) + 1);
        }

        // Calcul des stats des mots des SPAM -> BjSPAM
        for (Map.Entry<String, Double> entry : this.vectorSpam.entrySet())
            // Si c'est egale à 0 pas besoin de faire un put car vecteur déjà initilisé
            if (entry.getValue() != 0)
                this.vectorBjSpam.put(entry.getKey(), (this.vectorSpam.get(entry.getKey()) + epsilon) / (mSpam + 2 * epsilon));
    }

    public boolean filtreGeneratif(HashMap<String, Integer> vectorX) {
        boolean res;
        // P(X=x|Y=SPAM)
        double pXSachantSpam = 1.0;
        double pXSachantHam = 1.0;

        for (Map.Entry entry : vectorX.entrySet()) {
            if ((double) ((Integer) entry.getValue()) == 0.0) pXSachantSpam += Math.log(1.0 - vectorBjSpam.get(entry.getKey()));
            else pXSachantSpam += Math.log(vectorBjSpam.get(entry.getKey()));


            if ((double) ((Integer) entry.getValue()) == 0.0) pXSachantHam += Math.log(1.0 - vectorHam.get(entry.getKey()));
            else pXSachantHam += Math.log(vectorHam.get(entry.getKey()));
        }
        pXSachantSpam += Math.log(pSpam);
        pXSachantHam += Math.log(1.0 - pSpam);

        // P(X=x|Y=HAM)
        /*for (Map.Entry entry : vectorX.entrySet()) {
        }*/

        // P(X=x) = P(X=x|Y=HAM) + P(X=x|Y=SPAM)
        double pX = pXSachantHam + pXSachantSpam;

        // P(Y=SPAM|X=x)
        double pSpamSachantX = Math.log(pSpam) + pXSachantSpam;
        pSpamSachantX /= pX;

        // P(Y=HAM|X=x)
        double pHamSachantX = Math.log(1.0 - pSpam) + pXSachantHam;
        pHamSachantX /= pX;

        if(pSpamSachantX > pHamSachantX) res = false;
        else res = true;

        return res;
    }

}

package classifier;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Bayes implements Serializable {

    private Dictionary dictionary;
    private HashMap<String, Double> vectorHam;

    public HashMap<String, Double> getVectorHam() {
        return vectorHam;
    }

    public HashMap<String, Double> getVectorSpam() {
        return vectorSpam;
    }

    private HashMap<String, Double> vectorSpam;

    // proba d'avoir le mot j dans un Ham ou Spam
    private HashMap<String, Double> vectorBjHam;
    private HashMap<String, Double> vectorBjSpam;
    public static final int epsilon = 1;

    private static String BASE_APP_HAM = "./base/baseapp/ham/";
    private static String BASE_APP_SPAM = "./base/baseapp/spam/";

    private double pSpam;
    private double pPosterioriSpam, pPosterioriHam;

    public Bayes(Dictionary d, String pathBase, int spamApp, int hamApp) {
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

        BASE_APP_HAM = pathBase + "/ham/";
        BASE_APP_SPAM = pathBase + "/spam/";


        this.calculStatHam(hamApp);

        this.calculStatSpam(spamApp);
    }

    public void analysisBaseApp(String pathBaseTest, int spamTest, int hamTest) {

        // P(Y = SPAM)
        pSpam = (double) spamTest / (double) (hamTest + spamTest);

        int errSpam = 0;
        int errHam = 0;

        // On peut afficher les proba a posteriori des deux catégories
        System.out.println("\nTest :");
        String[] filesName = new File(pathBaseTest + "/spam").list();
        for (int i = 0; i < spamTest && i < filesName.length; i++) {
            Message m = new Message(pathBaseTest + File.separator + "/spam/" + filesName[i], this.dictionary);
            if (!filtreGeneratif(m.getVector())) {
                System.out.println("SPAM numéro " + i + " identifié comme un SPAM");
            } else {
                System.err.println("SPAM numéro " + i + " identifié comme un HAM");
                errSpam++;
            }
        }

        filesName = new File(pathBaseTest + File.separator + "/ham").list();
        for (int i = 0; i < hamTest && i < filesName.length; i++) {
            Message m = new Message(pathBaseTest + File.separator + "/ham/" + filesName[i], this.dictionary);
            if (filtreGeneratif(m.getVector())) {
                System.out.println("HAM numéro " + i + " identifié comme un HAM");
            } else {
                System.err.println("HAM numéro " + i + " identifié comme un SPAM");
                errHam++;
            }
        }

        double errSpamFinal = ((double) errSpam / (double) spamTest) * 100.0;
        double errHamFinal = ((double) errHam / (double) hamTest) * 100.0;
        int nbTotalTest = spamTest + hamTest;
        double errFinal = ((double) (errHam + errSpam) / (double) nbTotalTest) * 100.0;
        System.out.println("\nErreur de test sur les " + spamTest + " SPAM      : " + errSpamFinal + "%");
        System.out.println("Erreur de test sur les " + hamTest + " HAM       : " + errHamFinal + "%");
        System.out.println("Erreur de test globale sur " + nbTotalTest + " mails : " + errFinal + "%\n");
        System.out.println("Fin d'apprentissage");

    }

    public void calculStatHam(int mHam) {
        // HAM  ---------- ---------- ---------- ---------- ---------- ---------- ---------- ----------
        System.out.println("Apprentissage HAM ...");

        // recuperation des fichers d'apprentissage des HAM
        String[] filesName = new File(Bayes.BASE_APP_HAM).list();

        // Pour chaque mail on lit le message e
        for (int cmpt = 0; cmpt < mHam && cmpt < filesName.length; cmpt++) {
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

        for (int cmpt = 0; cmpt < mSpam && cmpt < filesName.length; cmpt++) {
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

    /**
     * Retourne vrai si le mot du vectorX est identifié comme un HAM
     *
     * @param vectorX
     * @return
     */
    public boolean filtreGeneratif(HashMap<String, Integer> vectorX) {
        boolean res;
        // P(X = x | Y = SPAM)
        double pXSachantSpam = Math.log(pSpam);
        double pXSachantHam = Math.log(1.0 - pSpam);

        for (Map.Entry entry : vectorX.entrySet()) {
            if ((double) ((Integer) entry.getValue()) == 0.0)
                pXSachantSpam += Math.log(1.0 - vectorBjSpam.get(entry.getKey()));
            else
                pXSachantSpam += Math.log(vectorBjSpam.get(entry.getKey()));

            if ((double) ((Integer) entry.getValue()) == 0.0)
                pXSachantHam += Math.log(1.0 - vectorBjHam.get(entry.getKey()));
            else
                pXSachantHam += Math.log(vectorBjHam.get(entry.getKey()));
        }

        // P(X = x) = P(X = x | Y = HAM) + P(X = x | Y = SPAM)
        double pX = pXSachantHam + pXSachantSpam;

        // P(Y = SPAM | X = x)
        //double pSpamSachantX = Math.log(pSpam) + pXSachantSpam;
        //pPosterioriSpam = pSpamSachantX - pX;

        // P(Y = HAM | X = x)
        //double pHamSachantX = Math.log(1.0 - pSpam) + pXSachantHam;
        //pPosterioriHam = pHamSachantX - pX;

        pPosterioriHam = 1.0 / (1 + Math.exp(pXSachantSpam - pXSachantHam));
        pPosterioriSpam = 1.0 / (1 + Math.exp(pXSachantHam - pXSachantSpam));
        // Nécessaire de faire Math.exp ???
        //if (Math.exp(pPosterioriSpam) > Math.exp(pPosterioriHam)) res = false;
        if (pPosterioriSpam > pPosterioriHam) res = false;
        else res = true;

        return res;
    }

    public void setDictionary(Dictionary dico) {
        this.dictionary = dico;
    }

    public Dictionary getDictionary() {
        return this.dictionary;
    }

    public void classifierMail(String pathMail) {
        Message m = new Message(pathMail, this.dictionary);
        if (!filtreGeneratif(m.getVector())) {
            System.out.println("Mail identifié comme un SPAM");
        } else {
            System.err.println("Mail identifié comme un HAM");
        }
    }

}

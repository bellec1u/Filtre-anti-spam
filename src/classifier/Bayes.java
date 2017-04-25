package classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Bayes {

	private Dictionary dictionary;
	private HashMap<String, Double> vectorHam;
	private HashMap<String, Double> vectorSpam;

	private final static String BASE_APP_HAM = "./base/baseapp/ham/";
	private final static String BASE_APP_SPAM = "./base/baseapp/spam/";

	public Bayes(Dictionary d) {
		this.dictionary = d;

		this.vectorHam = new HashMap<String, Double>();
		this.vectorSpam = new HashMap<String, Double>();

		for (String word : dictionary.getBase()) {
			vectorHam.put(word, 0.0);
			vectorSpam.put(word, 0.0);
		}
	}

	public void analysisBaseApp(int mHam, int mSpam) {	

		this.calculStatHam(mHam);
		
		this.calculStatSpam(mSpam);

		
		// P(Y = SPAM) --- ---------- ---------- ---------- ---------- ---------- ---------- ----------

		double pSpam = mSpam / (mHam + mSpam);



		// Probabilité a posteriori-- ---------- ---------- ---------- ---------- ---------- ----------

		double pSpamSachantX = 0;





		// comparaison des valeures obtenues
		for (Map.Entry entry : this.vectorHam.entrySet()) {
			System.out.println(entry.getKey()+" \t "+entry.getValue()+" \t "+
					this.vectorSpam.get(entry.getKey()));
		}





		System.out.println("Fin d'apprentissage");
	}

	public void calculStatHam(int mHam) {
		// HAM  ---------- ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Apprentissage HAM ...");

		// recuperation des fichers d'apprentissage des HAM
		String[] filesName = new File( Bayes.BASE_APP_HAM ).list();

		for (int cmpt = 0; cmpt < mHam; cmpt++) {
			// on lit le message
			Message m = new Message(Bayes.BASE_APP_HAM + filesName[cmpt], this.dictionary);
			// on regarde les mots du message
			for (Map.Entry<String, Integer> entry : m.getVector().entrySet())
				if (entry.getValue() != 0)
					this.vectorHam.put(entry.getKey(), this.vectorHam.get(entry.getKey()) + 1);	
		}

		// Calcul des stats des mots des HAM
		for (Map.Entry<String, Double> entry : this.vectorHam.entrySet())
			if (entry.getValue() != 0)
				this.vectorHam.put(entry.getKey(), this.vectorHam.get(entry.getKey()) / mHam);
	}

	public void calculStatSpam(int mSpam) {
		// SPAM ---------- ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Apprentissage SPAM ...");

		// recuperation des fichers d'apprentissage des SPAM
		String[] filesName = new File( Bayes.BASE_APP_SPAM ).list();

		for (int cmpt = 0; cmpt < mSpam; cmpt++) {
			// on lit le message
			Message m = new Message(Bayes.BASE_APP_SPAM + filesName[cmpt], this.dictionary);
			// on regarde les mots du message
			for (Entry<String, Integer> entry : m.getVector().entrySet())
				if (entry.getValue() != 0)
					this.vectorSpam.put(entry.getKey(), this.vectorSpam.get(entry.getKey()) + 1);
		}

		// Calcul des stats des mots des SPAM
		for (Map.Entry<String, Double> entry : this.vectorSpam.entrySet())
			if (entry.getValue() != 0)
				this.vectorSpam.put(entry.getKey(), this.vectorSpam.get(entry.getKey()) / mSpam);
	}
	
	public void filtreGeneratif() {
		// P(X=x|Y=...) -- ---------- ---------- ---------- ---------- ---------- ---------- ----------

		double pXSachantSpam = 1;
		for (Map.Entry entry : this.vectorSpam.entrySet())
			if (entry.getValue() == 0)
				pXSachantSpam *= 
	}

}

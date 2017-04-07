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
		
		this.vectorHam = new HashMap<String, Double>(d.getBase().size());
		for (String word : dictionary.getBase())
			vectorHam.put(word, 0.0);
		
		this.vectorSpam = new HashMap<String, Double>(d.getBase().size());
		for (String word : dictionary.getBase())
			vectorSpam.put(word, 0.0);
	}

	public void analysisBaseApp(int mHam, int mSpam) {		
		// HAM  ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Apprentissage HAM ...");
		String[] filesName = new File( Bayes.BASE_APP_HAM ).list();
		int cmpt = 0;
		while (cmpt < mHam || cmpt < filesName.length) {
			Message m = new Message(Bayes.BASE_APP_HAM + filesName[cmpt], this.dictionary);
			for (Map.Entry<String, Integer> entry : m.getVector().entrySet())
				if (entry.getValue() != 0)
					// on compte combien de fois on retrouve le meme mot dans les HAM
					this.vectorHam.put(entry.getKey(), this.vectorHam.get(entry.getKey()) + 1);	
			cmpt++;
		}
		
		// Calcul des stats des mots des HAM
		for (Map.Entry<String, Double> entry : this.vectorHam.entrySet())
			if (entry.getValue() != 0)
				// on compte combien de fois on retrouve le meme mot dans les HAM
				this.vectorHam.put(entry.getKey(), 
						this.vectorHam.get(entry.getKey()) / mHam);
		
		// SPAM ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Apprentissage SPAM ...");
		filesName = new File( Bayes.BASE_APP_SPAM ).list();
		cmpt = 0;
		while (cmpt < mHam || cmpt < filesName.length) {
			Message m = new Message(Bayes.BASE_APP_SPAM + filesName[cmpt], this.dictionary);
			for (Entry<String, Integer> entry : m.getVector().entrySet())
				if (entry.getValue() != 0)
					// on compte combien de fois on retrouve le meme mot dans les SPAM
					this.vectorSpam.put(entry.getKey(), this.vectorSpam.get(entry.getKey()) + 1);
			cmpt++;
		}

		// Calcul des stats des mots des SPAM
		for (Map.Entry<String, Double> entry : this.vectorSpam.entrySet())
			if (entry.getValue() != 0)
				// on compte combien de fois on retrouve le meme mot dans les HAM
				this.vectorSpam.put(entry.getKey(), 
						this.vectorSpam.get(entry.getKey()) / mSpam);
		
		
		
		
		
		// comparaison des valeures obtenues
		for (Map.Entry entry : this.vectorHam.entrySet()) {
			System.out.println(entry.getKey()+" \t "+entry.getValue()+" \t "+
						this.vectorSpam.get(entry.getKey()));
		}
		
		System.out.println("Fin d'apprentissage");
	}


}

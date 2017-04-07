package classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bayes {

	private Dictionary dictionary;
	private HashMap<String, Integer> vectorHam;
	private HashMap<String, Integer> vectorSpam;

	private final static String BASE_APP_HAM = "./base/baseapp/ham/";
	private final static String BASE_APP_SPAM = "./base/baseapp/spam/";

	public Bayes(Dictionary d) {
		this.dictionary = d;
		
		this.vectorHam = new HashMap<String, Integer>(d.getBase().size());
		for (String word : dictionary.getBase())
			vectorHam.put(word, 0);
		
		this.vectorSpam = new HashMap<String, Integer>(d.getBase().size());
		for (String word : dictionary.getBase())
			vectorSpam.put(word, 0);
	}

	public void analysisBaseApp(int mHam, int mSpam) {		
		// HAM  ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Apprentissage HAM ...");
		ArrayList<Message> messages = new ArrayList<Message>();
		String[] filesName = new File( Bayes.BASE_APP_HAM ).list();
		int cmpt = 0;
		while (cmpt < mHam || cmpt < filesName.length) {
			messages.add( new Message(Bayes.BASE_APP_HAM + filesName[cmpt], this.dictionary) );
			cmpt++;
		}
		
		// calcul des probas pour les HAM
		for (Message m : messages) {
			for (Map.Entry<String, Integer> entry : m.getVector().entrySet()) {
				if (entry.getValue() != 0)
					this.vectorHam.put(entry.getKey(), this.vectorHam.get(entry.getKey()) + 1);
			}	
		}
		for (Map.Entry<String, Integer> entry : this.vectorHam.entrySet()) {
			System.out.println(entry.getKey() + " \t "+entry.getValue());
		}

		// SPAM ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Apprentissage SPAM ...");
		messages = new ArrayList<Message>();
		filesName = new File( Bayes.BASE_APP_SPAM ).list();
		cmpt = 0;
		while (cmpt < mHam || cmpt < filesName.length) {
			messages.add( new Message(Bayes.BASE_APP_SPAM + filesName[cmpt], this.dictionary) );
			cmpt++;
		}

		System.out.println("Fin d'apprentissage");
	}


}

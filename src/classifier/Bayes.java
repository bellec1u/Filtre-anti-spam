package classifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bayes {

	private Dictionary dictionary;
	private ArrayList<Message> message;

	private final static String BASE_APP_HAM = "./base/baseapp/ham/";
	private final static String BASE_APP_SPAM = "./base/baseapp/spam/";

	public Bayes(Dictionary d) {
		this.dictionary = d;
		this.message = new ArrayList<Message>();
	}

	public void analysisBaseApp(int mHam, int mSpam) {
		// ham  ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Analyse des ham de baseapp ...");
		String[] filesName = new File( this.BASE_APP_HAM ).list();
		int cmpt = 0;
		while (cmpt < mHam || cmpt < filesName.length) {
			this.message.add( new Message(this.BASE_APP_HAM + filesName[cmpt], this.dictionary) );
			cmpt++;
		}

		// spam ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Analyse des spam de baseapp ...");
		filesName = new File( this.BASE_APP_SPAM ).list();
		cmpt = 0;
		while (cmpt < mHam || cmpt < filesName.length) {
			this.message.add( new Message(this.BASE_APP_SPAM + filesName[cmpt], this.dictionary) );
			cmpt++;
		}

		System.out.println("Fin d'analyse");
	}

}

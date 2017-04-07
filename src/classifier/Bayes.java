package classifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bayes {

	private Dictionary dictionary;
	private ArrayList<Message> message;

	private static String BASE_APP_HAM = "./base/baseapp/ham/";
	private static String BASE_APP_SPAM = "./base/baseapp/spam/";
	
	public Bayes(Dictionary d) {
		this.dictionary = d;
		this.message = new ArrayList<Message>();
	}

	public void analysisBaseApp() {
		// ham  ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Analyse des ham de baseapp ...");
		String[] filesName = new File( this.BASE_APP_HAM ).list();
		for (String s : filesName)
			this.message.add( new Message(this.BASE_APP_HAM + s, this.dictionary) );

		// spam ---------- ---------- ---------- ---------- ---------- ---------- ----------
		System.out.println("Analyse des spam de baseapp ...");
		filesName = new File( this.BASE_APP_SPAM ).list();
		for (String s : filesName)
			this.message.add( new Message(this.BASE_APP_SPAM + s, this.dictionary) );
		
		System.out.println("Fin d'analyse");
	}

}

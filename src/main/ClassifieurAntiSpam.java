package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import classifier.Bayes;

public class ClassifieurAntiSpam {

	/**
	 * charge une classifieur et analyse la base de test avec
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 4)
			throw new Error("java ClassifieurAntiSpam "
					+ "<@Classifieur> <@BaseTest> <NbSpamTest> <NbHamTest>");

		Bayes b = charger_classifieur(args[0]);

		int spamTest = Integer.parseInt(args[2]);
		int hamTest = Integer.parseInt(args[3]);

		b.analysisBaseApp(args[1], spamTest, hamTest);
	}

	static Bayes charger_classifieur(String filename) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream( new FileInputStream( new File(filename) ) );			
			Bayes bayes = (Bayes) ois.readObject();			
			ois.close();

			return bayes;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

}

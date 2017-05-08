package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import classifier.Bayes;

public class ClassifierMail {

	public static void main(String[] args) {
		if (args.length < 4)
			throw new Error("java ClassifierMail "
					+ "<@Classifieur> <@MailAClassifier>");
		
		Bayes b = charger_classifieur(args[0]);

		b.classifierMail(args[1]);
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

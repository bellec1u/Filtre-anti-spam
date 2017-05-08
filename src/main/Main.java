package main;

import classifier.Bayes;
import classifier.Dictionary;

import java.io.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		if (args.length < 3) {
			throw new Error("java Main <adresseBaseTest> <nbSpamTest> <nbHamTest> \n"
					+ "ou\n"
					+ "java Main <adresseClassifieur> <adresseBaseTest> <nbSpamTest> <nbHamTest>");
		}else {
			if (args.length == 3) {
				Scanner sc = new Scanner(System.in);
				System.out.println("Combien de SPAM dans la base d'apprentissage ? ");
				int spamApp = sc.nextInt();
				System.out.println("Combien de HAM dans la base d'apprentissage ? ");
				int hamApp = sc.nextInt();
				
				Dictionary d = new Dictionary();
				//Bayes b = new Bayes(d, spamApp, hamApp);
				
				int spamTest = Integer.parseInt(args[1]);
				int hamTest = Integer.parseInt(args[2]);

				//b.analysisBaseApp(args[0], spamTest, hamTest);
				
				//sauvegarder_classifieur(d, b);
			} else if (args.length == 4) {
				// on a un classifieur a charger
				Bayes bayes = charger_classifieur(args[0]);

				int spamTest = Integer.parseInt(args[2]);
				int hamTest = Integer.parseInt(args[3]);

				bayes.analysisBaseApp(args[1], spamTest, hamTest);
			}
		}
	}

	static void sauvegarder_classifieur(Dictionary dico, Bayes bayes) {
		String filename = "classifieurs/classifieur_" + System.currentTimeMillis() + ".objet";

		try {
			ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( new File(filename) ) );
			oos.writeObject(bayes);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Sauvegarde du classifieur : " + filename);
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

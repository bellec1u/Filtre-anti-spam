package main;

import java.util.Scanner;

import classifier.Bayes;
import classifier.Dictionary;

public class FiltreAntiSpam {

	/**
	 * cree un classifieur grace a la base d'apprentissage
	 * et test la base de test avec
	 * @param args
	 */
	public static void main (String[] args) {
		if (args.length < 4)
			throw new Error("java FiltreAntiSpam "
					+ "<@BaseApprentissage> <@BaseTest> <NbSpamTest> <NbHamTest>");
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Combien de SPAM dans la base d'apprentissage ? ");
		int spamApp = sc.nextInt();
		System.out.println("Combien de HAM dans la base d'apprentissage ? ");
		int hamApp = sc.nextInt();
		
		Dictionary d = new Dictionary();
		Bayes b = new Bayes(d, args[0], spamApp, hamApp);
		
		int spamTest = Integer.parseInt(args[2]);
		int hamTest = Integer.parseInt(args[3]);

		b.analysisBaseApp(args[1], spamTest, hamTest);
	}
	
}

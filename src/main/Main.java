package main;

import classifier.Bayes;
import classifier.Dictionary;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
	    if(args.length < 3){
	        throw new Error("Vous devez saisir 3 parametres: basetest nbSpam nbHam");
        }else {
            String pathBaseTest = args[0];
            Dictionary d = new Dictionary();
            Bayes b = new Bayes(d);

            int spamTest = Integer.parseInt(args[1]);
            int hamTest = Integer.parseInt(args[2]);
            Scanner sc = new Scanner(System.in);
            System.out.println("Combien de SPAM dans la base d'apprentissage ? ");
            int spamApp = sc.nextInt();
            System.out.println("Combien de HAM dans la base d'apprentissage ? ");
            int hamApp = sc.nextInt();
            b.analysisBaseApp(pathBaseTest, spamApp, hamApp, spamTest, hamTest);
        }
	}

}

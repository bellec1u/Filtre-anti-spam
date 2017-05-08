package main;

import classifier.Bayes;
import classifier.Dictionary;

import java.io.*;

/**
 * Created by mulhauser on 08/05/2017.
 */
public class CreationClassifieur {

    public static void main(String args[]){
        if(args.length != 4){
            throw new Error("java CreationClassifieur <@classifieur> <@baseApp> <nbSpamApp> <nbHamApp>");
        }else{
            Dictionary d = new Dictionary();
            int nbSpam = Integer.parseInt(args[2]);
            int nbHam = Integer.parseInt(args[3]);
            Bayes classifieur = new Bayes(d, args[1], nbSpam, nbHam);
            sauvegarder_classifieur(args[0], classifieur);
        }

    }

    static void sauvegarder_classifieur(String fileName, Bayes bayes) {
        String filename = "classifieurs/" + fileName + ".objet";

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
}

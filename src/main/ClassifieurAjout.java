package main;

import classifier.Bayes;
import classifier.Dictionary;
import classifier.Message;

import java.io.*;
import java.util.Map;

/**
 * Created by mulhauser on 08/05/2017.
 */
public class ClassifieurAjout {

    public static void main(String args[]){
        if(args.length != 3){
            throw new Error("java ClassifieurAjout <@classifieur> <msg.txt> <HAM | SPAM>");
        }else{
            Bayes classifieur = charger_classifieur(args[0]);

            Dictionary d = new Dictionary();

            Message m = new Message(args[1], d);
            switch (args[2]){
                case "HAM":
                    for (Map.Entry<String, Integer> entry : m.getVector().entrySet())
                        if (entry.getValue() != 0)
                            classifieur.getVectorHam().put(entry.getKey(), classifieur.getVectorHam().get(entry.getKey()) + 1);
                    System.out.println("Modification du filtre \'"+args[0]+"\' par apprentissage sur le HAM \'"+args[1]+"\'.");
                    break;
                case "SPAM":
                    for (Map.Entry<String, Integer> entry : m.getVector().entrySet())
                        if (entry.getValue() != 0)
                            classifieur.getVectorSpam().put(entry.getKey(), classifieur.getVectorSpam().get(entry.getKey()) + 1);
                    System.out.println("Modification du filtre \'"+args[0]+"\' par apprentissage sur le SPAM \'"+args[1]+"\'.");
                    break;
                default:
                        throw new Error("java ClassifieurAjout <@classifieur> <msg.txt> <HAM | SPAM>");
            }
        }

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

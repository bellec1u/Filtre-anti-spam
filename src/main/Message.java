package main;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mulhauser on 07/04/2017.
 */
public class Message {

    private Dictionary dictionary;
    private HashMap<String, Integer> vecteurBinaire;
    private static String[] separator =
            {".", ";", ",", " ", "<", ">", "!", "?", "#", "@", "(", ")", "\'", "\t", "-", "+", "="};

    public Message(String pathFile){
        dictionary = new Dictionary();
        vecteurBinaire = new HashMap<String, Integer>(dictionary.getBase().size());

        try {
            BufferedReader br = new BufferedReader(new FileReader(pathFile));
            String line;
            String[] words;
            while ((line = br.readLine()) != null) {
                words = line.split(" ");
                for(int i = 0; i < words.length; i++){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> getVecteurBinaire() {
        return vecteurBinaire;
    }

}

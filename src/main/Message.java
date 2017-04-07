package main;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mulhauser on 07/04/2017.
 */
public class Message {

    private HashMap<String, Integer> vector;
    String regex = "[.|;|,| |<|>|!|?|#|@|(|)|'|-|+|=|/|:]";


    public Message(String pathFile, Dictionary dictionary){
        this.vector = new HashMap<String, Integer>(dictionary.getBase().size());

        for(String word : dictionary.getBase()){
            vector.put(word, 0);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(pathFile));
            String line;
            String[] words;
            while ((line = br.readLine()) != null) {
                words = line.split(regex);

                for(String word : words){
                    word = word.toUpperCase();
                    if(vector.containsKey(word)) {

                        vector.put(word,vector.get(word)+1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> getVector() {
        return vector;
    }

}

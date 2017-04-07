package main;

import classifier.Bayes;
import classifier.Dictionary;

public class Main {

	public static void main(String[] args) {

		Dictionary d = new Dictionary();
		Bayes b = new Bayes(d);
		b.analysisBaseApp();
		
//		for(Map.Entry entry : m.getVector().entrySet()){
//		    System.out.println(entry.getKey()+ "\t"+entry.getValue());
//        }
	}

}

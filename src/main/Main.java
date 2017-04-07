package main;

import classifier.Bayes;
import classifier.Dictionary;

public class Main {

	public static void main(String[] args) {

		Dictionary d = new Dictionary();
		Bayes b = new Bayes(d);
		b.analysisBaseApp(200, 200);
		
	}

}

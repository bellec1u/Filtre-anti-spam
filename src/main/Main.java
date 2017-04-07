package main;

import java.util.Map;

public class Main {

	public static void main(String[] args) {

		Dictionary d = new Dictionary();
		String file = "./base/baseapp/ham/0.txt";
		Message m = new Message(file,d);

		for(Map.Entry entry : m.getVector().entrySet()){
		    System.out.println(entry.getKey()+ "\t"+entry.getValue());
        }
	}

}

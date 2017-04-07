package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dictionary {

	private ArrayList<String> base;

	public Dictionary(String path) {
		this.base = new ArrayList<String>();

		// Lecture de la base
		try {
			BufferedReader br = new BufferedReader( new FileReader(path) );
			String line;
			while ((line = br.readLine()) != null) {
				this.base.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

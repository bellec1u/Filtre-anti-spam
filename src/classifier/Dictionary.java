package classifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dictionary {

	private ArrayList<String> base;
	
	private final static String PATH = "./base/dictionnaire1000en.txt";

	public Dictionary() {
		this.base = new ArrayList<String>();

		// Lecture de la base
		try {
			BufferedReader br = new BufferedReader( new FileReader(PATH) );
			String line;
			while ((line = br.readLine()) != null) {
				// exclusion des mots de 3 lettres ou moins
				if (line.length() > 3)
					this.base.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public ArrayList<String> getBase() {
        return this.base;
    }
}

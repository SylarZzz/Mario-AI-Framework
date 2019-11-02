package levelGenerators.jppetitti_gferguson_generator;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;


public class MarkovChainGenerator implements MarioLevelGenerator {
    List<Slice> slices;
    int totalSlices;

    public MarkovChainGenerator() {
    	// Get all filenames in the levels directory
    	for (final File fileEntry : new File("./levels").listFiles()) {
    		if (!fileEntry.isDirectory()) {
    			parseIn(fileEntry.getName());
    		}
    	}
    }

    public void parseIn(String filename) {
    	FileReader fr;
    	try {
    		fr = new FileReader(filename);
			int i; // the character read by fr
			int column = 0; // the column number being read
			int row = 0; // the row number being read
			boolean firstLine = true;
			while ((i = fr.read()) != -1) {
				if ((char) i == '\n') {
					firstLine = false;
					row++;
					column = 0;
				} else {
	    			if (firstLine) {
	    				slices.add(new Slice());
	    			}
					if ((char) i == 'F') {
						slices.get(column).setFlag(true);
					}
					slices.get(column).addInChar((char) i, row);
				}
			}
			fr.close();
			
    	} catch (Exception e) {
    		System.err.println("File " + filename + " not found");
    		return;
    	}
    }
    
    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
    	// start with a clear model
    	model.clearMap();
    	
    	return ""; // TODO implement
    }
    
    @Override
    public String getGeneratorName() {
    	return "MarkovChainGenerator";
    }
}

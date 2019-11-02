package levelGenerators.jppetitti_gferguson_generator;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;


public class MarkovChainGenerator implements MarioLevelGenerator {
    private List<Slice> slices;
    private int sliceIndex;
    private List<Integer> starts; // indexes of all slices with mario starts
    private List<Integer> ends; // list of slices with a flag

    public MarkovChainGenerator() {
    	sliceIndex = 0;
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
			int columns = 0; // total number of columns
			int column = 0; // the column number being read
			int row = 0; // the row number being read
			boolean firstLine = true;
			while ((i = fr.read()) != -1) {
				if ((char) i == '\n') {
					if (firstLine) {
						firstLine = false;
						columns = column; // remember number of columns
					}
					row++;
					column = 0;
				} else {
	    			if (firstLine) {
	    				slices.add(new Slice());
	    			}
					if ((char) i == 'F') {
						slices.get(sliceIndex + column).setFlag(true);
						ends.add(sliceIndex + column); // remember that this is an ending slice
					} else if ((char) i == 'M') {
						slices.get(sliceIndex + column).setMario(true);
						starts.add(sliceIndex + column); // remember that this is a starting slice
					}
					slices.get(sliceIndex + column).addInChar((char) i, row);
				}
			}
			fr.close();
			// add to the sliceIndex for each slice we just added
			sliceIndex += columns;
			
    	} catch (Exception e) {
    		System.err.println("File " + filename + " not found");
    		return;
    	}
    }
    
    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
    	// intialize new random number generator
    	Random rng = new Random();
    	
    	// start with a clear model
    	model.clearMap();
    	
    	// pick a starting slice that has a Mario start block
    	Slice curSlice = slices.get(starts.get(rng.nextInt(starts.size())));
    	for (int i = 0; i < 16; ++i) {
    		model.setBlock(0, i, curSlice.getPiece(i));
    	}
    	
    	int x = 1;
    	boolean alreadyFlag = false;
    	while (x < model.getWidth() - 1) {
    		curSlice = curSlice.getNext(rng);
    		for (int i = 0; i < 16; ++i) {
    			model.setBlock(0,  i, curSlice.getPiece(i));
    		}
    		if (curSlice.getFlag()) {
    			alreadyFlag = true;
    			break;
    		}
    	}
    	
    	// if we haven't already added a flag, put a flag slice at the end
    	if (!alreadyFlag) {
    		// pick a random flag slice and add it to the end
    		curSlice = slices.get(ends.get(rng.nextInt(ends.size())));
        	for (int i = 0; i < 16; ++i) {
        		model.setBlock(model.getWidth() - 1, i, curSlice.getPiece(i));
        	}
    	}
    	
    	return model.getMap();
    }
    
    @Override
    public String getGeneratorName() {
    	return "MarkovChainGenerator";
    }
}

package levelGenerators.DaiCaiZhangGenerator;

import java.io.File;
import java.io.FileReader;
import java.util.*;

import engine.core.MarioLevelGenerator;
import engine.core.MarioTimer;
import engine.core.MarioLevelModel;

public class LevelGenerator implements MarioLevelGenerator{
    private final String LEVEL_FOLDER = "levels/ge/";
    private List<Chunk> chunks;
    private List<Integer> startChunks;
    private List<Integer> endChunks;

    // read files from levels folder
    public LevelGenerator(){
        chunks = new ArrayList<Chunk>();
        startChunks = new ArrayList<Integer>();
        endChunks = new ArrayList<Integer>();
        File[] f =  new File(LEVEL_FOLDER).listFiles();
        for(int i = 0; i < f.length; i++) {
            if(!f[i].isDirectory()) {
                readLevel(f[i].getAbsolutePath());
            }
        }
    }

    private void readLevel(String filename){
        FileReader filereader;
        List<char[]> columns = new ArrayList<char[]>();
        try {
            filereader = new FileReader(filename);

            int i;                        // the character read by filereader
            int colnum = 0;         // the number of column being read
            int rownum = 0;            // the number of row being read
            boolean isFirstLine = true;

            while ((i = filereader.read()) != -1){
                if ((char) i == '\n'){ 
                    if (isFirstLine){
                        isFirstLine = false;
                    }
                    rownum++;
                    colnum = 0;
                }
                else{
                    if (isFirstLine){
                        columns.add(new char[16]);
                    }
                    columns.get(colnum)[rownum] = (char) i;
                    colnum++;
                }
            }
            filereader.close();

        } catch (Exception e){
            System.err.println(e.toString());
            System.err.println("Error with file: " + filename);
            return;
        }

        Chunk lastChunk = null;
        for (char[] c : columns){
            Chunk tempChunk = null;
            for (Chunk chunk : chunks){
                if (chunk.toString().equals(String.valueOf(c))){
                    tempChunk = chunk;
                    break;
                }
            }
            if (tempChunk == null){
                //new chunk
                tempChunk = new Chunk();
                for (int i = 0; i <= 15; ++i){
                    if ((char) c[i] == 'M'){
                        tempChunk.putMario(true);
                        startChunks.add(chunks.size());
                    }
                    if ((char) c[i] == 'F'){
                        tempChunk.putFlag(true);
                        endChunks.add(chunks.size());
                    }
                    tempChunk.replaceChar(c[i], i);
                }
                chunks.add(tempChunk);
            }
            // add as next chunk
            if (lastChunk != null){
                lastChunk.addNextChunk(tempChunk);
            }
            lastChunk = tempChunk;
        }
    }


    public String getGeneratedLevel(MarioLevelModel mmodel, MarioTimer mtimer){
        // new random number generator
        Random randomNumberGenerator = new Random();
        mmodel.clearMap();

        Chunk currentChunk;
        if (startChunks.size() > 0){
            currentChunk = chunks.get(startChunks.get(randomNumberGenerator.nextInt(startChunks.size())));
        }
        else{
            currentChunk = chunks.get(randomNumberGenerator.nextInt(chunks.size()));
        }
        for (int i = 0; i <= 15; ++i){
            mmodel.setBlock(0, i, currentChunk.getPixel(i));
        }

        int xCoordinate = 1;
        boolean isFlagExist = false;
        int preHeight = currentChunk.getHeight();
        while (xCoordinate < mmodel.getWidth() - 1 ){
            do {
                currentChunk = currentChunk.getRandNext(randomNumberGenerator);
            } while (currentChunk.getSumNexts() < 1 && currentChunk.getHeight() < preHeight + 4);
            for (int i = 0; i <= 15; ++i){
                mmodel.setBlock(xCoordinate, i, currentChunk.getPixel(i));
            }
            if (currentChunk.getFlag()){
                isFlagExist = true;
                break;
            }
            xCoordinate++;
        }

        if (!isFlagExist){
            currentChunk = chunks.get(endChunks.get(randomNumberGenerator.nextInt(endChunks.size())));
            for (int i = 0; i <= 15; ++i){
                mmodel.setBlock(mmodel.getWidth() - 1, i, currentChunk.getPixel(i));
            }
        }

        return mmodel.getMap();
    }


    public String getGeneratorName() {
        return "DaiCaiZhangMarkovChainGenerator";
    }
}

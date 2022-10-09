package levelGenerators.DaiCaiZhangGenerator;

import java.io.*;
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

            int c;                        // the character read by filereader
            int colnum = 0;         // the number of column read
            int rownum = 0;            // the number of row read
            boolean isFirstLine = true;

            while ((c = filereader.read()) != -1){
                if ((char) c == '\n'){
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
                    columns.get(colnum)[rownum] = (char) c;
                    colnum++;
                }
            }
            filereader.close();

        } catch (Exception e){
            System.err.println(e.toString());
            return;
        }

        Chunk lastChunk = null;
        for (int i = 0; i < columns.size(); i++){
            Chunk tempChunk = null;
            for (int k = 0; k < chunks.size(); k++){
                if (chunks.get(k).toString().equals(String.valueOf(columns.get(i)))){
                    tempChunk = chunks.get(k);
                    break;
                }
            }
            if (tempChunk == null){
                //new chunk
                tempChunk = new Chunk();
                for (int j = 0; j <= 15; ++j){
                    if ( columns.get(i)[j] == 'M'){
                        tempChunk.putMario(true);
                        startChunks.add(chunks.size());
                    }
                    if ( columns.get(i)[j] == 'F'){
                        tempChunk.putFlag(true);
                        endChunks.add(chunks.size());
                    }
                    tempChunk.replaceChar(columns.get(i)[j], j);
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
        mmodel.clearMap();
        Random rand = new Random();

        Chunk currentChunk;
        if (startChunks.size() > 0){
            currentChunk = chunks.get(startChunks.get(rand.nextInt(startChunks.size())));
        }
        else {
            currentChunk = chunks.get(rand.nextInt(chunks.size()));
        }
        for (int i = 0; i <= 15; ++i){
            mmodel.setBlock(0, i, currentChunk.getPixel(i));
        }

        int xCoord = 1;
        boolean isFlagExist = false;
        int lastHeight = currentChunk.getHeight();
        while (xCoord < mmodel.getWidth() - 1 ){
            do {
                currentChunk = currentChunk.getRandNext(rand);
            }
            while (currentChunk.getSumNexts() < 1 && currentChunk.getHeight() < lastHeight + 4);
            for (int i = 0; i <= 15; ++i){
                mmodel.setBlock(xCoord, i, currentChunk.getPixel(i));
            }
            if (currentChunk.getFlag()){
                isFlagExist = true;
                break;
            }
            xCoord++;
        }

        if (!isFlagExist){
            currentChunk = chunks.get(endChunks.get(rand.nextInt(endChunks.size())));
            for (int i = 0; i <= 15; ++i){
                mmodel.setBlock(mmodel.getWidth() - 1, i, currentChunk.getPixel(i));
            }
        }

        return mmodel.getMap();
    }


    public String getGeneratorName() {
        return "DaiCaiZhangGenerator";
    }
}


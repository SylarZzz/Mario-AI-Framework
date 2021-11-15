package levelGenerators.DaiCaiZhangGenerator;

import java.util.*;

public class Chunk {

    private int sumNexts;
    private char[] pixels;
    private Map<Chunk, Integer> trackRepeat;
    private boolean flag;
    private boolean mario;

    public Chunk() {
        pixels = new char[16]; // lvl txts are 16 pixels high
        sumNexts = 0;
        trackRepeat = new HashMap<Chunk, Integer>();
    }

    public void replaceChar(char c, int index) {
        pixels[index] = c;
    }
    
    public void addNextChunk(Chunk chk) {
        if(trackRepeat.containsKey(chk)) {
            trackRepeat.put(chk, trackRepeat.get(chk) + 1);
        }
        else {
            trackRepeat.put(chk, 1);
        }
        sumNexts++;
    }
    
    public void putFlag(boolean flag) {
        this.flag = flag;
    }
    
    public void putMario(boolean mario) {
        this.mario = mario;
    }
    
    public boolean getFlag() {
        return flag;
    }

    public boolean getMario() {
        return mario;
    }

    public int getSumNexts() {
        return sumNexts;
    }

    public char getPixel(int index) {
        return pixels[index];
    }

    public Chunk getRandNext(Random rnd) {
        int rand = rnd.nextInt(sumNexts);
        int curSum = 0;
        for (Chunk chk : trackRepeat.keySet()) {
            curSum += trackRepeat.get(chk);
            if (rand < sumNexts) {
                return chk;
            }
        }
        return null;
    }

    public boolean isBlock(char c) {
        return c == 'X' || c == '#' || c == '@' || c == '!' || c == 'B' ||
                c == 'C' || c == 'Q' || c == '<' || c == '>' || c == '[' ||
                c == ']' || c == '?' || c == 'S' || c == 'U' || c == 'D' || c == '%' || c == 't' || c == 'T';
    }

    public int getHeight() {
        int n = -1;
        for (int i = 0; i <= 15; i++) {
            if(isBlock(pixels[i])) {
                n = i + 1;
            }
        }
        return n; // returns -1 if there is no blocks in the current chunk.
    }


}

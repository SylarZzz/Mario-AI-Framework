package levelGenerators.jppetitti_gferguson_generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Slice {
    private int totalFollow;
    private char[] pieces;
    private Map<Slice, Integer> followTimes;
    private boolean isFlag;  // whether this slice contains a flag character
    private boolean isMario; // whether this slice contains a Mario start character

    public Slice(){
        pieces = new char[16];
        totalFollow = 0;
        followTimes = new HashMap<Slice, Integer>();
    }

    public void addInChar(char a, int space){
        pieces[space] = a;
    }
    
    public void setFlag(boolean isFlag) {
    	this.isFlag = isFlag;
    }
    
    public boolean getFlag() {
    	return this.isFlag;
    }
    
    public void setMario(boolean isMario) {
    	this.isMario = isMario;
    }
    
    public boolean getMario() {
    	return this.isMario;
    }

    public char getPiece(int num){
        return(pieces[num]);
    }

    public void addFollow(Slice slice){
        if(followTimes.containsKey(slice)){
            followTimes.put(slice, followTimes.get(slice) + 1);

        }
        else{
            followTimes.put(slice, 1);
        }
        totalFollow++;
    }

    public Slice getNext(Random rng){
        int nextRand = rng.nextInt(totalFollow);
        int tempTotal = 0;
        for(Map.Entry<Slice, Integer> b : followTimes.entrySet()){
            tempTotal += b.getValue();
            if(nextRand < tempTotal){
                return(b.getKey());
            }
        }
        return(null);
    }

}

package agents.gferguson_jppetitti;

import java.util.ArrayList;

public class TreeNode {
    public ArrayList<Edge> nodes;

    public TreeNode(){
        nodes = null;
    }

    public void addNode(Edge added){
        nodes.add(added);
    }


}

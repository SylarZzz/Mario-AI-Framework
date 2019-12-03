package agents.gferguson_jppetitti;

import java.util.ArrayList;

public class TreeNode {
    public ArrayList<Edge> nodes;
    public String querey;

    public TreeNode(String q){
        nodes = null;
        querey = q;
    }

    public void addNode(Edge added){
        nodes.add(added);
    }


}

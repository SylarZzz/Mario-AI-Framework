package agents.gferguson_jppetitti;

import sun.reflect.generics.tree.Tree;

public class DecisionTree {
    public TreeNode rootNode;

    public DecisionTree(){
        rootNode = null;
    }

    public void addNode(Edge added){
        rootNode.addNode(added);
    }
}

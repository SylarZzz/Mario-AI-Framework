package agents.gferguson_jppetitti;

import sun.reflect.generics.tree.Tree;

import javax.swing.*;

public class DecisionTree {
    public TreeNode rootNode;

    public DecisionTree(){
        rootNode = null;
        TreeNode root = new TreeNode("base");
        rootNode = root;
        ActionNode back = new ActionNode("back", 1);
        ActionNode move = new ActionNode("move",2);
        ActionNode jump = new ActionNode("jump", 3);
        TreeNode isEnemy = new TreeNode("is there an enemy?");
        TreeNode isWithinX = new TreeNode("is enemy within x?");
        TreeNode isTurnedTowards = new TreeNode("is enemy turned toward you?");
        TreeNode checkTurnBack = new TreeNode("how many times turned back?");
        Edge enemyIs = new Edge(false,"enemyIs around");
        Edge enemyIsWithin = new Edge(false,"enemy is within range");
        Edge enemyIsTurnedTowards = new Edge(false,"enemy is turned towards you");
        Edge backGreater = new Edge(true,"backed greater than 3 times");
        Edge enemyIsnt = new Edge(true,"enemyIsnt around");
        Edge enemyIsntWithin = new Edge(true,"enemy isnt within range");
        Edge enemyIsntTurnedTowards = new Edge(true,"enemy isnt turned towards you");
        Edge backLEss = new Edge(true,"backed Less than 3 times");

        root.addNode(enemyIs);
        root.addNode(enemyIsnt)
    }

    public void addNode(Edge added){
        rootNode.addNode(added);
    }
}

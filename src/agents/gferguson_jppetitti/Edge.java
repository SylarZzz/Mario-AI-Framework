package agents.gferguson_jppetitti;

public class Edge {
    public TreeNode node;
    public ActionNode action;
    public String nameChoice;
    public boolean isAction;

    public Edge(boolean action, String name){
        isAction = action;
        nameChoice = name;
        node = null;
    }

    public void setNodeTree(TreeNode a){
        node = a;
    }

    public void setNodeAction(ActionNode a){
        action = a;
    }
}

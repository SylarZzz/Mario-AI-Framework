package agents.CaiDaiZhang2;


import agents.robinBaumgarten.Helper;

import java.util.ArrayList;

public class DecisionNode implements Node {
    private ArrayList<Node> branches;

    public DecisionNode(ArrayList<Node> branches) {
        this.setBranches(branches);
    }

    public DecisionNode() { }

    public void setBranches(ArrayList<Node> newBranches) {
        this.branches = newBranches;
    }

    public ArrayList<Node> getBranches() {
        return this.branches;
    }

    /**
     * get the branch at the given index in branches
     */
    public Node getBranch(int index) {
        return this.branches.get(index);
    }

    @Override
    public boolean[] execute() {
        // This generic question node should always just return the first branch
        if (this.branches.size() > 0) {
            return branches.get(0).execute();
        } else {
            // if there are no branches return an empty action
            return Helper.createAction(false, false, false, false, false);
        }
    }
}

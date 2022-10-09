package agents.CaiDaiZhang;

import java.util.*;
import agents.robinBaumgarten.Helper;

public class QuestionNode implements Node {
    private ArrayList<Node> branches;

    public QuestionNode(ArrayList<Node> branches) {
        this.setBranches(branches);
    }

    public QuestionNode() { }

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

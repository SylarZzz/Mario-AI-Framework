package agents.CaiDaiZhang2;



import java.util.ArrayList;

public class BinaryDecisionNode extends DecisionNode {
    public BinaryDecisionNode(Node yesBranch, Node noBranch) {
        ArrayList<Node> branches = new ArrayList<Node>();
        branches.add(yesBranch);
        branches.add(noBranch);
        this.setBranches(branches);
    }

    public boolean[] runYesBranch() {
        return this.getBranch(0).execute();
    }

    public boolean[] runNoBranch() {
        return this.getBranch(1).execute();
    }
}

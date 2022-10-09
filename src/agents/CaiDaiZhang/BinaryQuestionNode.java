package agents.CaiDaiZhang;

import java.util.*;

public class BinaryQuestionNode extends QuestionNode {
    public BinaryQuestionNode(Node yesBranch, Node noBranch) {
        ArrayList<Node> branches = new ArrayList<Node>();
        branches.add(yesBranch);
        branches.add(noBranch);
        this.setBranches(branches);
    }

    public boolean[] executeYesBranch() {
        return this.getBranch(0).execute();
    }

    public boolean[] executeNoBranch() {
        return this.getBranch(1).execute();
    }
}

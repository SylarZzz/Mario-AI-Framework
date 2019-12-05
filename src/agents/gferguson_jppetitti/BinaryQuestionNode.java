package agents.gferguson_jppetitti;

import java.util.ArrayList;

/**
 * Represents a question branch with just two nodes
 * @author Joseph Petitti
 */
public class BinaryQuestionNode extends QuestionNode {
	public BinaryQuestionNode(INode yesBranch, INode noBranch) {
		ArrayList<INode> branches = new ArrayList<INode>();
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

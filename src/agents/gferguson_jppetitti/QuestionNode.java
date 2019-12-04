package agents.gferguson_jppetitti;

import java.util.ArrayList;

import agents.robinBaumgarten.Helper;

/*
 * Each individual question should extend this class and modify execute() to it
 * conditionally return a branch's execute() result
 */
public class QuestionNode implements INode {
	private ArrayList<INode> branches;
	
	public QuestionNode(ArrayList<INode> branches) {
		this.setBranches(branches);
	}
	
	public QuestionNode() { }
	
	public void setBranches(ArrayList<INode> newBranches) {
		this.branches = newBranches;
	}
	
	public ArrayList<INode> getBranches() {
		return this.branches;
	}
	
	/*
	 * get the branch at the given index in branches
	 */
	public INode getBranch(int index) {
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

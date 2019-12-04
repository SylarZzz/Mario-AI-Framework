package agents.gferguson_jppetitti;

import java.util.ArrayList;

import agents.robinBaumgarten.AStarTree;
import agents.robinBaumgarten.Helper;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

public class DecisionTree {	
	public DecisionTree() {

	}
	
	public boolean[] decide(AStarTree aStarTree, MarioForwardModel model, MarioTimer timer) {
		INode dnn = new DoNothingNode();
		INode gfn = new GoForwardNode(aStarTree, model, timer);
		INode enn = new EnemyNearbyNode(model, dnn, gfn);
		INode ljsn = new LevelJustStartedNode(model, dnn, enn);
		return ljsn.execute();
	}
}

/*
 * Define each of the custom classes for the various decision and action nodes
 * here.
 * 
 * This is only necessary because Java doesn't have functions as first-class
 * citizens.
 */

/*
 * Executes the yes branch if there is an enemy within sight of Mario, otherwise
 * executes the no branch
 */
class EnemyNearbyNode extends QuestionNode {
	private MarioForwardModel model;

	public EnemyNearbyNode(MarioForwardModel model, INode yesBranch, INode noBranch) {
		this.model = model;
		ArrayList<INode> branches = new ArrayList<INode>();
		branches.add(yesBranch);
		branches.add(noBranch);
		setBranches(branches);
	}
	
	@Override
	public boolean[] execute() {
		int[][] obs = model.getMarioEnemiesObservation();
		for (int x = 0; x < obs.length; ++x) {
		    for (int y = 0; y < obs[x].length; ++y) {
		    	if (obs[x][y] != MarioForwardModel.OBS_NONE) {
					// enemy spotted
					return this.getBranch(0).execute(); // yesBranch
				}
			}
		}
		return this.getBranch(1).execute(); // noBranch
	}
}

class LevelJustStartedNode extends QuestionNode {
	private MarioForwardModel model;

	public LevelJustStartedNode(MarioForwardModel model, INode yesBranch, INode noBranch) {
		this.model = model;
		ArrayList<INode> branches = new ArrayList<INode>();
		branches.add(yesBranch);
		branches.add(noBranch);
		setBranches(branches);
	}

	@Override
	public boolean[] execute() {
		if (this.model.getRemainingTime() >= 19000) {
			return this.getBranch(0).execute(); // yesBranch
		} else {
			return this.getBranch(1).execute(); // noBranch
		}
	}
}

/*
 * Goes forward with the optimal action computed by A-Star
 */
class GoForwardNode extends ActionNode {
	private AStarTree aStarTree;
	private MarioForwardModel model;
	private MarioTimer timer;
	
	public GoForwardNode(AStarTree aStarTree, MarioForwardModel model, MarioTimer timer) {
		this.aStarTree = aStarTree;
		this.model = model;
		this.timer = timer;
	}
	
	@Override
	public boolean[] execute() {
		System.out.println("GoForwardNode: executing");
		return this.aStarTree.optimise(model, timer);
	}
}

/*
 * Returns an all-false action, doing nothing
 */
class DoNothingNode extends ActionNode {
	public DoNothingNode() { }
	
	@Override
	public boolean[] execute() {
		System.out.println("DoNothingNode: executing");
		return Helper.createAction(false, false, false, false, false);
	}
}




















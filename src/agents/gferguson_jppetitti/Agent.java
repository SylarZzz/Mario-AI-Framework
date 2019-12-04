package agents.gferguson_jppetitti;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;
import agents.robinBaumgarten.AStarTree;


/**
 * @author Grant Ferguson and Joseph Petitti
 */
public class Agent implements MarioAgent {
    private boolean action[];
    private AStarTree aStarTree;
    private DecisionTree decisionTree;

	@Override
	public void initialize(MarioForwardModel model, MarioTimer timer) {
		this.action = new boolean[MarioActions.numberOfActions()];
		this.aStarTree = new AStarTree();
		this.decisionTree = new DecisionTree();
	}

	@Override
	public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
		this.action = this.decisionTree.decide(this.aStarTree, model, timer);
		return action;
	}

	@Override
	public String getAgentName() {
		return "gferguson_jppetittiAgent";
	}

}

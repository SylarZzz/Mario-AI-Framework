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
    private AStarTree tree;

	@Override
	public void initialize(MarioForwardModel model, MarioTimer timer) {
		this.action = new boolean[MarioActions.numberOfActions()];
		this.tree = new AStarTree();
	}

	@Override
	public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
		// TODO Auto-generated method stub
		return action;
	}

	@Override
	public String getAgentName() {
		return "gferguson_jppetittiAgent";
	}

}

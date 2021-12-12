package agents.CaiDaiZhang2;

import agents.robinBaumgarten.AStarTree;
import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class Agent implements MarioAgent {
    boolean action[];
    AStarTree aStar;
    DecisionTree dt;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.action = new boolean[MarioActions.numberOfActions()];
        this.aStar = new AStarTree();
        this.dt = new DecisionTree();
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        this.action = this.dt.decide(this.aStar, model, timer);
        return action;
    }

    @Override
    public String getAgentName() {
        return "CaiDaiZhang2";
    }
}

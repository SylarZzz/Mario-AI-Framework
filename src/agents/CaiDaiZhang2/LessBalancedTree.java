package agents.CaiDaiZhang2;


import agents.robinBaumgarten.AStarTree;
import agents.robinBaumgarten.Helper;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class DecisionTree {

    public DecisionTree() {	}

    public boolean[] decide(AStarTree aStarTree, MarioForwardModel model, MarioTimer timer) {
        // Action nodes
        Node jrn = new JumpRight();
        Node gfn = new GoForward(aStarTree, model, timer);
        Node jyn= new JumpyRun(jrn, gfn);
        Node jpl = new JumpLeft();
        Node jrln = new JumpyRunLeft(jpl, gfn);
        // Decision nodes

        /*
        Node oln = new OnEdgeNode(model, jrn, jyn);
        Node evcn = new EnemyVeryCloseNode(model, jrln, gfn);
        Node ian = new InAirNode(model, gfn, evcn);
        Node nfn = new NearFlagNode(model,jyn, ian);
        Node enn = new EnemyNearbyNode(model, nfn, oln);
        */

        Node oln = new OnEdgeNode(model, jrn, jyn);
        Node evcn = new EnemyVeryCloseNode(model, jrln, gfn);
        Node ian = new InAirNode(model, gfn, evcn);
        Node enn = new EnemyNearbyNode(model, ian, oln);
        Node nfn = new NearFlagNode(model,jyn, enn);

        return nfn.execute();
    }
}

class InAirNode extends BinaryDecisionNode {
    private MarioForwardModel model;

    public InAirNode(MarioForwardModel model, Node yesBranch,
                     Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    public boolean[] execute() {
        if (model.isMarioOnGround()) {
            return this.runNoBranch();
        } else {
            return this.runYesBranch();
        }
    }
}


class EnemyNearbyNode extends BinaryDecisionNode {
    private MarioForwardModel model;

    public EnemyNearbyNode(MarioForwardModel model, Node yesBranch,
                           Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    @Override
    public boolean[] execute() {
        int[][] obv = model.getMarioEnemiesObservation();
        int[][] obv1 = model.getMarioSceneObservation();
        for (int x = 0; x < obv.length; ++x) {
            if (obv[x][obv[x].length / 2] < 15 && obv[x][obv[x].length / 2] > 1 && obv1[x][obv1[x].length / 2 + 7] != MarioForwardModel.OBS_SCENE_OBJECT) { // OBS_SCENE_OBJECT
                return this.runYesBranch();
            }
        }
        return this.runNoBranch();
    }
}


class OnEdgeNode extends BinaryDecisionNode {
    private MarioForwardModel model;

    public OnEdgeNode (MarioForwardModel model, Node yesBranch, Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    public boolean[] execute() {
        int[][] obv = model.getMarioSceneObservation();
        if (model.isMarioOnGround() &&
                obv[obv.length / 2][obv.length / 2 + 2] ==
                        MarioForwardModel.OBS_NONE) {
            return this.runYesBranch();
        } else {
            return this.runNoBranch();
        }
    }
}


class NearFlagNode extends BinaryDecisionNode {
    private MarioForwardModel model;

    public NearFlagNode(MarioForwardModel model, Node yesBranch,
                        Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    public boolean[] execute() {
        if (this.model.getCompletionPercentage() >= .80) {
            return this.runYesBranch();
        } else {
            return this.runNoBranch();
        }
    }
}

class EnemyVeryCloseNode extends BinaryDecisionNode {
    private MarioForwardModel model;

    public EnemyVeryCloseNode(MarioForwardModel model, Node yesBranch,
                              Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    public boolean[] execute() {
        int[][] obv = model.getMarioEnemiesObservation();
        //System.out.println(obv.length);
        //System.out.println(obv[0].length);
        for (int x = obv.length / 2; x < obv.length / 2 + 2; ++x) {
            if (obv[x][obv[x].length / 2] < 15 && obv[x][obv[x].length / 2] > 0) {
                //System.out.println(obv[x][obv[x].length / 2] != MarioForwardModel.OBS_NONE);
                return this.runYesBranch();
            }
        }
        return this.runNoBranch();
    }
}


class JumpyRun extends BinaryDecisionNode {
    public JumpyRun (Node yesBranch, Node noBranch) {
        super(yesBranch, noBranch);
    }

    public boolean[] execute() {
        if (Math.random() < .03) {
            return this.runYesBranch();
        } else {
            return this.runNoBranch();
        }
    }
}

class JumpyRunLeft extends BinaryDecisionNode {
    public JumpyRunLeft (Node yesBranch, Node noBranch) {
        super(yesBranch, noBranch);
    }

    public boolean[] execute() {
        if (Math.random() < .1) {
            return this.runYesBranch();
        } else {
            return this.runNoBranch();
        }
    }
}


class GoForward extends ActionNode {
    private AStarTree aStar;
    private MarioForwardModel model;
    private MarioTimer timer;

    public GoForward(AStarTree aStarTree, MarioForwardModel model,
                     MarioTimer timer) {
        this.aStar = aStarTree;
        this.model = model;
        this.timer = timer;
    }

    @Override
    public boolean[] execute() {
        boolean[] aStarAction = this.aStar.optimise(model, timer);
        aStarAction[MarioActions.SPEED.getValue()] = false;
        return aStarAction;
    }
}


class JumpRight extends ActionNode {
    public JumpRight() { }

    @Override
    public boolean[] execute() {
        return Helper.createAction(false, true, false, true, false);
    }
}


class JumpLeft extends ActionNode {
    public JumpLeft() { }

    @Override
    public boolean[] execute() {
        return Helper.createAction(true, false, false, true, false);
    }
}

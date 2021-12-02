package agents.CaiDaiZhang;

import agents.robinBaumgarten.AStarTree;
import agents.robinBaumgarten.Helper;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;
import java.util.*;

public class DecisionTree {

    public DecisionTree() {	}

    public boolean[] decide(AStarTree aStarTree, MarioForwardModel model, MarioTimer timer) {
        Node jrn = new JumpRightNode();
        Node gfn = new GoForwardNode(aStarTree, model, timer);
        Node jyn= new JumpyRun(jrn, gfn);
        Node oln = new OnLedgeNode(model, jrn, jyn);
        Node enn = new EnemyNearbyNode(model, gfn, oln);
        Node jpl = new JumpLeft();
        Node evcn = new EnemyVeryCloseNode(model, jpl, enn);
        Node ian = new InAirNode(model, gfn, evcn);
        Node nfn = new NearFlagNode(model,jrn, ian);

        return nfn.execute();
    }
}


/**
 * Executes the yes branch if Mario is in the air (not touching the ground),
 * otherwise executes the no branch
 * @author Joseph Petitti
 */
class InAirNode extends BinaryQuestionNode {
    private MarioForwardModel model;

    public InAirNode(MarioForwardModel model, Node yesBranch,
                     Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    public boolean[] execute() {
        if (model.isMarioOnGround()) {
            return this.executeNoBranch();
        } else {
            return this.executeYesBranch();
        }
    }
}


/**
 * Executes the yes branch if there is an enemy within 8 blocks of Mario on the
 * same Y level, otherwise executes the no branch
 * @author Joseph Petitti
 */
class EnemyNearbyNode extends BinaryQuestionNode {
    private MarioForwardModel model;

    public EnemyNearbyNode(MarioForwardModel model, Node yesBranch,
                           Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    @Override/*
    public boolean[] execute() {
        int[][] obs = model.getMarioEnemiesObservation();
        for (int x = 0; x < obs.length; ++x) {
            if (obs[x][obs[x].length / 2] != MarioForwardModel.OBS_NONE && obs[x][obs[x].length / 2] != MarioForwardModel.OBS_SOLID) {
                // enemy spotted
                return this.executeYesBranch();
            }
        }
        // no enemy spotted
        return this.executeNoBranch();
    }
    */
    public boolean[] execute() {
        int[][] obs = model.getMarioEnemiesObservation();
        int[][] obs1 = model.getMarioSceneObservation();
        for (int x = 0; x < obs.length; ++x) {
            if (obs[x][obs[x].length / 2] == MarioForwardModel.OBS_GOOMBA && obs1[x][obs1[x].length / 3] == MarioForwardModel.OBS_SCENE_OBJECT) { // OBS_SCENE_OBJECT
                return this.executeYesBranch();
            }
        }
        // no enemy spotted
        return this.executeNoBranch();
    }
}

/**
 * Executes the yes branch if Mario is standing on a ledge, otherwise executes
 * the no branch
 * @author Joseph Petitti
 */
class OnLedgeNode extends BinaryQuestionNode {
    private MarioForwardModel model;

    public OnLedgeNode(MarioForwardModel model, Node yesBranch,
                       Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    public boolean[] execute() {
        int[][] obs = model.getMarioSceneObservation();
        if (model.isMarioOnGround() &&
                obs[obs.length / 2][obs.length / 2 + 3] ==
                        MarioForwardModel.OBS_NONE) {
            return this.executeYesBranch();
        } else {
            return this.executeNoBranch();
        }
    }
}

class NearFlagNode extends BinaryQuestionNode {
    private MarioForwardModel model;

    public NearFlagNode(MarioForwardModel model, Node yesBranch,
                       Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    public boolean[] execute() {
        int[][] obs = model.getMarioCompleteObservation();
        if (model.isMarioOnGround() &&
                obs[obs.length / 2][obs.length / 2 + 3] ==
                        MarioForwardModel.OBS_SCENE_OBJECT) {
            return this.executeYesBranch();
        } else {
            return this.executeNoBranch();
        }
    }
}

class JumpyRun extends BinaryQuestionNode {
    public JumpyRun (Node yesBranch, Node noBranch) {
        super(yesBranch, noBranch);
    }

    public boolean[] execute() {
        if (Math.random() < .01) {
            return this.executeYesBranch();
        } else {
            return this.executeNoBranch();
        }
    }
}


/**
 * Executes the yes branch if there is an enemy within 3 blocks in front of
 * Mario, otherwise executes the no branch
 * @author Joseph Petitti
 */
class EnemyVeryCloseNode extends BinaryQuestionNode {
    private MarioForwardModel model;

    public EnemyVeryCloseNode(MarioForwardModel model, Node yesBranch,
                              Node noBranch) {
        super(yesBranch, noBranch);
        this.model = model;
    }

    public boolean[] execute() {
        int[][] obs = model.getMarioEnemiesObservation();

        for (int x = obs.length / 2; x < obs.length / 2 + 2; ++x) {
            if (obs[x][obs[x].length / 2] != MarioForwardModel.OBS_NONE) {
                // enemy spotted
                return this.executeYesBranch();
            }
        }
        // no enemy spotted
        return this.executeNoBranch();
    }
}


/**
 * Goes forward with the optimal action computed by A-Star
 * @author Joseph Petitti
 */
class GoForwardNode extends ActionNode {
    private AStarTree aStarTree;
    private MarioForwardModel model;
    private MarioTimer timer;

    public GoForwardNode(AStarTree aStarTree, MarioForwardModel model,
                         MarioTimer timer) {
        this.aStarTree = aStarTree;
        this.model = model;
        this.timer = timer;
    }

    @Override
    public boolean[] execute() {
        boolean[] aStarAction = this.aStarTree.optimise(model, timer);
        aStarAction[MarioActions.SPEED.getValue()] = false;
        return aStarAction;
    }
}


/**
 * Returns an action walking slowly to the left
 * @author Joseph Petitti
 */
class GoLeftNode extends ActionNode {
    public GoLeftNode() { }

    @Override
    public boolean[] execute() {
        return Helper.createAction(true, false, false, false, false);
    }
}

/*
 * Returns an action jumping to the right
 */
class JumpRightNode extends ActionNode {
    public JumpRightNode() { }

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

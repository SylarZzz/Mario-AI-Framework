package agents.gferguson_jppetitti;

import agents.robinBaumgarten.AStarTree;
import agents.robinBaumgarten.Helper;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.ArrayList;

/**
 * this class contains an entire decision tree, and has a public interface that
 * recursively walks down it to get an action
 * @author Joseph Petitti
 */
public class DecisionTree {
	public DecisionTree() {	}

	public boolean[] decide(AStarTree aStarTree, MarioForwardModel model,
			MarioTimer timer) {
		INode dnn = new DoNothingNode();
		INode gln = new GoLeftNode();
		INode jrn = new JumpRightNode();
		INode gfn = new GoForwardNode(aStarTree, model, timer);
		INode ffn = new FiftyFiftyNode(gln, jrn);
		INode oln = new OnLedgeNode(model, ffn, gfn);
		INode enn = new EnemyNearbyNode(model, gfn, oln);
		INode evcn = new EnemyVeryCloseNode(model, gln, enn);
		INode ian = new InAirNode(model, gfn, evcn);
		INode ljsn = new LevelJustStartedNode(model, dnn, ian);
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

/**
 * 50% chance of executing yesBranch, 50% chance of executing noBranch
 * @author Joseph Petitti
 */
class FiftyFiftyNode extends BinaryQuestionNode {
	public FiftyFiftyNode(INode yesBranch, INode noBranch) {
		super(yesBranch, noBranch);
	}

	public boolean[] execute() {
		if (Math.random() < .5) {
			return this.executeYesBranch();
		} else {
			return this.executeNoBranch();
		}
	}
}

/**
 * Executes the yes branch if Mario is in the air (not touching the ground),
 * otherwise executes the no branch
 * @author Joseph Petitti
 */
class InAirNode extends BinaryQuestionNode {
	private MarioForwardModel model;

	public InAirNode(MarioForwardModel model, INode yesBranch,
			INode noBranch) {
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

	public EnemyNearbyNode(MarioForwardModel model, INode yesBranch,
			INode noBranch) {
		super(yesBranch, noBranch);
		this.model = model;
	}

	@Override
	public boolean[] execute() {
		int[][] obs = model.getMarioEnemiesObservation();
		for (int x = 0; x < obs.length; ++x) {
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
 * Executes the yes branch if Mario is standing on a ledge, otherwise executes
 * the no branch
 * @author Joseph Petitti
 */
class OnLedgeNode extends BinaryQuestionNode {
	private MarioForwardModel model;

	public OnLedgeNode(MarioForwardModel model, INode yesBranch,
			INode noBranch) {
		super(yesBranch, noBranch);
		this.model = model;
	}

	public boolean[] execute() {
		int[][] obs = model.getMarioSceneObservation();
		if (model.isMarioOnGround() && 
				obs[obs.length / 2 + 3][obs.length / 2 + 1] == 
				MarioForwardModel.OBS_NONE) {
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

	public EnemyVeryCloseNode(MarioForwardModel model, INode yesBranch,
			INode noBranch) {
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
 * Executes the yes branch if less than one second has elapsed in the level,
 * otherwise executes the no branch
 * @author Joseph Petitti
 */
class LevelJustStartedNode extends BinaryQuestionNode {
	private MarioForwardModel model;

	public LevelJustStartedNode(MarioForwardModel model, INode yesBranch,
			INode noBranch) {
		super(yesBranch, noBranch);
		this.model = model;
	}

	@Override
	public boolean[] execute() {
		if (this.model.getRemainingTime() >= 19 * 1000) {
			return this.executeYesBranch();
		} else {
			return this.executeNoBranch();
		}
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
/*
*	EnemyClose and Astar over it
 */
class EnemyCloseNode extends QuestionNode{
	private MarioForwardModel model;

	public EnemyCloseNode(MarioForwardModel model, INode yesBranch, INode noBranch) {
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

/**
 * Returns an all-false action, doing nothing
 * @author Joseph Petitti
 */
class DoNothingNode extends ActionNode {
	public DoNothingNode() { }

	@Override
	public boolean[] execute() {
		return Helper.createAction(false, false, false, false, false);
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


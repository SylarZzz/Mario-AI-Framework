package agents.gferguson_jppetitti;

import agents.robinBaumgarten.Helper;

/*
 * Each individual leaf node should override execute() with something that returns an appropriate action
 */
public class ActionNode implements INode {

	public ActionNode() { }
	
	@Override
	public boolean[] execute() {
		// This generic action just returns an empty action
		return Helper.createAction(false, false, false, false, false);
	}

}

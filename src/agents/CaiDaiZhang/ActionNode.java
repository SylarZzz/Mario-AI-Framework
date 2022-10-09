package agents.CaiDaiZhang;

import agents.robinBaumgarten.Helper;

public class ActionNode implements Node {

    public ActionNode() { }

    @Override
    public boolean[] execute() {
        // This generic action just returns an empty action
        return Helper.createAction(false, false, false, false, false);
    }

}

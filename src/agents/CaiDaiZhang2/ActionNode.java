package agents.CaiDaiZhang2;

import agents.robinBaumgarten.Helper;

public class ActionNode implements Node {

    public ActionNode() { }

    @Override
    public boolean[] execute() {
        return Helper.createAction(false, false, false, false, false);
    }

}

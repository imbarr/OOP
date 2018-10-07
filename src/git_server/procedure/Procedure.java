package git_server.procedure;

import util.procedure.IProcedure;
import util.procedure.Result;

public abstract class Procedure implements IProcedure {
    public abstract Result execute();
}

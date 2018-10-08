package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.IProcedure;

import java.io.IOException;

public abstract class NetCommand implements ICommand {
    public abstract IProcedure toProcedure(ILocalRepository local) throws IOException;
}

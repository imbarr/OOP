package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.GetLog;
import util.procedure.IProcedure;

public class Log implements ICommand {
    @Override
    public IProcedure toProcedure(ILocalRepository local) {
        return new GetLog(local.getRepoName());
    }
}

package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.GetLatest;
import util.procedure.IProcedure;

public class Update implements ICommand {
    @Override
    public IProcedure toProcedure(ILocalRepository local) {
        return new GetLatest(local.getRepoName());
    }
}

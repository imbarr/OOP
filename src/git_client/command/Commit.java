package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.IProcedure;

import java.io.IOException;

public class Commit implements ICommand {
    @Override
    public IProcedure toProcedure(ILocalRepository local) throws IOException {
        return new util.procedure.Commit(local.getRepoName(), local.getChanges());
    }
}

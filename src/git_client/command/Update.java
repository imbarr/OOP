package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.GetLatest;
import util.procedure.IProcedure;

import java.io.IOException;

public class Update extends NetCommand {
    @Override
    public IProcedure toProcedure(ILocalRepository local) throws IOException {
        return new GetLatest(local.getRepoName());
    }
}

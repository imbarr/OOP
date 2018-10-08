package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.GetLog;
import util.procedure.IProcedure;

import java.io.IOException;

public class Log extends NetCommand {
    @Override
    public IProcedure toProcedure(ILocalRepository local) throws IOException {
        return new GetLog(local.getRepoName());
    }
}

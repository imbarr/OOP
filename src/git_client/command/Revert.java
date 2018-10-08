package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.Get;
import util.procedure.IProcedure;

public class Revert implements ICommand {
    public final String version;
    public final boolean hard;

    public Revert(String version, boolean hard) {
        this.version = version;
        this.hard = hard;
    }

    @Override
    public IProcedure toProcedure(ILocalRepository local) {
        return new Get(local.getRepoName(), version);
    }
}

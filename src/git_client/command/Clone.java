package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.GetLatest;
import util.procedure.IProcedure;

import java.nio.file.Path;

public class Clone extends NetCommand {
    public final Path path;
    public final Path repoName;
    public final boolean addDirectory;

    public Clone(Path path, Path name, boolean addDirectory) {
        this.path = path;
        this.repoName = name;
        this.addDirectory = addDirectory;
    }

    @Override
    public IProcedure toProcedure(ILocalRepository local) {
        return new GetLatest(repoName);
    }
}

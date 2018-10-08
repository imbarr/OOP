package git_client.command;

import git_client.local_repository.ILocalRepository;
import util.procedure.Create;
import util.procedure.IProcedure;

import java.nio.file.Path;

public class Add implements ICommand {
    public final Path name;

    public Add(Path name) {
        this.name = name;
    }

    @Override
    public IProcedure toProcedure(ILocalRepository local) {
        return new Create(name);
    }
}

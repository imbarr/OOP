package git_client.command;

import git_client.local_repository.ILocalRepository;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class ChangeDir implements ICommand {
    public final String dir;
    public final ILocalRepository local;

    public ChangeDir(ILocalRepository local, String dir) {
        this.local = local;
        this.dir = dir;
    }

    @Override
    public String execute() {
        try {
            local.changeDirectory(Paths.get(dir));
        } catch (InvalidPathException e) {
            return "Error: No such directory";
        } catch (IOException e) {
            return "IOError: failed to change dir";
        }
        return "OK: Directory changed";
    }
}

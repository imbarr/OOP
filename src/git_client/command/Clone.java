package git_client.command;

import java.nio.file.Path;

public class Clone implements ICommand {
    public final Path path;
    public final Path repoName;
    public final boolean addDirectory;

    public Clone(Path path, Path name, boolean addDirectory) {
        this.path = path;
        this.repoName = name;
        this.addDirectory = addDirectory;
    }
}

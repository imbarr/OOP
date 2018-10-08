package git_client.command;

import java.nio.file.Path;

public class ChangeDir implements ICommand {
    public final Path dir;

    public ChangeDir(Path dir) {
        this.dir = dir;
    }
}

package util.procedure;

import java.nio.file.Path;

public class Get implements IProcedure {
    public final Path repo;
    public final String version;

    public Get(Path repo, String version) {
        this.version = version;
        this.repo = repo;
    }
}

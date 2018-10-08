package util.procedure;

import java.nio.file.Path;
import java.util.Map;

public class Commit implements IProcedure {
    public final Path repo;
    public final Map<Path, String> changes;

    public Commit(Path repo, Map<Path, String> changes) {
        this.changes = changes;
        this.repo = repo;
    }
}

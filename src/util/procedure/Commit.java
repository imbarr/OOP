package util.procedure;

import java.nio.file.Path;
import java.util.Map;

public class Commit implements IProcedure {
    public final Map<Path, String> changes;

    public Commit(Map<Path, String> changes) {
        this.changes = changes;
    }
}

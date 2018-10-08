package util.procedure;

import java.nio.file.Path;

public class GetLog implements IProcedure {
    public final Path repo;

    public GetLog(Path repo) {
        this.repo = repo;
    }
}

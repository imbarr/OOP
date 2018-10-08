package util.procedure;

import java.nio.file.Path;

public class GetLatest implements IProcedure {
    public final Path repo;

    public GetLatest(Path repo) {
        this.repo = repo;
    }
}

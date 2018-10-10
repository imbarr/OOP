package util.procedure;

import java.nio.file.Path;

public class GetLatest implements IProcedure {
    public final String repo;

    public GetLatest(String repo) {
        this.repo = repo;
    }
}

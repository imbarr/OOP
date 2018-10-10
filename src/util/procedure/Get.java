package util.procedure;

import java.nio.file.Path;

public class Get implements IProcedure {
    public final String repo;
    public final String version;

    public Get(String repo, String version) {
        this.version = version;
        this.repo = repo;
    }
}

package util.procedure;

import java.nio.file.Path;

public class GetLog implements IProcedure {
    public final String repo;

    public GetLog(String repo) {
        this.repo = repo;
    }
}

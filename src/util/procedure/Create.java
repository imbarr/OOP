package util.procedure;

import java.nio.file.Path;

public class Create implements IProcedure {
    public final Path name;

    public Create(Path name) {
        this.name = name;
    }
}

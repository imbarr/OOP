package util.procedure;

import java.nio.file.Path;

public class Create implements IProcedure {
    public final String name;

    public Create(String name) {
        this.name = name;
    }
}

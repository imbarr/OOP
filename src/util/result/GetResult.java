package util.result;

import java.nio.file.Path;
import java.util.Map;

public class GetResult extends Result {
    public final Map<Path, String> files;

    public GetResult(Map<Path, String> files) {
        super();
        this.files =files;
    }
}

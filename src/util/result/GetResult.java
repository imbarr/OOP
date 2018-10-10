package util.result;

import util.procedure.FileContent;

public class GetResult extends Result {
    public final FileContent[] files;

    public GetResult(FileContent[] files) {
        super();
        this.files =files;
    }
}

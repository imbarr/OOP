package util.serializable;

public class GetResult extends Result {
    public FileContent[] files;

    public GetResult() {}

    public GetResult(FileContent[] files) {
        super();
        this.files =files;
    }
}

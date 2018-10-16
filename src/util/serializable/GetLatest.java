package util.serializable;

public class GetLatest implements IProcedure {
    public String repo;

    public GetLatest() {}

    public GetLatest(String repo) {
        this.repo = repo;
    }
}

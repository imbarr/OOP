package util.serializable;

public class GetLog implements IProcedure {
    public String repo;

    public GetLog() {}

    public GetLog(String repo) {
        this.repo = repo;
    }
}

package util.serializable;

public class Get implements IProcedure {
    public String repo;
    public String version;

    public Get() {}

    public Get(String repo, String version) {
        this.version = version;
        this.repo = repo;
    }
}

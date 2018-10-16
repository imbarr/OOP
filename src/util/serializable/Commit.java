package util.serializable;

public class Commit implements IProcedure {
    public String repo;
    public FileContent[] changes;

    public Commit() {}

    public Commit(String repo, FileContent[] changes) {
        this.changes = changes;
        this.repo = repo;
    }
}

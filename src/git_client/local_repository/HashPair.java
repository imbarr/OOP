package git_client.local_repository;

import java.nio.file.Path;

public class HashPair {
    public Path path;
    public Byte[] hash;

    public HashPair() {}

    public HashPair(Path path, Byte[] hash) {
        this.path = path;
        this.hash = hash;
    }
}

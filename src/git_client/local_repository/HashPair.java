package git_client.local_repository;

import java.nio.file.Path;

public class HashPair {
    public String path;
    public byte[] hash;

    public HashPair() {}

    public HashPair(String path, byte[] hash) {
        this.path = path;
        this.hash = hash;
    }
}

package git_client.local_repository;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.file.Path;

public class Hashes {
    public HashPair[] hashes;

    public Hashes() {}

    public Hashes(HashPair[] hashes) {
        this.hashes = hashes;
    }

    public Byte[] get(Path path) {
        for(HashPair p : hashes) {
            if(p.path.equals(path.toString()))
                return ArrayUtils.toObject(p.hash);
        }
        return null;
    }
}

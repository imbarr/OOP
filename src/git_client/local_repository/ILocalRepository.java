package git_client.local_repository;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface ILocalRepository {
    void changeDirectory(Path directory);

    Path getRepoName() throws IOError;

    Map<Path, String> getChanges() throws IOException;
}

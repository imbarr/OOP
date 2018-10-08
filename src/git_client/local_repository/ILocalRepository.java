package git_client.local_repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface ILocalRepository {
    void changeDirectory(Path directory);

    void createHere(Path name) throws IOException;;

    void addHere(Map<Path, String> files) throws IOException;

    Path getRepoName() throws IOException;;

    Map<Path, String> getChanges() throws IOException;
}

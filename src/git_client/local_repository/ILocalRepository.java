package git_client.local_repository;

import util.procedure.FileContent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface ILocalRepository {
    void changeDirectory(Path directory);

    void createHere(String name) throws IOException;

    void addHere(FileContent[] files, boolean hard) throws IOException;

    String getRepoName() throws IOException;

    FileContent[] getChanges() throws IOException;
}

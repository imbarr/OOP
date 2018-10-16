package git_client.local_repository;

import util.serializable.FileContent;

import java.io.IOException;
import java.nio.file.Path;

public interface ILocalRepository {
    void changeDirectory(Path directory) throws IOException;

    void createHere(String name) throws IOException;

    void addHere(FileContent[] files, boolean hard) throws IOException;

    String getRepoName() throws IOException;

    FileContent[] getChanges() throws IOException;

    void refreshHashes() throws IOException;
}

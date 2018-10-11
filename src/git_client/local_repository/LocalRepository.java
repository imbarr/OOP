package git_client.local_repository;

import file_worker.FileWorker;
import file_worker.executable.MD5Execution;
import org.apache.commons.io.FileUtils;
import serializator.Serializator;
import util.procedure.FileContent;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LocalRepository implements ILocalRepository {
    public final Serializator serializator;
    private Path dir;
    public final FileWorker worker;
    public final MD5Execution md5;

    public LocalRepository(Serializator serializator, Path dir) throws IOException {
        this.serializator = serializator;
        md5 = new MD5Execution();
        changeDirectory(dir);
        worker = new FileWorker(md5, dir.toFile(), true);
    }

    @Override
    public void changeDirectory(Path dir) throws IOException {
        if(!dir.toFile().isDirectory())
            throw new IOException("Not a dir");
        this.dir = dir;
    }

    @Override
    public void createHere(String name) throws IOException {

    }



    @Override
    public void addHere(FileContent[] files, boolean hard) throws IOException {

    }

    @Override
    public String getRepoName() throws IOException {
        return null;
    }

    @Override
    public FileContent[] getChanges() throws IOException {
        return new FileContent[0];
    }

    @Override
    public void refreshHashes() throws IOException {
        if(!worker.execute())
            throw new IOException("Failed to count hashes");
        HashPair[] hashes = md5.hashes.entrySet().stream()
                .map(e -> new HashPair(e.getKey().toPath(), e.getValue()))
                .toArray(HashPair[]::new);
        File file = dir.resolve("gt").resolve("hashes").toFile();
        FileUtils.writeByteArrayToFile(file, serializator.serialize(hashes));
    }
}

package git_client.local_repository;

import file_worker.FileWorker;
import file_worker.executable.MD5Execution;
import org.apache.commons.io.FileUtils;
import serializator.ParseException;
import serializator.Serializator;
import util.procedure.FileContent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Vector;

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
        File[] files = dir.toFile().listFiles();
        if(files!=null) {
            for (File f : files) {
                if (f.isDirectory())
                    FileUtils.deleteDirectory(f);
                else if (!f.delete())
                    throw new IOException("Failed to clear dir");
            }
        }
        Path gt = dir.resolve("gt");
        File meta = gt.resolve("meta").toFile();
        if(!gt.toFile().mkdir()
                || !gt.resolve("hashes").toFile().createNewFile()
                || !meta.createNewFile())
            throw new IOException("Failed to create files");
        FileUtils.writeByteArrayToFile(meta, serializator.serialize(new Meta(name)));
    }

    @Override
    public void addHere(FileContent[] files, boolean hard) throws IOException {
        if(hard) {
            for(FileContent fc : files)
                replace(fc);
        }
        else {
            FileContent[] changes = getChanges();
            for (FileContent fc: files)
                if(!isChanged(changes, fc))
                    replace(fc);
        }
    }

    private void replace(FileContent fc) throws IOException {
        FileUtils.writeByteArrayToFile(Paths.get(fc.file).toFile(), fc.content);
    }

    private boolean isChanged(FileContent[] changes, FileContent file) {
        for(FileContent c : changes)
            if(c.file.equals(file.file))
                return true;
        return false;
    }

    @Override
    public String getRepoName() throws IOException {
        try {
            File meta = dir.resolve("gt").resolve("meta").toFile();
            Meta m = (Meta) serializator.deserialize(FileUtils.readFileToByteArray(meta));
            return m.name;
        } catch (ParseException e) {
            throw new IOException("Failed to parse meta");
        }
    }

    @Override
    public FileContent[] getChanges() throws IOException {
        try {
            File file = dir.resolve("gt").resolve("hashes").toFile();
            Hashes hashes = (Hashes) serializator.deserialize(FileUtils.readFileToByteArray(file));
            Vector<FileContent> result = new Vector<>();
            Files.walk(dir).filter(Files::isRegularFile).forEach((f) -> {
                try {
                    Byte[] old = hashes.get(f);
                    byte[] content = FileUtils.readFileToByteArray(f.toFile());
                    Byte[] hash = md5.md5.get(content);
                    if (old == null || !Arrays.equals(old, hash))
                        result.add(new FileContent(f.toString(), content));
                } catch (IOException ignored) {}
            });
            return result.toArray(new FileContent[0]);
        } catch (ParseException e) {
            throw new IOException("Failed to parse hashes file");
        }
    }

    @Override
    public void refreshHashes() throws IOException {
        if(!worker.execute())
            throw new IOException("Failed to count hashes");
        HashPair[] hashes = md5.hashes.entrySet().stream()
                .map(e -> new HashPair(e.getKey().toPath(), e.getValue()))
                .toArray(HashPair[]::new);
        File file = dir.resolve("gt").resolve("hashes").toFile();
        FileUtils.writeByteArrayToFile(file, serializator.serialize(new Hashes(hashes)));
    }
}

package git_server.server_task;

import org.apache.commons.io.FileUtils;
import server.server_task.ServerTask;
import util.command_packet.CommandPacketException;
import util.command_packet.DefaultCommandPacket;
import util.serializable.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

public class DefaultServerTask extends ServerTask {
    private ServerCommandPacket packet = new ServerCommandPacket(
            new DefaultCommandPacket("util.serializable"));

    private Path dir = Paths.get("./repos");

    @Override
    public byte[] work(byte[] input) {
        return packet.serialize(toResult(input));
    }

    private Result toResult(byte[] input) {
        try {
            IProcedure p = packet.deserialize(input);
            if(p instanceof Commit)
                return commit((Commit) p);
            if(p instanceof Create)
                return create((Create) p);
            if(p instanceof Get)
                return get(((Get) p).repo, ((Get) p).version);
            if(p instanceof GetLatest)
                return getLatest((GetLatest) p);
            else
                throw new IllegalArgumentException();
        } catch (CommandPacketException e) {
            return new Result(-1, "Invalid serialization");
        } catch (NotACommandException e) {
            return new Result(-2, "No such procedure");
        } catch (IOException e) {
            return new Result(-3, "Server IOException");
        }
    }

    private Result getLatest(GetLatest get) throws IOException {
        Float ver = getLatest(get.repo);
        if(ver == null)
            return new Result(5, "No versions yet");
        return get(get.repo, ver.toString());
    }

    private Result get(String name, String version) throws IOException {
        Path repo = dir.resolve(name);
        if(!repo.toFile().isDirectory())
            return new Result(2, "Repo not found");
        Path ver = repo.resolve(version);
        if(!ver.toFile().isDirectory())
            return new Result(3, "Version not found");
        Vector<FileContent> result = new Vector<>();
        Files.walk(ver)
                .filter(Files::isRegularFile)
                .forEach(f -> {
                    try {
                        result.add(new FileContent(f.toString(), FileUtils.readFileToByteArray(f.toFile())));
                    } catch (IOException ignored) {}
                });
        return new GetResult(result.toArray(new FileContent[0]));
    }

    private Result create(Create create) throws IOException {
        File[] files = dir.toFile().listFiles();
        if(files != null && Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toSet())
                .contains(create.name))
            return new Result(1, "Repo already exists");
        Path repo = dir.resolve(create.name);
        if(!repo.toFile().mkdir())
            throw new IOException("Failed to create dir");
        commit(new Commit(create.name, new FileContent[0]));
        return new Result();
    }

    private Result commit(Commit commit) throws IOException {
        Float latest = getLatest(commit.repo);
        if(latest == null)
            latest = 1.f;
        else
            latest += 1;
        Path current = dir.resolve(commit.repo).resolve(latest.toString());
        if(!current.toFile().mkdir())
            throw new IOException("failed to create folder");
        for(FileContent fc : commit.changes) {
            File f = Paths.get(fc.file).toFile();
            File parent = f.getParentFile();
            if(parent.exists())
                if(!parent.mkdirs())
                    throw new IOException();
            if(!f.createNewFile())
                throw new IOException();
            FileUtils.writeByteArrayToFile(f, fc.content.content);
        }
        return new Result();
    }

    private Float getLatest(String repoName) {
        File[] list = dir.resolve(repoName).toFile().listFiles();
        if(list == null)
            return null;
        Float max = null;
        for(File f: list) {
            float n = Float.parseFloat(f.getName());
            if(max == null || n > max)
                max = n;
        }
        return max;
    }
}

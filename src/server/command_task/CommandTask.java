package server.command_task;

import file_worker.FileWorker;
import file_worker.executable.MD5Execution;
import thread_dispatcher.threaded_task.ThreadedTask;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandTask extends ThreadedTask {
    private Socket socket;
    private FileWorker worker;
    private MD5Execution hashing;

    public CommandTask(Socket socket, FileWorker worker, MD5Execution hashing) {
        this.socket = socket;
        this.worker = worker;
        this.hashing = hashing;
    }

    @Override
    public void start() {
        try {
            byte[] result = readAll(socket.getInputStream());
            if (result == null)
                return;
            OutputStream out = socket.getOutputStream();
            out.write(execute(new String(result, StandardCharsets.UTF_8)));
            out.close();
            socket.close();
        } catch (IOException ignored) {}
    }

    private byte[] readAll(InputStream stream) throws IOException {
        byte[] rawCount = new byte[2];
        if(stream.read(rawCount, 0, 2) < 2)
            return null;
        ByteBuffer buffer = ByteBuffer.wrap(rawCount);
        short length = buffer.getShort();

        byte[] result = new byte[length];
        if(stream.read(result, 0, length) != length)
            return null;
        return result;
    }

    private byte[] execute(String command) {
        String[] args = command.split(" +");
        switch (args[0]) {
            case "list":
                return list();
            case "hash":
                return (args.length == 2) ?
                        hash(args[1]) :
                        encode("Error: Wrong number of arguments.");
        }
        return encode("Error: Command not recognized.");
    }

    private byte[] list() {
        String[] files = worker.getFile().list();
        return (files == null) ?
                encode("Error: " + worker.getFile().toString() + " is not a directory.") :
                encode(Arrays.stream(files).collect(Collectors.joining("\n")));
    }

    private byte[] hash(String filename) {
        try {
            worker.execute();
            return ArrayUtils.toPrimitive(
                    hashing.getHash(
                            Paths.get(worker.getFile().toString(), filename).toFile()));
        } catch (FileNotFoundException e) {
            return encode("Error: File not found.");
        } catch (IOException e) {
            return encode("Error: " + e.getMessage());
        }
    }

    private byte[] encode(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }
}

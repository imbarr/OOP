package server.command_task;

import server.server_task.ServerTask;
import thread_dispatcher.threaded_task.ThreadedTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServerThreadedTask extends ThreadedTask {
    private Socket socket;
    private ServerTask task;

    public ServerThreadedTask(Socket socket, ServerTask task) {
        this.socket = socket;
        this.task = task;
    }

    @Override
    public void start() {
        try(Socket s = socket;
            InputStream is = s.getInputStream();
            OutputStream out = s.getOutputStream()) {
            byte[] result = readAll(is);
            if (result == null)
                return;
            out.write(task.work(result));
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
}

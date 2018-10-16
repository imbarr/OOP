package server.command_task;

import server.server_task.ServerTask;
import thread_dispatcher.threaded_task.ThreadedTask;
import util.application_protocol.ApplicationProtocolException;
import util.application_protocol.ProtocolMethods;

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
            byte[] result = ProtocolMethods.readAll(is);
            if (result == null)
                return;
            ProtocolMethods.writeAll(task.work(result), out);
        } catch (IOException | ApplicationProtocolException ignored) {}
    }
}

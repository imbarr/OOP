package server;

import file_worker.executable.MD5Execution;
import file_worker.FileWorker;
import server.command_task.ServerThreadedTask;
import thread_dispatcher.ThreadDispatcher;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ThreadDispatcher dispatcher;
    private FileWorker worker;
    private MD5Execution hashing;

    private volatile boolean stopped;

    public void stop() {
        stopped = true;
    }

    public Server(File logging, File directory){
        dispatcher = ThreadDispatcher.getInstance(logging);
        hashing = new MD5Execution();
        worker = new FileWorker(hashing, directory, true);
    }

    public void start() throws IOException {
        try(ServerSocket server = new ServerSocket(40000)) {
            while (!stopped) {
                Socket client = server.accept();
                //TODO: Write ServerTask
                dispatcher.add(new ServerThreadedTask(client, null));
            }
        }
    }
}

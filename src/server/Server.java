package server;

import file_worker.executable.MD5Execution;
import file_worker.FileWorker;
import server.command_task.ServerThreadedTask;
import server.server_task.ServerTask;
import thread_dispatcher.ThreadDispatcher;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ThreadDispatcher dispatcher;
    private ServerTask task;
    private volatile boolean stopped;

    public void stop() {
        stopped = true;
    }

    public Server(File logging, ServerTask task){
        dispatcher = ThreadDispatcher.getInstance(logging);
        this.task = task;
    }

    public void start() throws IOException {
        try(ServerSocket server = new ServerSocket(40000)) {
            while (!stopped) {
                Socket client = server.accept();
                dispatcher.add(new ServerThreadedTask(client, task));
            }
        }
    }
}

package server;

import file_worker.executable.MD5Execution;
import file_worker.FileWorker;
import server.command_task.CommandTask;
import thread_dispatcher.ThreadDispatcher;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class Server {
    private ThreadDispatcher dispatcher;
    private FileWorker worker;
    private MD5Execution hashing;

    public Server(File logging, File directory) throws NoSuchAlgorithmException {
        dispatcher = ThreadDispatcher.getInstance(logging);
        hashing = new MD5Execution();
        worker = new FileWorker(hashing, directory, true);
    }

    public void start() throws IOException {
        ServerSocket server = new ServerSocket(40000);
        while(true) {
            Socket client = server.accept();
            dispatcher.add(new CommandTask(client, worker, hashing));
        }
    }
}

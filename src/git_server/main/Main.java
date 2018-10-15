package git_server.main;

import git_server.server_task.DefaultServerTask;
import server.Server;
import server.server_task.ServerTask;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        ServerTask task = new DefaultServerTask();
        Server server = new Server(Paths.get("./log.txt").toFile(), task);
        try {
            server.start();
        } catch (IOException e) {
            System.out.println("IOException occurred");
        }
    }
}

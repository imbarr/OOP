package server.main;

import server.Server;
import util.IniParser;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Main
{
    public static void main(String[] args) {
        try {
            Map<String, String> ini = IniParser.parse("settings.ini");
            Server server = new Server(new File(ini.get("thread_monitor_file")),
                    new File(ini.get("test_directory")));
            System.out.println("Server started.");
            server.start();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Fatal: MD5 algorithm is not available.");
        } catch (IOException e) {
            System.out.println("Fatal: " + e.getMessage() + ".");
        }
    }
}
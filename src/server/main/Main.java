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
        Map<String, String> ini = IniParser.parse("settings.ini");
        if(ini.containsKey("test_directory") && ini.containsKey("thread_monitor_file")) {
            try {
                Server server = new Server(new File(ini.get("thread_monitor_file")),
                        new File(ini.get("test_directory")));
                System.out.println("Server started.");
                server.start();
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Fatal: MD5 algorithm is not available.");
            } catch (IOException e) {
                System.err.println("Fatal: " + e.getMessage() + ".");
            }
        }
        else {
            System.err.println("Ini file is not present or incomplete.");
        }
    }
}
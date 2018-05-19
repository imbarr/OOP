package thread_dispatcher.main;

import thread_dispatcher.ThreadDispatcher;
import thread_dispatcher.threaded_task.SleepWorker;
import util.IniParser;

import java.io.File;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> ini = IniParser.parse("settings.ini");
        if(ini.containsKey("thread_monitor_file")) {
            ThreadDispatcher td = ThreadDispatcher.getInstance(new File(ini.get("thread_monitor_file")));
            td.add(new SleepWorker(5000, td.monitor));
            td.add(new SleepWorker(5000));
            td.add(new SleepWorker(10000, td.monitor));
        }
        else {
            System.err.println("Ini file is not present or incomplete.");
        }
    }
}

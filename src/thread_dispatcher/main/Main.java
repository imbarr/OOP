package thread_dispatcher.main;

import thread_dispatcher.ThreadDispatcher;
import thread_dispatcher.threaded_task.SleepWorker;
import util.IniParser;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String filename = IniParser.parse("settings.ini").get("thread_monitor_file");
        ThreadDispatcher td = ThreadDispatcher.getInstance(new File(filename));
        td.add(new SleepWorker(5000, td.monitor));
        td.add(new SleepWorker(5000));
        td.add(new SleepWorker(10000, td.monitor));
    }
}

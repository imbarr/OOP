package thread_dispatcher;

import thread_dispatcher.threaded_task.ThreadMonitor;
import thread_dispatcher.threaded_task.ThreadedTask;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ThreadDispatcher {
    public final ThreadMonitor monitor;

    private ThreadDispatcher(File logging) {
        threads = new HashSet<>();
        monitor = new ThreadMonitor(logging, threads);
        add(monitor);
    }

    private static ThreadDispatcher instance;

    private Set<Thread> threads;

    public static ThreadDispatcher getInstance(File logging) {
        if(instance == null)
            synchronized (ThreadDispatcher.class) {
                if(instance == null)
                    instance = new ThreadDispatcher(logging);
            }
        return instance;
    }

    public void add(ThreadedTask task) {
        Thread thread = new Thread(task);
        threads.add(thread);
        thread.start();
        monitor.refresh();
    }
}

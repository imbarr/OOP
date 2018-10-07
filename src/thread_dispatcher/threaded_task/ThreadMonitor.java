package thread_dispatcher.threaded_task;

import thread_dispatcher.callback.Callback;

import java.io.*;
import java.util.Set;
import java.util.stream.Collectors;

public class ThreadMonitor extends ThreadedTask implements Callback {
    private File output;
    private Set<Thread> threads;

    private volatile boolean cancelled;

    public ThreadMonitor(File output, Set<Thread> threads) {
        this.output = output;
        this.threads = threads;
    }

    public void cancel() {
        cancelled = true;
    }

    private Set<Thread> alive() {
        return threads.stream().filter(Thread::isAlive).collect(Collectors.toSet());
    }

    @Override
    public void start() {
        Set<Thread> copy = alive();
        while(!cancelled) {
            Set<Thread> alive = alive();
            if(!alive.equals(copy)) {
                refresh();
                copy = alive;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }
    }

    public void refresh() {
        try(PrintWriter pw = new PrintWriter(output)) {
            for(Thread t: alive())
                pw.println(Long.toString(t.getId()) + " " + t.getName());
        } catch (IOException ignored) {}
    }
}

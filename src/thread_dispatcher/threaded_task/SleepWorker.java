package thread_dispatcher.threaded_task;

import thread_dispatcher.callback.Callback;

public class SleepWorker extends ThreadedTask {
    public final long millis;

    public SleepWorker(long millis) {
        this.millis = millis;
    }

    public SleepWorker(long millis, Callback callback) {
        super(callback);
        this.millis = millis;
    }

    public void start() {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}

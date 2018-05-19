package thread_dispatcher.threaded_task;

import thread_dispatcher.callback.Callback;

public abstract class ThreadedTask implements Runnable {
    public abstract void start();
    private Callback callback;

    public ThreadedTask() {}

    public ThreadedTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        start();
        if (callback != null)
            callback.refresh();
    }
}

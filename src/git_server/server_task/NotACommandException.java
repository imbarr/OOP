package git_server.server_task;

public class NotACommandException extends Exception {
    public NotACommandException() {
        super("Class is not a subclass of IProcedure");
    }
}

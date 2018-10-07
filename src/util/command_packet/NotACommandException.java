package util.command_packet;

public class NotACommandException extends Exception {
    public NotACommandException() {
        super("Class is not a subclass of ICommandSignature");
    }
}

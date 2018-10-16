package util.command_packet;

public class CommandPacketException extends Exception {
    public CommandPacketException(String msg, Exception e) {
        super(msg, e);
    }
}

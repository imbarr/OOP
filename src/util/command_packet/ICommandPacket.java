package util.command_packet;

public interface ICommandPacket {
    byte[] serialize(Object o);
    Object deserialize(byte[] bytes) throws CommandPacketException;
}

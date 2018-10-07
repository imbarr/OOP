package util.command_packet;

import serializator.ParseException;
import util.command.ICommand;

import java.util.zip.DataFormatException;

public interface ICommandPacket {
    byte[] serialize(ICommand command);
    ICommand deserialize(byte[] bytes) throws DataFormatException, ParseException, NotACommandException;
}

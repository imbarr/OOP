package util.command_packet;

import serializator.ParseException;
import util.command.ICommandSignature;

import java.util.zip.DataFormatException;

public interface ICommandPacket {
    byte[] serialize(ICommandSignature command);
    ICommandSignature deserialize(byte[] bytes) throws DataFormatException, ParseException, NotACommandException;
}

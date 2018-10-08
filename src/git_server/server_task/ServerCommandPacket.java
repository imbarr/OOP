package git_server.server_task;

import util.command_packet.CommandPacketException;
import util.command_packet.ICommandPacket;
import util.procedure.IProcedure;
import util.procedure.Result;

public class ServerCommandPacket {
    ICommandPacket packet;

    public ServerCommandPacket(ICommandPacket packet) {
        this.packet = packet;
    }

    public byte[] serialize(Result result) {
        return packet.serialize(result);
    }

    public IProcedure deserialize(byte[] data) throws CommandPacketException, NotACommandException {
        Object obj = packet.deserialize(data);
        if(obj instanceof IProcedure)
            return (IProcedure) obj;
        throw new NotACommandException();
    }
}

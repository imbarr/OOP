package git_client.command_packet;

import util.command_packet.CommandPacketException;
import util.command_packet.ICommandPacket;
import util.procedure.IProcedure;
import util.result.Result;

public class ClientCommandPacket {
    ICommandPacket packet;

    public ClientCommandPacket(ICommandPacket packet) {
        this.packet = packet;
    }

    public byte[] serialize(IProcedure procedure) {
        return packet.serialize(procedure);
    }

    public Result deserialize(byte[] data) throws CommandPacketException, NotAResultException {
        Object obj = packet.deserialize(data);
        if(obj instanceof Result)
            return (Result) obj;
        throw new NotAResultException("Server returned invalid result");
    }
}

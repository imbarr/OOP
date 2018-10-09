package git_server.server_task;

import server.server_task.ServerTask;
import util.command_packet.CommandPacketException;
import util.command_packet.DefaultCommandPacket;
import util.procedure.IProcedure;
import util.result.Result;

public class DefaultServerTask extends ServerTask {
    private ServerCommandPacket packet = new ServerCommandPacket(
            new DefaultCommandPacket("git_server.procedure"));

    @Override
    public byte[] work(byte[] input) {
        return packet.serialize(toResult(input));
    }

    private Result toResult(byte[] input) {
        try {
            IProcedure p = packet.deserialize(input);
            //TODO
            return null;
        } catch (CommandPacketException e) {
            return new Result(-1, "Invalid serialization");
        } catch (NotACommandException e) {
            return new Result(-2, "No such procedure");
        }
    }
}

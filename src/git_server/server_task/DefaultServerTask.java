package git_server.server_task;

import git_server.procedure.Procedure;
import server.server_task.ServerTask;
import util.command_packet.CommandPacketException;
import util.command_packet.DefaultCommandPacket;
import util.procedure.Result;

public class DefaultServerTask extends ServerTask {
    private ServerCommandPacket packet = new ServerCommandPacket(new DefaultCommandPacket());

    @Override
    public byte[] work(byte[] input) {
        return packet.serialize(toResult(input));
    }

    private Result toResult(byte[] input) {
        try {
            Procedure p = packet.deserialize(input);
            return p.execute();
        } catch (CommandPacketException e) {
            return new Result(-1, "Invalid serialization");
        } catch (NotACommandException e) {
            return new Result(-2, "No such procedure");
        }
    }
}

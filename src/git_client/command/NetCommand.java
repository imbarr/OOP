package git_client.command;

import client.Client;
import git_client.command_packet.ClientCommandPacket;
import git_client.command_packet.NotAResultException;
import util.application_protocol.ApplicationProtocolException;
import util.command_packet.CommandPacketException;
import util.serializable.IProcedure;
import util.serializable.Result;

import java.io.IOException;

public abstract class NetCommand implements ICommand {
    private Client client;
    private ClientCommandPacket packet;

    public NetCommand(Client client, ClientCommandPacket packet) {
        this.client = client;
        this.packet = packet;
    }

    protected Result send(IProcedure procedure) throws IOException,
            ApplicationProtocolException,
            CommandPacketException,
            NotAResultException {
        return packet.deserialize(client.send(packet.serialize(procedure)));
    }

    @Override
    public String execute() {
        try {
            return nonWrappedExecute();
        } catch (ApplicationProtocolException | CommandPacketException | NotAResultException e) {
            return "Error: invalid server response.";
        } catch (IOException e) {
            return "Error: IOException";
        }
    }

    protected abstract String nonWrappedExecute() throws IOException,
            ApplicationProtocolException,
            CommandPacketException,
            NotAResultException;
}

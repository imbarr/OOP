package git_client.command;

import client.Client;
import git_client.command_packet.ClientCommandPacket;
import git_client.command_packet.NotAResultException;
import util.application_protocol.ApplicationProtocolException;
import util.command_packet.CommandPacketException;
import util.serializable.Create;

import java.io.IOException;

public class Add extends NetCommand {
    public final String name;

    public Add(Client client, ClientCommandPacket packet, String name) {
        super(client, packet);
        this.name = name;
    }

    @Override
    protected String nonWrappedExecute() throws IOException,
            ApplicationProtocolException,
            CommandPacketException,
            NotAResultException {
        return send(new Create(name)).toString();
    }
}

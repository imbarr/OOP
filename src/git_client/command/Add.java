package git_client.command;

import client.Client;
import git_client.command_packet.ClientCommandPacket;
import git_client.command_packet.NotAResultException;
import git_client.local_repository.ILocalRepository;
import util.application_protocol.ApplicationProtocolException;
import util.command_packet.CommandPacketException;
import util.procedure.Create;
import util.procedure.IProcedure;
import util.result.Result;

import java.io.IOException;
import java.nio.file.Path;

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

package git_client.command;

import client.Client;
import git_client.command_packet.ClientCommandPacket;
import git_client.command_packet.NotAResultException;
import git_client.local_repository.ILocalRepository;
import util.application_protocol.ApplicationProtocolException;
import util.command_packet.CommandPacketException;
import util.serializable.Result;

import java.io.IOException;

public class Commit extends NetCommand {
    public final ILocalRepository local;

    public Commit(Client client, ClientCommandPacket packet, ILocalRepository local) {
        super(client, packet);
        this.local = local;
    }

    @Override
    protected String nonWrappedExecute() throws IOException,
            ApplicationProtocolException,
            CommandPacketException,
            NotAResultException {
        Result r = send(new util.serializable.Commit(local.getRepoName(), local.getChanges()));
        return r.toString();
    }
}

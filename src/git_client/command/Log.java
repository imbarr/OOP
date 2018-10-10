package git_client.command;

import client.Client;
import git_client.command_packet.ClientCommandPacket;
import git_client.command_packet.NotAResultException;
import git_client.local_repository.ILocalRepository;
import util.application_protocol.ApplicationProtocolException;
import util.command_packet.CommandPacketException;
import util.procedure.GetLog;
import util.procedure.IProcedure;
import util.result.LogResult;
import util.result.Result;

import java.io.IOException;

public class Log extends NetCommand {
    public final ILocalRepository local;

    public Log(Client client, ClientCommandPacket packet, ILocalRepository local) {
        super(client, packet);
        this.local = local;
    }

    @Override
    protected String nonWrappedExecute() throws IOException,
            ApplicationProtocolException,
            CommandPacketException,
            NotAResultException {
        Result r = send(new GetLog(local.getRepoName()));
        return r.toString();
    }
}

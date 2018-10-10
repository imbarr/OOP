package git_client.command;

import client.Client;
import git_client.command_packet.ClientCommandPacket;
import git_client.command_packet.NotAResultException;
import git_client.local_repository.ILocalRepository;
import util.application_protocol.ApplicationProtocolException;
import util.command_packet.CommandPacketException;
import util.procedure.GetLatest;
import util.procedure.IProcedure;
import util.result.GetResult;
import util.result.Result;

import java.io.IOException;

public class Update extends NetCommand {
    public final ILocalRepository local;

    public Update(Client client, ClientCommandPacket packet, ILocalRepository local) {
        super(client, packet);
        this.local = local;
    }

    @Override
    protected String nonWrappedExecute() throws IOException,
            ApplicationProtocolException,
            CommandPacketException,
            NotAResultException {
        Result r = send(new GetLatest(local.getRepoName()));
        if(!(r instanceof GetResult))
            return r.toString();
        local.addHere(((GetResult) r).files, false);
        return r.toString();
    }
}

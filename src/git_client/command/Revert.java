package git_client.command;

import client.Client;
import git_client.command_packet.ClientCommandPacket;
import git_client.command_packet.NotAResultException;
import git_client.local_repository.ILocalRepository;
import util.application_protocol.ApplicationProtocolException;
import util.command_packet.CommandPacketException;
import util.procedure.Get;
import util.procedure.IProcedure;
import util.result.GetResult;
import util.result.LogResult;
import util.result.Result;

import java.io.IOException;

public class Revert extends NetCommand {
    public final String version;
    public final boolean hard;
    public final ILocalRepository local;

    public Revert(Client client, ClientCommandPacket packet,
                  ILocalRepository local, String version, boolean hard) {
        super(client, packet);
        this.local = local;
        this.version = version;
        this.hard = hard;
    }

    @Override
    protected String nonWrappedExecute() throws IOException,
            ApplicationProtocolException,
            CommandPacketException,
            NotAResultException{
        Result r = send(new Get(local.getRepoName(), version));
        if(!(r instanceof GetResult))
            return r.toString();
        local.addHere(((GetResult) r).files, hard);
        return r.toString();
    }
}

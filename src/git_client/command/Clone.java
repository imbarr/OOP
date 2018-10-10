package git_client.command;

import client.Client;
import git_client.command_packet.ClientCommandPacket;
import git_client.command_packet.NotAResultException;
import git_client.local_repository.ILocalRepository;
import util.application_protocol.ApplicationProtocolException;
import util.command_packet.CommandPacketException;
import util.procedure.GetLatest;
import util.result.GetResult;
import util.result.Result;

import java.io.IOException;
import java.nio.file.Path;

public class Clone extends NetCommand {
    public final Path path;
    public final String repoName;
    public final boolean addDirectory;
    public final ILocalRepository local;

    public Clone(ILocalRepository local,
                 Client client,
                 ClientCommandPacket packet,
                 Path path,
                 String name,
                 boolean addDirectory) {
        super(client, packet);
        this.local = local;
        this.path = path;
        this.repoName = name;
        this.addDirectory = addDirectory;
    }

    @Override
    protected String nonWrappedExecute() throws IOException,
            ApplicationProtocolException,
            CommandPacketException,
            NotAResultException {
        Result r = send(new GetLatest(repoName));
        if(r.error != 0)
            return r.toString();
        if(!(r instanceof GetResult))
            return r.toString();
        local.createHere(repoName);
        local.addHere(((GetResult) r).files, false);
        return r.toString();
    }
}

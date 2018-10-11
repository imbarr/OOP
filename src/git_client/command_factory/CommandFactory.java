package git_client.command_factory;

import client.Client;
import git_client.command.*;
import git_client.command_packet.ClientCommandPacket;
import git_client.local_repository.ILocalRepository;
import util.command_packet.ICommandPacket;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;

public class CommandFactory implements ICommandFactory {
    public final Client client;
    public final ClientCommandPacket packet;
    public final ILocalRepository local;

    public CommandFactory(Client client, ClientCommandPacket packet, ILocalRepository local) {
        this.local = local;
        this.client = client;
        this.packet = packet;
    }

    @Override
    public ICommand parse(String input) throws SyntaxException {
        String[] list = split(input);
        if(list.length == 0)
            throw new SyntaxException("Empty input");

        switch (list[0].toLowerCase()) {
            case "add":
                checkArgs(list, i -> i == 2);
                return new Add(client, packet, list[2]);
            case "clone":
                checkArgs(list, i -> i >= 3);
                boolean addDir = true;
                if (list.length == 4 && list[4].equals("."))
                    addDir = false;
                else if (list.length != 3)
                    throw new SyntaxException("Wrong flags");
                return new Clone(local, client, packet,
                        toPath(list[1]), list[2], addDir);
            case "update":
                checkArgs(list, i -> i == 1);
                return new Update(client, packet, local);
            case "commit":
                checkArgs(list, i -> i == 1);
                return new Commit(client, packet, local);
            case "revert":
                checkArgs(list, i -> i >= 2);
                boolean hard = false;
                if(list.length == 3 && list[3].equals("--hard"))
                    hard = true;
                else if(list.length != 2)
                    throw new SyntaxException("Wrong flags");
                return new Revert(client, packet, local, list[1], hard);
            case "log":
                checkArgs(list, i -> i == 1);
                return new Log(client, packet, local);
            case "changedir":
                checkArgs(list, i -> i == 2);
                return new ChangeDir(local, list[1]);
        }
        throw new SyntaxException("Command not found");
    }

    private String[] split(String input) {
        return Arrays.stream(input.split("(?!(?!\\\\)\\\\) "))
                .filter(s -> !s.isEmpty())
                .map(s -> s.replace("\\ ", " "))
                .toArray(String[]::new);
    }

    private void checkArgs(String[] list, Function<Integer, Boolean> check) throws SyntaxException {
        if(!check.apply(list.length))
            throw new SyntaxException("Wrong number of arguments");
    }

    private Path toPath(String s) throws SyntaxException {
        try {
            return Paths.get(s);
        } catch (InvalidPathException e) {
            throw new SyntaxException("Invalid path");
        }
    }
}

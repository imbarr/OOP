package git_client.main;

import client.Client;
import git_client.command.ICommand;
import git_client.command_factory.CommandFactory;
import git_client.command_factory.ICommandFactory;
import git_client.command_factory.SyntaxException;
import git_client.command_packet.ClientCommandPacket;
import git_client.local_repository.ILocalRepository;
import git_client.local_repository.LocalRepository;
import serializator.Serializator;
import util.command_packet.DefaultCommandPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Serializator serializator;

        ILocalRepository local;
        Client client;
        ClientCommandPacket packet;
        ICommandFactory factory;
        try {
            serializator = new Serializator("client.local_repository");
            local = new LocalRepository(serializator, Paths.get("./repos"));
            client = new Client(InetAddress.getLocalHost(), 9999);
            packet = new ClientCommandPacket(new DefaultCommandPacket("util"));
            factory = new CommandFactory(client, packet, local);
        } catch (IOException e) {
            System.out.println("IOError while initializing");
            return;
        }
        Scanner scan = new Scanner(System.in);
        while (true) {
            String in = scan.next().toLowerCase();
            if(in.equals("quit") || in.equals("exit") || in.equals("q"))
                break;
            try {
                ICommand command = factory.parse(scan.next());
                System.out.println(command.execute());
            } catch (SyntaxException e) {
                System.out.println("Command not recognized");
            }
        }
    }
}

package client.command_factory;

import client.command.ICommand;

public interface ICommandFactory {
    ICommand parse(String input) throws SyntaxException;
}

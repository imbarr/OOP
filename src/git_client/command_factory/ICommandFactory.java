package git_client.command_factory;

import git_client.command.ICommand;

public interface ICommandFactory {
    ICommand parse(String input) throws SyntaxException;
}

package git_client.command;

public class Add implements ICommand {
    public final String name;

    public Add(String name) {
        this.name = name;
    }
}

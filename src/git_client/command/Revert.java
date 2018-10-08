package git_client.command;

public class Revert implements ICommand {
    public final String version;
    public final boolean hard;

    public Revert(String version, boolean hard) {
        this.version = version;
        this.hard = hard;
    }
}

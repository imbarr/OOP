package util.result;

import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class LogResult extends Result {
    public final LogUnit[] commits;

    public LogResult(LogUnit[] commits) {
        this.commits = commits;
    }

    @Override
    public String toString() {
        return commits.length == 0
                ? "OK: No commits"
                : "OK:\n" + Arrays.stream(commits)
                    .map(u -> u.version + " " + u.date.toString() + " " +
                            u.ip.toString() + "\n" + String.join("\n", u.files))
                    .collect(Collectors.joining("\n"));
    }
}

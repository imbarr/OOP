package util.serializable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LogResult extends Result {
    public LogUnit[] commits;

    public LogResult() {}

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

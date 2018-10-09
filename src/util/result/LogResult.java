package util.result;

import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Date;

public class LogResult extends Result {
    public final Date date;
    public final String version;
    public final InetAddress ip;
    public final Path[] files;

    public LogResult(Date date, String version, InetAddress ip, Path[] files) {
        this.date = date;
        this.version = version;
        this.ip = ip;
        this.files = files;
    }
}

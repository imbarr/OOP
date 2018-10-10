package util.result;

import java.net.InetAddress;
import java.util.Date;

public class LogUnit {
    public final Date date;
    public final String version;
    public final InetAddress ip;
    public final String[] files;

    public LogUnit(Date date, String version, InetAddress ip, String[] files) {
        this.date = date;
        this.version = version;
        this.ip = ip;
        this.files = files;
    }
}

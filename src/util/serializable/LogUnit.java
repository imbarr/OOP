package util.serializable;

import java.net.InetAddress;
import java.util.Date;

public class LogUnit {
    public Date date;
    public String version;
    public InetAddress ip;
    public String[] files;

    public LogUnit() {}

    public LogUnit(Date date, String version, InetAddress ip, String[] files) {
        this.date = date;
        this.version = version;
        this.ip = ip;
        this.files = files;
    }
}

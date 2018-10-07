package util.application_protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ProtocolMethods {
    public static byte[] readAll(InputStream stream) throws IOException, ApplicationProtocolException {
        StringBuilder length = new StringBuilder();
        while(true) {
            int next = stream.read();
            if(next == -1)
                throw new ApplicationProtocolException("No length marker");
            if(next == 0)
                break;
            length.append((char) next);
        }
        try {
            int l = Integer.parseInt(length.toString());
            byte[] result = new byte[l];
            stream.readNBytes(result, 0, l);
            return result;
        } catch (NumberFormatException e) {
            throw new ApplicationProtocolException("Invalid length");
        }
    }

    public static void writeAll(byte[] data, OutputStream stream) throws IOException {
        byte[] length = StandardCharsets.UTF_8.encode(Integer.toString(data.length, 16)).array();
        stream.write(length);
        stream.write(0);
        stream.write(data);
    }
}

package client;

import util.application_protocol.ApplicationProtocolException;
import util.application_protocol.ProtocolMethods;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private Socket socket;
    private InetAddress address;
    private int port;

    public Client(InetAddress address, int port) {
        socket = new Socket();
        this.address = address;
        this.port = port;
    }

    public byte[] send(byte[] data) throws IOException, ApplicationProtocolException {
        try(Socket s = socket) {
            s.connect(new InetSocketAddress(address, port));
            OutputStream os = s.getOutputStream();
            ProtocolMethods.writeAll(data, os);
            InputStream is = s.getInputStream();
            return ProtocolMethods.readAll(is);
        }
    }
}

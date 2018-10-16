package util.command_packet;

import serializator.ParseException;
import serializator.Serializator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DefaultCommandPacket implements ICommandPacket {
    private Serializator serializator;

    public DefaultCommandPacket(String usedPackage) {
        this.serializator = new Serializator();
    }

    @Override
    public byte[] serialize(Object o) {
        byte[] serialized = serializator.serialize(o);

        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);
        compressor.setInput(serialized);
        compressor.finish();

        byte[] buf = new byte[1024];
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(serialized.length)) {
            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                os.write(buf, 0, count);
            }
            return os.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws CommandPacketException {
        try {
            Inflater decompresser = new Inflater();
            decompresser.setInput(bytes);

            byte[] buf = new byte[1024];
            try (ByteArrayOutputStream os = new ByteArrayOutputStream(bytes.length)) {
                while (!decompresser.finished()) {
                    int count = decompresser.inflate(buf);
                    os.write(buf, 0, count);
                }
                return serializator.deserialize(os.toByteArray());
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        } catch (DataFormatException e) {
            throw new CommandPacketException("Invalid compression", e);
        } catch (ParseException e) {
            throw new CommandPacketException("Invalid serialization", e);
        }
    }
}

package util.command_packet;

import serializator.ParseException;
import serializator.Serializator;
import util.command.ICommandSignature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DefaultCommandPacket implements ICommandPacket {
    private Serializator serializator = new Serializator();

    @Override
    public byte[] serialize(ICommandSignature command) {
        byte[] serialized = serializator.serialize(command);

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
    public ICommandSignature deserialize(byte[] bytes) throws DataFormatException, ParseException, NotACommandException {
        Inflater decompresser = new Inflater();
        decompresser.setInput(bytes);

        byte[] buf = new byte[1024];
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(bytes.length)) {
            while (!decompresser.finished()) {
                int count = decompresser.inflate(buf);
                os.write(buf, 0, count);
            }
            Object obj = serializator.deserialize(os.toByteArray());
            if(obj instanceof ICommandSignature)
                return (ICommandSignature) obj;
            throw new NotACommandException();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }
}

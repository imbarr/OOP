package file_worker.executable;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.lang.SecurityException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.stream.Collectors;

public class MD5Execution implements IExecutable {
    private HashMap<File, Byte[]> hashes;
    private MessageDigest md;

    public MD5Execution() throws NoSuchAlgorithmException {
        hashes = new HashMap<>();
        md = MessageDigest.getInstance("MD5");
    }

    public Byte[] getHash(File file) throws IOException {
        if(file.isFile())
            return hashes.get(file);
        else {
            Vector<Byte> bytes = new Vector<>();
            for(File f: Files.list(file.toPath()).map(Path::toFile).collect(Collectors.toList())){
                Byte[] hash = getHash(f);
                if(hash != null)
                    bytes.addAll(Arrays.asList(hash));
            }
            Byte[] result = ArrayUtils.toObject(md.digest(ArrayUtils.toPrimitive(bytes.toArray(new Byte[0]))));
            hashes.put(file, result);
            return result;
        }
    }

    @Override
    public boolean execute(File file) {
        FileInputStream fis = null;
        try {
            byte[] result = new byte[(int) file.length()];
            fis = new FileInputStream(file);
            fis.read(result);
            hashes.put(file, ArrayUtils.toObject(md.digest(result)));
            return true;
        }
        catch(IOException | SecurityException e) {
            return false;
        }
        finally {
            if(fis != null) {
                try {
                    fis.close();
                }
                catch(IOException ignored) {}
            }
        }
    }
}
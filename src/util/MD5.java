package util;

import org.apache.commons.lang3.ArrayUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private final MessageDigest md;

    public MD5() {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }
    }

    public Byte[] get(Byte[] file) {
        return ArrayUtils.toObject(md.digest(ArrayUtils.toPrimitive(file)));
    }

    public Byte[] get(byte[] file) {
        return ArrayUtils.toObject(md.digest(file));
    }
}

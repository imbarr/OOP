package serializator.main;

import serializator.Serializator;
import util.serializable.Commit;

import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        Serializator s = new Serializator();
        byte[] result = s.serialize(new Byte[0]);
        System.out.println(new String(result, StandardCharsets.UTF_8));
        try {
            Commit deserialized = (Commit) s.deserialize(result);
            System.out.println("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

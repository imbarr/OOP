package serializator.main;

import serializator.Serializator;
import serializator.test_classes.Class1;

import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        Class1 c1 = new Class1();
        c1.str = "abc#()gfhfghf123||";
        c1.class1 = new Class1();
        Serializator s = new Serializator("serializator.test_classes");
        byte[] result = s.serialize(c1);
        System.out.println(new String(result, StandardCharsets.UTF_8));
        try {
            Class1 c1_deserialized = (Class1)s.deserialize(result);
            System.out.println("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

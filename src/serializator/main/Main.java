package serializator.main;

import serializator.Serializator;
import serializator.test_classes.Class1;
import serializator.test_classes.Class2;
import serializator.test_classes.ClassArray;
import serializator.test_classes.Pair;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Class1 c1 = new Class1();
        c1.str = "abc#()gfhfghf123||";
        c1.class1 = new Class1();
        Serializator s = new Serializator("serializator.test_classes");
        Class1[] array = {new Class2(), new Class2()};
        ClassArray a = new ClassArray();
        a.field = array;
        HashMap<Path, String> m = new HashMap<>();
        m.put(Paths.get("./first/second"), "AAAAAAAAAAA");
        m.put(Paths.get("./first"), "BBBBBBBBBBB");
        byte[] result = s.serialize(a);
        System.out.println(new String(result, StandardCharsets.UTF_8));
        try {
            ClassArray c1_deserialized = (ClassArray)s.deserialize(result);
            System.out.println("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

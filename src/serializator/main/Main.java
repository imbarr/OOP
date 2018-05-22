package serializator.main;

import serializator.Serializator;
import serializator.test_classes.Class1;

public class Main {
    public static void main(String[] args) {
        Class1 c1 = new Class1();
        c1.str = "abc#()gfhfghf123||";
        c1.class1 = new Class1();
        Serializator s = new Serializator();
        System.out.println(s.serialize(c1));
    }
}

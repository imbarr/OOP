package serializator.main;

import serializator.Serializator;
import util.procedure.FileContent;
import util.procedure.Commit;

import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        Serializator s = new Serializator("util.procedure");
        FileContent[] fc = {new FileContent("A/file1", "111111111424223423"),
                new FileContent("A/file2", "11111111142gfhfh4223423"),
                new FileContent("B/file3", "111111111gh424223423")};
        Commit c = new Commit("repo1", fc);
        byte[] result = s.serialize(c);
        System.out.println(new String(result, StandardCharsets.UTF_8));
        try {
            Commit deserialized = (Commit) s.deserialize(result);
            System.out.println("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

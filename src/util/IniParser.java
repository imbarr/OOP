package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IniParser {
    private IniParser() {}

    public static Map<String, String> parse(String filename) {
        try {
            return Files.lines(Paths.get(filename))
                    .map(s -> s.split("="))
                    .filter(l -> l.length == 2)
                    .collect(Collectors.toMap(p -> p[0], p -> p[1]));
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}

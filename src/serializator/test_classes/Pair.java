package serializator.test_classes;

import javafx.beans.NamedArg;

import java.io.Serializable;

public class Pair implements Serializable {
    private String key;
    private String value;

    public Pair(@NamedArg("key") String key, @NamedArg("value") String value) {
        this.key = key;
        this.value = value;
    }
}
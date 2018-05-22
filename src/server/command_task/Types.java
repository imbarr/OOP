package server.command_task;

public enum Types {
    integer("int"),
    _float("float"),
    _double("double"),
    character("char"),
    _byte("byte"),
    string("string"),
    object("object");


    public final String name;

    Types(String name) {
        this.name = name;
    }
}

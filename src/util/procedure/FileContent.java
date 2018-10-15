package util.procedure;

public class FileContent {
    public String file;
    public byte[] content;

    public FileContent() {}

    public FileContent(String file, byte[] content) {
        this.file = file;
        this.content = content;
    }
}

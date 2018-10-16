package util.serializable;

public class FileContent {
    public String file;
    public Content content;

    public FileContent() {}

    public FileContent(String file, byte[] content) {
        this.file = file;
        this.content = new Content(content);
    }
}

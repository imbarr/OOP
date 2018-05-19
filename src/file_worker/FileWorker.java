package file_worker;
import java.io.File;
import file_worker.executable.IExecutable;
import java.io.FileNotFoundException;

public class FileWorker {
    public static final boolean isRecursiveDefault = false;

    private boolean isRecursive;
    private File file;
    private IExecutable command;

    public FileWorker(IExecutable command) {
        this.command = command;
        file = new File("");
        isRecursive = isRecursiveDefault;
    }

    public FileWorker(IExecutable command, File file, boolean isRecursive) {
        this.command = command;
        this.file = file;
        this.isRecursive = isRecursive;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setRecursive(boolean isRecursive) {
        this.isRecursive = isRecursive;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public boolean execute() throws FileNotFoundException {
        if(file.isFile())
            return execute(file);
        if(file.isDirectory())
            return executeDirectory(file);
        throw new FileNotFoundException("File " + file.getPath() + " not found.");
    }

    private boolean execute(File file) {
        if(file.isFile())
            return command.execute(file);
        return !isRecursive || executeDirectory(file);
    }

    private boolean executeDirectory(File file) {
        boolean succeed = true;
        for(File f : file.listFiles())
            succeed = succeed && execute(f);
        return succeed;
    }
}
package file_worker;
import java.io.File;
import file_worker.executable.IExecutable;
import java.io.FileNotFoundException;

public class FileWorker {
    private static final boolean isRecursiveDefault = false;

    private boolean isRecursive;
    private File file;
    private IExecutable command;

    public FileWorker(IExecutable command, File file) {
        this.command = command;
        this.file = file;
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
            return command.execute(file);
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
        File[] list = file.listFiles();
        if(list == null)
            return false;
        for(File f : list)
            succeed = succeed && execute(f);
        return succeed;
    }
}
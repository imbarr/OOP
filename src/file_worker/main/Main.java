package file_worker.main;
import file_worker.executable.MD5Execution;
import file_worker.FileWorker;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import util.IniParser;

import java.io.File;


public class Main
{
    public static void main(String[] args)
    {
        try {
            String directory = IniParser.parse("settings.ini").get("test_directory");
            MD5Execution md5 = new MD5Execution();
            FileWorker fw = new FileWorker(md5, new File(directory), true);
            boolean res = fw.execute();
            System.out.println(res);
            Byte[] a = md5.getHash(new File(directory + "/folder1"));
            System.out.println(Hex.encodeHexString(ArrayUtils.toPrimitive(a)));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

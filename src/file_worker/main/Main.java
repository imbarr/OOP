package file_worker.main;
import file_worker.executable.MD5Execution;
import file_worker.FileWorker;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import util.IniParser;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


public class Main
{
    public static void main(String[] args)
    {
        Map<String, String> ini = IniParser.parse("settings.ini");
        if(ini.containsKey("test_directory")) {
            try {
                MD5Execution md5 = new MD5Execution();
                FileWorker fw = new FileWorker(md5, new File(ini.get("test_directory")), true);
                boolean res = fw.execute();
                System.out.println(res);
                Byte[] a = md5.getHash(new File(ini.get("test_directory") + "/folder1"));
                System.out.println(Hex.encodeHexString(ArrayUtils.toPrimitive(a)));
            } catch (IOException | NoSuchAlgorithmException e) {
                System.err.println(e.getMessage());
            }
        }
        else {
            System.err.println("Ini file is not present or incomplete.");
        }
    }
}

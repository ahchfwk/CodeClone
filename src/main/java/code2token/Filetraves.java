package code2token;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huage on 2017/3/23.
 */
public class Filetraves {



    public List<File> lists;
    public File dir;

    public Filetraves(String dir) {
        this.lists = new ArrayList<File>();
        this.dir = new File(dir);
    }

    public List<File> directoryAllList(){
        if (!dir.isDirectory()){
            System.out.println(dir+"不是目录或�?�不存在");
            return null;
        }
        else {
            directorylist(dir);
            return lists;
        }
    }
    public  void directorylist(File dir){
        File[] sonFiles = dir.listFiles();
        if(sonFiles!=null&&sonFiles.length>0) {
            for (File sonFile : sonFiles) {
                if (sonFile.isDirectory()) {
                    directorylist(sonFile);
                } else {
                    if (sonFile.getName().endsWith(".java")) {
                        lists.add(sonFile);
                    }
                }
            }
        }
    }


}

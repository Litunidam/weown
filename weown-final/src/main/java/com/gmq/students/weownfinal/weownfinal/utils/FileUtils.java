package com.gmq.students.weownfinal.weownfinal.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void fileupload(byte[] file,String filePath,String fileName) throws IOException {

        File targetfile = new File(filePath+fileName);
        if(targetfile.exists()) {
            targetfile.mkdirs();
        }

        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

}

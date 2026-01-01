package com.fmx.xiaomeng.common.utils.jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {

    public static void saveFile(String filename, byte[] data) {

        if (data != null) {
            String filepath = filename;
            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data, 0, data.length);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

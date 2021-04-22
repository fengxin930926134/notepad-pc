package sample;

import java.io.*;

public class FileUtils {

    /**
     * 是否存在此文件
     * @param path 全路径
     * @return boolean
     */
    public static boolean isExist(String path) {
        return new File(path).exists();
    }
}

package com.bit.utils;

public class FileUtil {


    /**
     * Description: 获取文件后缀名
     *
     * @param fileName
     * @return
     * @see
     */
    public static String getExtensionName(String fileName)
    {
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }

    /**
     * Description: 获取文件后缀名
     *
     * @param fileName
     * @return
     * @see
     */
    public static String getFileName(String fileName)
    {
        String prefix = fileName.substring(0,fileName.lastIndexOf(".") );
        return prefix;
    }

    /**
     * 根据path获取文件名
     *
     * @author kokJuis
     * @version 1.0
     * @date 2016-12-12
     * @param filename
     * @return
     */
    public static String getOriginalFilename(String filename)
    {
        if (filename == null) {
            return "";
         }
        int pos = filename.lastIndexOf("/");
        if (pos == -1){
            pos = filename.lastIndexOf("\\");
        }
        if (pos != -1){
            return filename.substring(pos + 1);
        } else{
            return filename;
        }

    }
}

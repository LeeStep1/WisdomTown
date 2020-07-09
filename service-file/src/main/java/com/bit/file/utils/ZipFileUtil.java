package com.bit.file.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author chenduo
 * @create 2019-01-22 9:12
 */
public class ZipFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);



    private static final int  BUFFER_SIZE = 2 * 1024;



    /**
     * 压缩成ZIP 方法
     * @param srcFiles 需要压缩的文件列表
     * @param zipFilePath :压缩后存放路径
     * @param fileName :压缩后文件的名称
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles , String zipFilePath,String fileName) throws RuntimeException, FileNotFoundException {
        File zipFile = new File(zipFilePath + "/" + fileName +".zip");
        FileOutputStream fos = null;
        fos = new FileOutputStream(zipFile);
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(fos);
            for (File srcFile : srcFiles) {
                FileInputStream in = new FileInputStream(srcFile);
                try {
                    byte[] buf = new byte[BUFFER_SIZE];
                    zos.putNextEntry(new ZipEntry(srcFile.getName()));
                    int len;
                    while ((len = in.read(buf)) != -1){
                        zos.write(buf, 0, len);
                    }
                }catch (Exception e){
                    logger.error("压缩异常{}"+e.getMessage());
                }finally {
                    zos.closeEntry();
                    in.close();
                }
            }
            long end = System.currentTimeMillis();
            logger.info("压缩完成，耗时："+(end - start) +" ms");
        } catch (Exception e) {
            logger.error("压缩异常{}"+e.getMessage());
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

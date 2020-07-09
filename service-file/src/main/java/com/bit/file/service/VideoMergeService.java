package com.bit.file.service;

import com.bit.file.component.FastDFSClient;
import com.bit.file.dao.FileInfoDao;
import com.bit.file.model.FileInfo;
import com.bit.file.model.VideoFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-30 14:40
 */
@Service("videoMergeService")
public class VideoMergeService{
    @Autowired
    private FileInfoDao fileInfoDao;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Value("${fastdfs.address}")
    private String fastAddress;
    @Value("${video.path}")
    private String path;

    /**
     * 异步调用方法 返回结果
     * @param resultFile
     * @param videoFile
     * @return
     */
//    @Async
    @Transactional
    public FileInfo mm(File resultFile, VideoFile videoFile,String[] filepaths){
        long start = System.currentTimeMillis();
        try {
            int bufSize = 1024;
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(resultFile));
            byte[] buffer = new byte[bufSize];

            for (String str : filepaths) {
                String pp = str;
                //url获取文件
                URL url = new URL(pp);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                InputStream inStream = conn.getInputStream();//通过输入流获取图片数据

                //合并
                BufferedInputStream inputStream = new BufferedInputStream(inStream);
                int readcount;
                while ((readcount = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, readcount);
                }
                inStream.close();
                inputStream.close();
            }
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("合并完成,花费了:"+(end-start));
        //处理旧数据 更新业务表
        return  uploadAndDelete(resultFile, videoFile);
    }

    /**
     * 删除旧数据 和 更新业务表
     * @param resultFile
     * @param videoFile
     */
    @Transactional
    public FileInfo uploadAndDelete(File resultFile, VideoFile videoFile){
        List<Long> ids = new ArrayList<>();
        ids = videoFile.getFileids();
        Long fileId = null;

        //读取文件转换成字节流
        try {
            FileInputStream inputStream = new FileInputStream(resultFile);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            System.out.println("上传fast");
            String path = fastDFSClient.uploadFile(buffer, resultFile.getName());

            //新增file表一条空记录 然后把id返回
            FileInfo fileInfo = new FileInfo();
            fileInfo.setPath(path);
            fileInfo.setFileSize(resultFile.getTotalSpace());
            fileInfo.setFileName(videoFile.getFileName());
            fileInfo.setSuffix(videoFile.getSuffix());
            fileInfoDao.insert(fileInfo);
            fileId = fileInfo.getId();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInfo byId = fileInfoDao.findById(fileId);
        if (byId != null){
            byId.setPath(fastAddress + "/" + byId.getPath());
        }
        //删除碎片文件
        if (ids!=null && ids.size()>0){
            fastDFSClient.batchPhysicalDeleteFile(ids);
        }
        //删除临时文件目录
        resultFile.delete();
        return byId;
    }
}

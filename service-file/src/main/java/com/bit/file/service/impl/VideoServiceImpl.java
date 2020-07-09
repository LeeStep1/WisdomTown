package com.bit.file.service.impl;

import com.bit.base.vo.BaseVo;
import com.bit.file.dao.FileInfoDao;
import com.bit.file.model.FileInfo;
import com.bit.file.model.VideoFile;
import com.bit.file.service.VideoMergeService;
import com.bit.file.service.VideoService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-26 16:06
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {
    @Autowired
    private FileInfoDao fileInfoDao;

    @Value("${fastdfs.address}")
    private String fastAddress;
    @Value("${video.path}")
    private String path;
    @Autowired
    private VideoMergeService videoMergeService;

    @Override
    public BaseVo mergeFile(VideoFile videoFile) {
        BaseVo baseVo = new BaseVo();
        List<Long> fileids = videoFile.getFileids();
        List<FileInfo> fileInfoList = new ArrayList<>();
        List<String> fpaths = new ArrayList<>();
        //从数据库中查出视频碎片
        for (Long fileid : fileids) {
            FileInfo byId = fileInfoDao.findById(fileid);
            fileInfoList.add(byId);
            fpaths.add(fastAddress+"/"+byId.getPath());
        }
        File[] files = new File[fpaths.size()];
        for (int i = 0; i < fpaths.size(); i ++) {
            files[i] = new File(fpaths.get(i));
        }
        String resultpath = path + "/" + videoFile.getFileName() + "."+videoFile.getSuffix();
        System.out.println("path:"+resultpath);

        //合并视频文件
        File resultFile = new File(resultpath);
        String[] filepaths = fpaths.toArray(new String[fpaths.size()]);


        FileInfo info = videoMergeService.mm(resultFile, videoFile,filepaths);
        baseVo.setData(info);

        return baseVo;
    }



}

package com.bit.file.service;

import com.bit.base.vo.BaseVo;
import com.bit.file.model.VideoFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-26 16:06
 */
public interface VideoService {


    BaseVo mergeFile(VideoFile videoFile);
}

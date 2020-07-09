package com.bit.file.controller;

import com.bit.base.vo.BaseVo;
import com.bit.file.model.VideoFile;
import com.bit.file.service.VideoService;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-26 15:17
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;


    @PostMapping("/merge")
    public BaseVo mergeFile(@RequestBody VideoFile videoFile){
        return videoService.mergeFile(videoFile);
    }



}

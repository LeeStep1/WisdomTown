package com.bit.file.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-26 15:54
 */
@Data
public class VideoFile implements Serializable {


    private String fileName;

    private String suffix;

    private List<Long> fileids;
}

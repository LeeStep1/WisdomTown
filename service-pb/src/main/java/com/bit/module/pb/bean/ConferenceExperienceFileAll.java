package com.bit.module.pb.bean;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-30 11:32
 */
@Data
public class ConferenceExperienceFileAll {

    private PageInfo<ConferenceExperienceFile> pageInfo;
    /**
     * 改会议下所有的文件id集合
     */
    private List<Long> fileIds;
}

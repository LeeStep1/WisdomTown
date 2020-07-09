package com.bit.officialdoc.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/23 16:24
 */
@Data
public class ArchiveParam implements Serializable {

    private long targetFolderId;

    private long[] archiveIds;
}

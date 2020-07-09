package com.bit.file.model;

import lombok.Data;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-16 10:27
 */
@Data
public class FileList {
    /**
     * 文件id集合
     */
    private List<Long> fileIds;
	/**
	 * 文件名称
	 */
    private String fileName;

}

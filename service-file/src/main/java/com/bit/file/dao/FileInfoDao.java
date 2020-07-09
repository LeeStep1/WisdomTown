package com.bit.file.dao;

import com.bit.file.model.FileInfo;
import com.bit.file.model.FileInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件对象的crud
 * @author lyj
 */
@Component
public interface FileInfoDao {


    /**
     * @param fileInfo
     */
    void insert(FileInfo fileInfo);


    /**
     * @param fileId
     * @return
     */
    List<FileInfo> query(@Param("fileId") Long fileId);


    /**
     * @param fileId
     * @return
     */
    void delete(@Param("fileId") Long fileId);

    /**
     * 根据id主键查询
     * @param id
     * @return
     */
    FileInfo findById(@Param(("id")) Long id);

    /**
     * 批量查询文件信息
     * @param ids
     * @return
     */
    List<FileInfo> batchSelect(@Param(value = "ids") List<Long> ids);

    /**
     * 根据参数查询
     * @param fileInfoVO
     * @return
     */
    List<FileInfo> findByParam(FileInfoVO fileInfoVO);

    /**
     * 查询文件列表
     * @param fileInfo
     * @return
     */
    List<FileInfo> findFileListSql(@Param("fileInfo") FileInfo fileInfo);

    /**
     * 查询文件类型集合
     * @param module
     * @return
     */
    List<String> findFileTypeSql(@Param("module") String module);

    void batchDelete(@Param(value = "ids") List<Long> ids);

    void update(FileInfo fileInfo);

    /**
     * 按照hashcode查询
     * @param hashCode
     * @return
     */
    FileInfo findByHashCode(@Param(value = "hashCode")String hashCode);

    /**
     * 插入 带hashcode
     * @param fileInfo
     */
    void insertHashCode(FileInfo fileInfo);

    /**
     * 根据参数查询
     * @param fileInfoVO
     * @return
     */
    List<FileInfo> findByParamHashCode(FileInfoVO fileInfoVO);

}

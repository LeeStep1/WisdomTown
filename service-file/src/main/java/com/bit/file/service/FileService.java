package com.bit.file.service;

import com.bit.base.vo.BaseVo;
import com.bit.file.model.FileInfo;
import com.bit.file.model.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lyj
 */
public interface FileService {


    /**
     * @param filedata
     * @return
     */
    BaseVo uploadFile(MultipartFile filedata);

    /**
     * @param fileId
     * @return
     */
    byte[]  downloadFile(Long fileId);

    /**
     * @param fileId
     * @return
     */
    List<FileInfo> getFileInfo(Long fileId);


    /**
     * @param fileId
     * @return
     */
    void  delFile(Long fileId) throws Exception;

    /**
     * 批量下载会议心得体会
     * @param response
     * @param conferenceId
     */
//    void batchDownload(HttpServletResponse response, Long conferenceId);

    /**
     * 批量下载会议心得体会
     * @param response
     * @param fileids
     */
    void batchDownload(HttpServletResponse response, HttpServletRequest request, List<Long> fileids,String filename);

    /**
     * 根据id查询文件路径
     * @param fileId
     * @return
     */
    BaseVo findById(Long fileId);

    /**
     * 根据id集合查询
     * @param fileids
     * @return
     */
    BaseVo findByIds(List<Long> fileids);

    /**
     * 根据参数查询
     * @param fileInfoVO
     * @return
     */
    BaseVo findByParams(FileInfoVO fileInfoVO);

    /**
     * 查询文件列表
     * @param fileInfo
     * @return
     */
    BaseVo findFileList(FileInfo fileInfo);

    /**
     * @param filedata
     * @return
     */
    BaseVo uploadFileTest(MultipartFile filedata);
}

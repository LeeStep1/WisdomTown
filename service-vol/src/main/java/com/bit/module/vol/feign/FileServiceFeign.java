package com.bit.module.vol.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.vo.FileInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author chenduo
 * @create 2019-01-21 16:01
 */
@FeignClient(value = "service-file")
public interface FileServiceFeign {
    /**
     * 下载文件
     * @param fileId
     * @param cover
     */
    @RequestMapping(value = "/downLoadFile/{fileId}/{cover}",method = RequestMethod.GET)
    void downloadFile(@PathVariable("fileId") Long fileId, @PathVariable("cover") String cover);

    /**
     * 多参数查询
     * @param fileInfoVO
     * @return
     */
    @RequestMapping(value = "/findByParams",method = RequestMethod.POST)
    BaseVo findByParams(@RequestBody FileInfoVO fileInfoVO);

    /**
     * 根据文件id查询
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/findById/{fileId}",method = RequestMethod.GET)
    BaseVo findById(@PathVariable("fileId") Long fileId);

    /**
     * 根据文件id批量查询
     * @param fileInfoVO
     * @return
     */
    @RequestMapping(value = "/findByIds",method = RequestMethod.POST)
    BaseVo findByIds(@RequestBody FileInfoVO fileInfoVO);
}

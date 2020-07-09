package com.bit.module.pb.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.vo.FileInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author chenduo
 * @create 2019-01-21 16:01
 */
@FeignClient(value = "service-file")
public interface FileServiceFeign {

    @RequestMapping(value = "/downLoadFile/{fileId}/{cover}",method = RequestMethod.GET)
    void downloadFile(@PathVariable("fileId") Long fileId,@PathVariable("cover") String cover);

    @RequestMapping(value = "/findByParams",method = RequestMethod.POST)
    BaseVo findByParams(@RequestBody FileInfoVO fileInfoVO);

    @RequestMapping(value = "/findById/{fileId}",method = RequestMethod.GET)
    BaseVo findById(@PathVariable("fileId") Long fileId);

    @RequestMapping(value = "/findByIds",method = RequestMethod.POST)
    BaseVo findByIds(@RequestBody FileInfoVO fileInfoVO);

    @RequestMapping(value = "/fileDel/{fileId}",method = RequestMethod.DELETE)
    BaseVo fileDel(@PathVariable("fileId") Long fileId);
}

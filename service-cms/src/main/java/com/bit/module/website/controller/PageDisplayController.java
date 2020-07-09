package com.bit.module.website.controller;

import com.alibaba.fastjson.JSON;
import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.FileInfo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.vo.PortalCategoryVO;
import com.bit.module.manager.vo.PortalContentVO;
import com.bit.module.website.feign.FileFeign;
import com.bit.module.website.service.PageDisplayService;
import com.bit.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @description: 页面展示
 * @author: liyang
 * @date: 2019-05-07
 **/
@RestController
@RequestMapping("/pageDisplay")
public class PageDisplayController {

    @Autowired
    private PageDisplayService pageDisplayService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private FileFeign fileFeign;

    /**
     * @description: 根据站点获取所有导航
     * @author: liyang
     * @date: 2019-05-07
     **/
    @GetMapping("/allNavigation/{stationId}")
    public BaseVo allNavigation(@PathVariable(value = "stationId") Long stationId){
         return pageDisplayService.getAllNavigation(stationId);
    }

    /**
     * @description: 根据站点和导航多个栏目列表同时展示（不分页）
     * @author: liyang
     * @param stationId  站点ID
     * @param portalCategoryVO  栏目明细以及分页
     * @date: 2019-05-08
     **/
    @PostMapping("/categoryListContentList/{stationId}")
    public BaseVo categoryListContentList(@PathVariable(value = "stationId") Long stationId,@RequestBody PortalCategoryVO portalCategoryVO){
        portalCategoryVO.setStationId(stationId);
        return pageDisplayService.getContentListByCategoryIdList(portalCategoryVO);
    }

    /**
     * @description: 根据导航ID和栏目获取单个下内容列表(分页)
     * @author: liyang
     * @param stationId  站点ID
     * @param portalContentVO  栏目明细以及分页
     * @date: 2019-05-08
     **/
    @PostMapping("/contentList/{stationId}")
    public BaseVo contentList(@PathVariable(value = "stationId") Long stationId,@RequestBody PortalContentVO portalContentVO){
        portalContentVO.setStationId(stationId);
        return pageDisplayService.getContentListByCategoryId(portalContentVO);
    }

    /**
     * 根据栏目ID和内容ID获取内容明细
     * @author liyang
     * @date 2019-05-09
     * @param stationId : 站点ID
     * @param navigationId : 导航ID
     * @param categoryId : 栏目ID
     * @param contentId :   内容ID
    */
    @GetMapping("/contentDetail/{stationId}/{navigationId}/{categoryId}/{contentId}")
    public BaseVo contentDetail(@PathVariable(value = "stationId") Long stationId,
                                @PathVariable(value = "navigationId") Long navigationId,
                                @PathVariable(value = "categoryId") Long categoryId,
                                @PathVariable(value = "contentId") Long contentId){
        PortalContent portalContent = new PortalContent();
        portalContent.setStationId(stationId);
        portalContent.setCategoryId(categoryId);
        portalContent.setId(contentId);
        return pageDisplayService.getContentDetailByContentId(portalContent);
    }

    /**
     * 根据根据栏目ID 查询栏目下的推荐内容
     * @author liyang
     * @date 2019-05-21
     * @param stationId : 站点ID
     * @param portalContentVO : 栏目ID,内容ID,查询条数
     * @return : BaseVo
    */
    @PostMapping("/contentDetail/{stationId}")
    public BaseVo recommendContentList(@PathVariable(value = "stationId") Long stationId,
                                       @RequestBody PortalContentVO portalContentVO){

        return pageDisplayService.getRecommendContentList(stationId,portalContentVO);

    }

    /**
     * 下载上传的文件
     * @author liyang
     * @date 2019-05-30
     * @param fileId : 文件详情
     * @return : BaseVo
     */
    @GetMapping("/downLoadFile/{fileId}")
    public void downLoadFile(HttpServletResponse response,@PathVariable(value = "fileId") Long fileId) throws IOException{

        OutputStream toClient = null;
        try {
            BaseVo baseVo =fileFeign.findById(fileId);
            String fileStr = JSON.toJSONString(baseVo.getData());
            FileInfo fileInfo = JSON.parseObject(fileStr,FileInfo.class);
            String pathTem = fileInfo.getPath().replace("http://","").replace("https://","");//去除http和https前缀
            int a = pathTem.indexOf("/");
            String path = pathTem.substring(a+1);
            byte[] buffer = fastDFSClient.downloadFile(path);
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileInfo.getFileName(), "UTF-8").replaceAll("\\+","%20") + "."+fileInfo.getSuffix());
            response.addHeader("Content-Length", "" + buffer.length);
            // 通过文件流的形式写到客户端
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 写完以后关闭文件流
            toClient.flush();
            toClient.close();
        }

    }

}

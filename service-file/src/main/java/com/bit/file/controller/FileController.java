package com.bit.file.controller;


import com.bit.base.controller.BaseController;
import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.bit.file.component.FastDFSClient;
import com.bit.file.model.FileInfo;
import com.bit.file.model.FileInfoVO;
import com.bit.file.model.FileList;
import com.bit.file.service.FileService;
import com.bit.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/")
public class FileController extends BaseController {


    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     * @param filedata :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2018-10-13
     */
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public BaseVo uploadFileNew(@RequestParam MultipartFile filedata) {
        if (filedata != null && !filedata.isEmpty()) {
            return fileService.uploadFile(filedata);
        }else {
            throw new BusinessException("文件不允许为空");
        }
    }

    /**
     * 上传文件
     * @param file :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2018-10-13
     */
    @RequestMapping(value = "uploadVideoFile", method = RequestMethod.POST)
    public BaseVo uploadVideoFile(@RequestParam MultipartFile file,@RequestParam Integer chunkNumber) {
        System.out.println(chunkNumber);
        if (file != null && !file.isEmpty()) {
            System.out.println("end");
            return fileService.uploadFile(file);
        }else {
            throw new BusinessException("文件不允许为空");
        }
    }


    /**
     * @param fileId : 文件id
     * @return : com.bit.base.vo.BaseVo
     * @description:下载服务
     * @author liyujun
     * @date 2018-10-13
     */
    @RequestMapping(value = "getDownLoadFile/{fileId}", method = RequestMethod.GET)
    public void getDownLoadFile(HttpServletResponse response, @PathVariable("fileId") Long fileId) throws IOException {
        OutputStream toClient = null;
        try {

            List<FileInfo> infoList = new ArrayList<FileInfo>();
            infoList = fileService.getFileInfo(fileId);
            // 判断文件是否存在
            if (infoList.size() > 0) {
                FileInfo info = infoList.get(0);
                byte[] buffer = fastDFSClient.downloadFile(info.getPath());
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(info.getFileName(), "UTF-8").replaceAll("\\+","%20") + "."+info.getSuffix());
                response.addHeader("Content-Length", "" + buffer.length);
                // 通过文件流的形式写到客户端
                toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);

            }
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


    /**
     * @param fileId :
     * @return : com.bit.base.vo.BaseVo
     * @description: 删除某个文件
     * @author liyujun
     * @date 2018-10-15
     */
    @RequestMapping(value = "fileDel/{fileId}", method = RequestMethod.DELETE)
    public BaseVo fileDel(@PathVariable("fileId") Long fileId) {
        BaseVo rs = new BaseVo();
        try {
            fileService.delFile(fileId);
            rs.setCode(ResultCode.SUCCESS.getCode());
            rs.setMsg(ResultCode.SUCCESS.getInfo());
        } catch (Exception e) {
            e.printStackTrace();
            rs.setCode(ResultCode.WRONG.getCode());
            rs.setMsg(e.getMessage());
        }
        return rs;
    }


    @RequestMapping(value = "downLoadFile/{fileId}/{cover}", method = RequestMethod.GET)
    public void downLoadFile(HttpServletResponse response, @PathVariable("fileId") Long fileId,@PathVariable("cover") String cover) throws IOException {


        //从数据库中查出来文件的信息
        List<FileInfo> infoList = new ArrayList<FileInfo>();
        infoList = fileService.getFileInfo(fileId);
        OutputStream out =null;
        // 判断文件是否存在
        if (infoList.size() > 0){
            FileInfo info = infoList.get(0);
            OutputStream toClient = null;
            try {

                byte[] buffer = fastDFSClient.downloadFile(info.getPath());

                //获得新建文件夹的路径

                File file = new File("D:\\files\\"+cover+"\\"+info.getFileName()+"."+info.getSuffix());
                if (file.exists()){
                    deleteFile(file);
                }else {
                    file.createNewFile();
                }
                // 通过文件流的形式写到客户端
                 out = new FileOutputStream(file);
                toClient = new BufferedOutputStream(out);
                toClient.write(buffer);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                toClient.flush();
                toClient.close();
                out.flush();
                out.close();

            }
        }

    }



    @PostMapping("/batchDownload")
    public void batchDownload(@RequestBody FileList fileList, HttpServletResponse response, HttpServletRequest request){
        List<Long> fileids = new ArrayList<>();
        fileids = fileList.getFileIds();
        String filename = "";
        if (StringUtil.isNotEmpty(fileList.getFileName())){
            filename = "会议心得";
        }else {
            filename = fileList.getFileName();
        }
        fileService.batchDownload(response,request,fileids,filename);
    }


    //递归删除文件夹
    private boolean deleteFile(File dirFile){
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();

    }

    /**
     * 根据fileId查询file对象
     * @param fileId
     * @return
     */
    @GetMapping("/findById/{fileId}")
    public BaseVo findById(@PathVariable("fileId") Long fileId){
        return fileService.findById(fileId);
    }

    /**
     * 根据id集合查询
     * @param fileInfoVO
     * @return
     */
    @PostMapping("/findByIds")
    public BaseVo findByIds(@RequestBody FileInfoVO fileInfoVO){
        return fileService.findByIds(fileInfoVO.getFileIds());
    }

    /**
     * 根据参数查询
     * @param fileInfoVO
     * @return
     */
    @PostMapping("/findByParams")
    public BaseVo findByParams(@RequestBody FileInfoVO fileInfoVO){
        return fileService.findByParams(fileInfoVO);
    }

    /**
     * 查询文件列表接口
     * @param fileInfo
     * @return
     */
    @PostMapping("/findFileList")
    public BaseVo findFileList(@RequestBody FileInfo fileInfo){

        return fileService.findFileList(fileInfo);

    }

    /**
     * 批量打包下载
     * @author liyang
     * @date 2019-07-19
     * @param fileList : 要下载的文件集合
     * @param filename : 打包名称
    */
    @PostMapping("/batchDownloadZip")
    public void batchDownloadZip(@RequestBody FileList fileList,String filename, HttpServletResponse response, HttpServletRequest request){
        List<Long> fileids = new ArrayList<>();
        fileids = fileList.getFileIds();
        fileService.batchDownload(response,request,fileids,filename);
    }

    /**
     * 测试上传文件
     * @param filedata :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author chenduo
     * @date 2018-10-13
     */
    @RequestMapping(value = "uploadFileTest", method = RequestMethod.POST)
    public BaseVo uploadFileTest(@RequestParam MultipartFile filedata) {
        if (filedata != null && !filedata.isEmpty()) {
            return fileService.uploadFileTest(filedata);
        }else {
            throw new BusinessException("文件不允许为空");
        }
    }
}


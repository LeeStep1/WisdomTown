package com.bit.file.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.file.component.FastDFSClient;
import com.bit.file.dao.FileInfoDao;
import com.bit.file.feign.PbFeign;
import com.bit.file.model.FileInfo;
import com.bit.file.model.FileInfoReturn;
import com.bit.file.model.FileInfoVO;
import com.bit.file.service.FileService;
import com.bit.file.utils.FileUtil;
import com.bit.file.utils.ZipFileUtil;
import com.bit.utils.DateUtil;
import com.bit.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private FileInfoDao fileInfoDao;

    @Autowired
    private PbFeign pbFeign;

    @Value("${fastdfs.address}")
    private String fastAddress;

    @Value("${dict.module}")
    private String fileType;

    @Override
    public BaseVo uploadFile(MultipartFile filedata) {
        BaseVo baseVo = new BaseVo();
        FileInfo info = new FileInfo();
        try {
            info.setFileName(FileUtil.getFileName(filedata.getOriginalFilename()));
            info.setSuffix(FileUtil.getExtensionName(filedata.getOriginalFilename()));
            info.setFileSize(filedata.getSize());
            String path = fastDFSClient.uploadFile(filedata.getBytes(), filedata.getOriginalFilename());
            info.setPath(path);
            fileInfoDao.insert(info);
            FileInfo fileInfo = fileInfoDao.findById(info.getId());
            if (fileInfo != null){
                fileInfo.setPath(fastAddress + "/" + fileInfo.getPath());
            }
            baseVo.setData(fileInfo);
            return baseVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseVo;
    }


    @Override
    public byte[] downloadFile(Long fileId) {
        byte[] buffer = null;
        List<FileInfo> list = fileInfoDao.query(fileId);
        if (list.size() < 0) {
            //throw new Exception("无此文件");
        } else {
            FileInfo info = list.get(0);
            try {
                buffer = fastDFSClient.downloadFile(info.getPath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

    @Override
    public List<FileInfo> getFileInfo(Long fileId) {
        return fileInfoDao.query(fileId);
    }


    @Override
    @Transactional
    public void delFile(Long fileId) throws Exception {
        List<FileInfo> list = fileInfoDao.query(fileId);
        if (!list.isEmpty()) {
            fastDFSClient.deleteFile(list.get(0).getPath());
            fileInfoDao.delete(fileId);
            pbFeign.pbDelFile(fileId);
        } else {
            throw new Exception("无此文件");
        }

    }


    @Override
    public void batchDownload(HttpServletResponse response, HttpServletRequest request,List<Long> fileids,String filename) {

        if (fileids==null||fileids.size()<=0){
            throw new BusinessException("下载文件参数异常");
        }
        String contextpath = request.getServletContext().getRealPath("/");

        logger.info("contextpath:{}",contextpath);

        //根据id查询文件信息
        List<FileInfo> fileInfos = new ArrayList<>();
        fileInfos = fileInfoDao.batchSelect(fileids);

        String cover = UUIDUtil.getUUID();
        List<File> ls=new ArrayList<>(10);
        //新建外层文件夹 保存下载的文件
        File origin = new File(contextpath+"/files/"+cover+"/");
        if (origin.exists()){
            deleteFile(origin);
        }else {
            origin.mkdirs();
        }
        if (fileInfos.size()>0){

            for (FileInfo fileInfo : fileInfos) {
                //开始下载文件
                OutputStream toClient = null;
                OutputStream out =null;
                try {
                    //下载文件
                    byte[] buffer = fastDFSClient.downloadFile(fileInfo.getPath());
                    File file = new File(contextpath+"/files/"+cover+"/"+fileInfo.getFileName()+"."+fileInfo.getSuffix());
                    if (file.exists()){
                        deleteFile(file);
                    }else {
                        file.createNewFile();
                    }
                    ls.add(file);
                    // 通过文件流的形式写到客户端
                    out = new FileOutputStream(file);
                    toClient = new BufferedOutputStream(out);
                    toClient.write(buffer);
                }catch (Exception e){

                }finally {
                    //关闭流
                    try {
                        toClient.flush();
                        toClient.close();
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
            //压缩文件
            String path = origin.getPath();
            //文件下载和压缩完成 开始用流输出
            OutputStream outputStream = null;
            //压缩包文件
            File zip = new File(path+".zip");
            try {
                //读取文件
                ZipFileUtil.toZip(ls,contextpath+"/files",cover);
                deleteFile(origin);
                InputStream in = new FileInputStream(zip);
                //流转换为字节
                ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
                byte[] bb = new byte[2048];
                int n = 0;
                while ((n = in.read(bb)) != -1) {
                    bytestream.write(bb, 0, n);
                }
                byte[] data = bytestream.toByteArray();
                //关闭流
                bytestream.flush();
                bytestream.close();
                in.close();
                String fileName = filename + DateUtil.date2String(new Date(),DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("/+","%20") + ".zip");

                response.addHeader("Content-Length", "" + data.length);
                response.setContentType("application/octet-stream");
                outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(data);
                //删除压缩包
                deleteFile(zip);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (outputStream!=null){
                        outputStream.flush();
                        outputStream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public BaseVo findById(Long fileId) {
        BaseVo baseVo = new BaseVo();
        FileInfo fileInfo = fileInfoDao.findById(fileId);
        if (fileInfo != null){
            fileInfo.setPath(fastAddress + "/" + fileInfo.getPath());
            baseVo.setData(fileInfo);
        }
        return baseVo;
    }

    @Override
    public BaseVo findByIds(List<Long> fileids) {
        BaseVo baseVo = new BaseVo();
        List<FileInfo> fileInfos = fileInfoDao.batchSelect(fileids);
        for (FileInfo fileInfo : fileInfos) {
            fileInfo.setPath(fastAddress + "/" + fileInfo.getPath());
        }
        baseVo.setData(fileInfos);
        return baseVo;
    }

    @Override
    public BaseVo findByParams(FileInfoVO fileInfoVO) {
        BaseVo baseVo = new BaseVo();
        List<FileInfo> fileInfoList = fileInfoDao.findByParam(fileInfoVO);
        baseVo.setData(fileInfoList);
        return baseVo;
    }

    /**
     * 查询文件列表
     * @param fileInfo  查询条件
     * @return
     */
    @Override
    public BaseVo findFileList(FileInfo fileInfo) {
        FileInfoReturn fileInfoReturn = new FileInfoReturn();

        //根据条件查询列表
        List<FileInfo> fileInfoList = fileInfoDao.findFileListSql(fileInfo);

        fileInfoReturn.setFileInfoList(fileInfoList);

        //查询文件类型列表
        List<String> fileTypeList = fileInfoDao.findFileTypeSql(fileType);

        fileInfoReturn.setFileTypeList(fileTypeList);

        BaseVo baseVo = new BaseVo();

        baseVo.setData(fileInfoReturn);

        return baseVo;
    }

    /**
     * 测试上传文件
     * @param filedata :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author chenduo
     * @date 2018-10-13
     */
    @Override
    public BaseVo uploadFileTest(MultipartFile filedata) {
        BaseVo baseVo = new BaseVo();
        FileInfo info = new FileInfo();
        try {
            //打印hash值
            String hashcode = FileUtil.md5HashCode32(filedata.getInputStream());
            logger.info("文件hash值:{}",hashcode);
            FileInfoVO vo = new FileInfoVO();
            vo.setHashCode(hashcode);
            List<FileInfo> byParam = fileInfoDao.findByParamHashCode(vo);
            if (!CollectionUtils.isEmpty(byParam)){
                FileInfo fileInfo = fileInfoDao.findByHashCode(hashcode);
                baseVo.setMsg("文件已存在");
                baseVo.setData(fileInfo);
                return baseVo;
            }
            info.setHashCode(hashcode);
            //判断
            info.setFileName(FileUtil.getFileName(filedata.getOriginalFilename()));
            info.setSuffix(FileUtil.getExtensionName(filedata.getOriginalFilename()));
            info.setFileSize(filedata.getSize());
            String path = fastDFSClient.uploadFile(filedata.getBytes(), filedata.getOriginalFilename());
            info.setPath(path);
            fileInfoDao.insertHashCode(info);
            FileInfo fileInfo = fileInfoDao.findById(info.getId());
            if (fileInfo != null){
                fileInfo.setPath(fastAddress + "/" + fileInfo.getPath());
            }

            baseVo.setData(fileInfo);
            return baseVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseVo;
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

}

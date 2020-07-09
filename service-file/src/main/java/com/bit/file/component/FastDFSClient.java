package com.bit.file.component;
import com.bit.file.core.ConnectionPool;
import com.bit.file.dao.FileInfoDao;
import com.bit.file.utils.FileUtil;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class FastDFSClient {


    /** 连接池 */
    @Autowired
    private ConnectionPool connectionPool;
    @Autowired
    private FileInfoDao fileInfoDao;

    /**
     * 上传文件
     *
     * @param buff
     *            文件对象
     * @param fileName
     *            文件名
     * @return
     */
    public String uploadFile(byte[] buff, String fileName)
    {
        return uploadFile(buff, fileName, null, null);
    }

    public String uploadFile(byte[] buff, String fileName, String groupName) {
        return uploadFile(buff, fileName, null, null);
    }

    /**
     * 上传文件
     *
     * @param buff
     *
     * @param fileName
     *            文件名
     * @param metaList
     *            文件元数据
     * @return
     */
    public String uploadFile(byte[] buff, String fileName, Map<String, String> metaList,
                             String groupName)
    {

        try
        {
            NameValuePair[] nameValuePairs = null;
            if (metaList != null)
            {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String, String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();)
                {
                    Map.Entry<String, String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++ ] = new NameValuePair(name, value);
                }
            }

            /** 获取可用的tracker,并创建存储server */

            /*TrackerServer trackerServer= connectionPool.checkout();
            StorageServer storageServer = null;
            StorageClient1 storageClient= new StorageClient1(trackerServer, storageServer);*/
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer= connectionPool.checkout();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);

            String path = null;
            if (!StringUtils.isEmpty(groupName))
            {
                // 上传到指定分组
                path = storageClient.upload_file1(groupName, buff,
                        FileUtil.getExtensionName(fileName), nameValuePairs);
            }
            else
            {
                path = storageClient.upload_file1(buff, FileUtil.getExtensionName(fileName),
                        nameValuePairs);
            }

            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer);

            return path;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件元数据
     *
     * @param fileId
     *            文件ID
     * @return
     */
    public Map<String, String> getFileMetadata(String fileId)
    {

        try
        {

            /** 获取可用的tracker,并创建存储server */
          //  StorageClient1 storageClient = connectionPool.checkout();

            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer= connectionPool.checkout();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);

            NameValuePair[] metaList = storageClient.get_metadata1(fileId);
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer);

            if (metaList != null)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                for (NameValuePair metaItem : metaList)
                {
                    map.put(metaItem.getName(), metaItem.getValue());
                }
                return map;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param fileId
     *            文件ID
     * @return 删除失败返回-1，否则返回0
     */
    public int deleteFile(String fileId)
    {
        try
        {

            /** 获取可用的tracker,并创建存储server */
           // StorageClient1 storageClient = connectionPool.checkout();
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer= connectionPool.checkout();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);

            int i = storageClient.delete_file1(fileId);
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer);

            return i;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 下载文件
     *
     * @param fileId
     *            文件ID（上传文件成功后返回的ID）
     * @return
     */
    public byte[] downloadFile(String fileId) throws Exception {
        try
        {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer= connectionPool.checkout();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);

            /** 获取可用的tracker,并创建存储server */
           // StorageClient1 storageClient = connectionPool.checkout();

            byte[] content = storageClient.download_file1(fileId);
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer);

            return content;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (MyException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description:获取文件信息
     *
     * @param fileId
     * @return
     * @see
     */
    public FileInfo getFileInfo(String fileId) throws Exception {

        try
        {
            /** 获取可用的tracker,并创建存储server */
            //StorageClient1 storageClient = connectionPool.checkout();
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer= connectionPool.checkout();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
            FileInfo fileInfo = storageClient.get_file_info1(fileId);
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer);

            return fileInfo;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (MyException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 批量物理删除文件
     *
     * @param ids
     *            文件ID集合
     * @return 删除失败返回-1，否则返回0
     */
    public void batchPhysicalDeleteFile(List<Long> ids)
    {
        try
        {
            //物理删除
            for (Long id : ids) {
                /** 获取可用的tracker,并创建存储server */
                // StorageClient1 storageClient = connectionPool.checkout();
                TrackerClient trackerClient = new TrackerClient();
                TrackerServer trackerServer= connectionPool.checkout();
                StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
                StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);

                int i = storageClient.delete_file1(id.toString());
                /** 上传完毕及时释放连接 */
                connectionPool.checkin(trackerServer);
            }
            //数据库删除
            fileInfoDao.batchDelete(ids);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

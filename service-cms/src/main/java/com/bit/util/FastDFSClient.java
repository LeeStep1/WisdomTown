package com.bit.util;

import com.bit.common.core.ConnectionPool;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FastDFSClient {


    /** 连接池 */
    @Autowired
    private ConnectionPool connectionPool;

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

}

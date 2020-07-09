package com.bit.file.config;

import com.bit.file.core.ConnectionPool;
import org.csource.fastdfs.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author lyj
 */
@Configuration
public class FastDFSConfig extends WebMvcConfigurerAdapter {

    static final String FASTDFS_CONFIG = "fdfs-client.conf";

    @Bean
    public ConnectionPool initStorageClient1(){
        ConnectionPool  pool=new ConnectionPool(10,30,200);
        return  pool;
    }

   @Bean
    public StorageClient1 initStorageClient()
    {
        StorageClient1 storageClient = null;
        try
        {
            ClientGlobal.init(FASTDFS_CONFIG);

            System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
            //ClientGlobal.setG_secret_key("123456");
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null)
            {
                throw new IllegalStateException("getConnection return null");
            }
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null)
            {
                throw new IllegalStateException("getStoreStorage return null");
            }
            storageClient = new StorageClient1(trackerServer, storageServer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return storageClient;
    }
}

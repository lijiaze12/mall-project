package com.changgou.file.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/10 11:50
 * @description 标题
 * @package com.changgou.file.util
 */
public class FastDFSClient {
    static {
        try {
            ClassPathResource classPathResource = new ClassPathResource("fdfs_client.conf");
            ClientGlobal.init(classPathResource.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传图片
    // [0]-->group1
    // [1]--->M00/00/00/JAJHSDFKAHFKA.jpg
    public static String[] upload(FastDFSFile file) throws Exception {
        //3.创建一个trackerClient
        TrackerClient trackerClient = new TrackerClient();
        //4.获取trackerserver
        TrackerServer trackerServer = trackerClient.getConnection();
        //5.创建storageServer 赋值为空
        //6.创建storageClient （提供了很多图片操作的API)
        //参数1 指定trackserver
        //参数2 指定storageserver
        StorageClient storageClient = new StorageClient(trackerServer,null);
        //7.执行上传图片
        //参数1 指定图片的的字节数组
        //参数2 指定图片的后缀 去掉 "点"
        //参数3 指定图片的元数据 ：高度 宽度  像素  作者  拍摄日期 文件大小.........
        NameValuePair[] meta_list = new NameValuePair[]{
                new NameValuePair(file.getName())//元数据
        };
        String[] pngs = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        return pngs;
    }

    //下载图片

    public static byte[] downFile(String groupName, String remoteFileName) throws Exception {
        //3.创建一个trackerClient
        TrackerClient trackerClient = new TrackerClient();
        //4.获取trackerserver
        TrackerServer trackerServer = trackerClient.getConnection();
        //5.创建storageServer 赋值为空
        //6.创建storageClient （提供了很多图片操作的API)
        //参数1 指定trackserver
        //参数2 指定storageserver
        StorageClient storageClient = new StorageClient(trackerServer,null);
        //7.执行上传图片
        //参数1 组名  参数2  文件名
        byte[] bytes = storageClient.download_file(groupName, remoteFileName);
        return bytes;
    }

    //删除图片
    public static boolean deleteFile(String groupName,String remoteFileName) throws Exception{
        //3.创建一个trackerClient
        TrackerClient trackerClient = new TrackerClient();
        //4.获取trackerserver
        TrackerServer trackerServer = trackerClient.getConnection();
        //5.创建storageServer 赋值为空
        //6.创建storageClient （提供了很多图片操作的API)
        //参数1 指定trackserver
        //参数2 指定storageserver
        StorageClient storageClient = new StorageClient(trackerServer,null);
        int group1 = storageClient.delete_file(groupName, remoteFileName);
        return group1==0;
    }

}

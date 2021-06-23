package com.changgou;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 该测试类用于测试 API 实现图片上传
 *
 * @author ljh
 * @version 1.0
 * @date 2020/11/10 11:11
 * @description 标题
 * @package com.changgou
 */

public class FastdfsTest {

    //上传图片
    @Test
    public void upload() throws Exception {
        //1.创建一个配置文件 用于配置连接到tracker server的ip地址和端口
        //2.加载配置文件使其生效
        ClientGlobal.init("C:\\Users\\admin\\IdeaProjects\\changgou101\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
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
        //参数1 指定图片的所在的路径
        //参数2 指定图片的后缀 去掉 "点"
        //参数3 指定图片的元数据 ：高度 宽度  像素  作者  拍摄日期 文件大小.........
        String[] pngs = storageClient.upload_file("C:\\Users\\admin\\Pictures\\45.png", "png", null);
        for (String png : pngs) {
            System.out.println(png);
        }
    }

    //下载图片 单例模式

    @Test
    public void download() throws Exception{
        //1.创建一个配置文件 用于配置连接到tracker server的ip地址和端口
        //2.加载配置文件使其生效
        ClientGlobal.init("C:\\Users\\admin\\IdeaProjects\\changgou101\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
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
        byte[] bytes = storageClient.download_file("group1", "M00/00/00/wKjThF-qeNyATiVHAAAl8vdCW2Y824.png");

        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\1010\\1234.jpg"));
        fileOutputStream.write(bytes);
        fileOutputStream.close();//放finally中
    }

    //删除图片

    @Test
    public void deleteFile() throws Exception{
        //1.创建一个配置文件 用于配置连接到tracker server的ip地址和端口
        //2.加载配置文件使其生效
        ClientGlobal.init("C:\\Users\\admin\\IdeaProjects\\changgou101\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //3.创建一个trackerClient
        TrackerClient trackerClient = new TrackerClient();
        //4.获取trackerserver
        TrackerServer trackerServer = trackerClient.getConnection();
        //5.创建storageServer 赋值为空
        //6.创建storageClient （提供了很多图片操作的API)
        //参数1 指定trackserver
        //参数2 指定storageserver
        StorageClient storageClient = new StorageClient(trackerServer,null);
        int group1 = storageClient.delete_file("group1", "M00/00/00/wKjThF-qeNyATiVHAAAl8vdCW2Y824.png");
        if(group1==0){
            System.out.println("成功");
        }else{
            System.out.println("成仁");
        }

    }

}

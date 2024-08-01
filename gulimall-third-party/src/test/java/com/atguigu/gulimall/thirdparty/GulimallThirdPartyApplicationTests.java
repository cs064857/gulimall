package com.atguigu.gulimall.thirdparty;

import com.atguigu.gulimall.thirdparty.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sun.net.www.content.image.png;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GulimallThirdPartyApplicationTests {
    @Autowired
    private MinioService minioService;
    @Autowired
    private MinioClient minioClient;

    @Test
    void contextLoads() {
        minioService.testMinIOClient();
    }

    @Test
    public void test() throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("gulimall").build());
        System.out.println(bucketExists);
    }

    //測試Object
    //上傳
    //使用putObject上傳
    @Test
    public void test1() throws Exception {
        File file = new File("C:\\Users\\User\\Desktop\\miniotest\\Snipaste_2024-06-03_21-25-53.png");
        ObjectWriteResponse gulimall = minioClient.putObject(PutObjectArgs.builder()
                .bucket("gulimall")//桶的名稱
                .object("test.png")//存到Minio中的名稱
                                                   //文件大小或-1, -1或者緩存大小
                .stream(new FileInputStream(file), file.length(), -1)
                .build());
        System.out.println(gulimall);
    }

    //使用uploadObject上傳(省去FileInputStream輸入流)
    @Test
    public void test2() throws Exception {
        minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket("gulimall")
                .object("test2.png")
                .filename("C:\\Users\\User\\Desktop\\miniotest\\Snipaste_2024-06-03_21-25-53.png")
                .build());
    }
    //預生成簽名URL
    @Test
    public void test3() throws Exception {
        String objectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket("gulimall")//指定minio桶名
                .object("test2.png")//指定文件名稱
                .method(Method.PUT)//PUT為上傳,GET為下載,DELETE為刪除
                .expiry(3, TimeUnit.MINUTES)//設定時間,3分鐘後過期
                .build());
        System.out.println(objectUrl);//打印URL
    }
}

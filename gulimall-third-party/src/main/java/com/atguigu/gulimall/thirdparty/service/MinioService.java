package com.atguigu.gulimall.thirdparty.service;

import io.minio.MinioClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ClassName: MinioService
 * Description:
 *
 * @Create 2024/7/28 上午4:27
 */
@Service
public class MinioService {
    @Resource
    private MinioClient minioClient;

    public void  testMinIOClient(){
        System.out.println(minioClient);
    }
}

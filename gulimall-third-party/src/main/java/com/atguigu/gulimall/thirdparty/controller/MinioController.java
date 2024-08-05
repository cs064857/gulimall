package com.atguigu.gulimall.thirdparty.controller;

import cn.hutool.core.lang.UUID;
import com.atguigu.gulimall.thirdparty.util.MinioUtil;
import com.atguigu.gulimall.thirdparty.util.R;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: MinioController
 * Description:
 *
 * @Create 2024/7/28 上午3:30
 */
@Slf4j
@RestController
@RequestMapping("thirdparty")
public class MinioController {
    @Resource
    private MinioUtil minioUtil;
    @Autowired
    private MinioClient minioClient;

    @PostMapping("/oss/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("gulimall")
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            String fileUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket("gulimall")
                            .object(fileName)
                            .method(Method.GET)
                            .build()
            );
            log.info("fileUrl:{}",fileUrl);
            return R.ok().put("url", fileUrl);
        } catch (Exception e) {
            log.error("文件上傳失敗", e);
            return R.error("文件上傳失敗: " + e.getMessage());
        }
    }
    @GetMapping("/oss/geturl")
    public R generatorUrl() throws Exception {
        String fileName = UUID.randomUUID().toString();
        String objectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket("gulimall")
                .expiry(5, TimeUnit.MINUTES)
                .method(Method.POST)
                .object(fileName)
                .build());
        return R.ok().put("url",objectUrl);
    }
    @RequestMapping("/download")
    public void download(String fileName, HttpServletResponse response) {
        minioUtil.fileDownload(fileName, false, response);
    }
}

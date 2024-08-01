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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

//    @RequestMapping("/oss/upload")
//    public R upload(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//            minioClient.putObject(PutObjectArgs.builder()
//                    .bucket("gulimall")
//                    .object(fileName)
//                    .stream(file.getInputStream(), file.getSize(), -1)
//                    .contentType(file.getContentType())
//                    .build());
//
//            // 構建直接訪問的 URL
//            String fileUrl = minioUtil.getObjectURL("gulimall", fileName);
//            return R.ok().put("url", fileUrl);
//        } catch (Exception e) {
//            log.error("文件上傳失敗", e);
//            return R.error("文件上傳失敗: " + e.getMessage());
//        }
//    }

    @RequestMapping("/oss/upload")
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

//    @RequestMapping("/oss/upload")
//    public R upload(MultipartFile multipartFile){
//        try {
//            minioClient.putObject(PutObjectArgs.builder()
//                    .bucket("gulimall")
//                    .object("uptest.png")
//                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
//                    .build());
//            System.out.println("上傳成功");
//        } catch (ErrorResponseException e) {
//            throw new RuntimeException(e);
//        } catch (InsufficientDataException e) {
//            throw new RuntimeException(e);
//        } catch (InternalException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidResponseException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (ServerException e) {
//            throw new RuntimeException(e);
//        } catch (XmlParserException e) {
//            throw new RuntimeException(e);
//        }
//        return R.ok();
//    }

//    @RequestMapping("/oss/policy")
//    public R upload(MultipartFile multipartFile) throws IOException {
//        log.info("multipartFile:{}",multipartFile);
//        try {
//            if (multipartFile == null || multipartFile.isEmpty()) {
//                return R.error("文件為空");
//            }
//            String fileUrl = minioUtil.upload(multipartFile);
//            return R.ok().put("fileUrl", fileUrl);
//        } catch (Exception e) {
//            return R.error("文件上傳失敗: " + e.getMessage());
//        }
//    }

    @RequestMapping("/download")
    public void download(String fileName, HttpServletResponse response) {
        minioUtil.fileDownload(fileName, false, response);
    }

//    @GetMapping("/presigned-url")
//    public ResponseEntity<Map<String, String>> getPresignedUrl(@RequestParam String fileName) {
//        try {
//            String objectName = UUID.randomUUID().toString() + "_" + fileName;
//            String presignedUrl = minioClient.getPresignedObjectUrl(
//                    GetPresignedObjectUrlArgs.builder()
//                            .bucket("gulimall")
//                            .object(objectName)
//                            .method(Method.PUT)
//                            .expiry(5, TimeUnit.MINUTES)
//                            .build()
//            );
//
//            Map<String, String> response = new HashMap<>();
//            response.put("presignedUrl", presignedUrl);
//            response.put("objectName", objectName);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

}

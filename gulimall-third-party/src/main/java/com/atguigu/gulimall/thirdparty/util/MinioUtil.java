package com.atguigu.gulimall.thirdparty.util;

import cn.hutool.core.util.ObjectUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang.BooleanUtils;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;


/**
 * ClassName: MinioUtil
 * Description:
 *
 * @Create 2024/7/28 上午3:24
 */
@Component
public class MinioUtil {
    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.endpoint}")
    private String endpoint;

    public String getObjectURL(String bucketName, String objectName) {
        try {
            // 構建直接訪問的 URL
            return String.format("%s/%s/%s", endpoint, bucketName, objectName);
        } catch (Exception e) {
            throw new RuntimeException("獲取對象 URL 失敗", e);
        }
    }

    /**
     * description: 判斷bucket是否存在，不存在則創建
     */
    @SneakyThrows
    public boolean existBucket(String name) {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
        }
        return exists;
    }

    /**
     * 創建存儲bucket
     *
     * @param bucketName 存儲bucket名稱
     * @return Boolean
     */
    @SneakyThrows
    public Boolean makeBucket(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
            return true;
        }
        return false;
    }

    /**
     * 刪除存儲bucket
     *
     * @param bucketName 存儲bucket名稱
     * @return Boolean
     */
    @SneakyThrows
    public Boolean removeBucket(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return false;

        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        for (Result<Item> result : results) {
            Item item = result.get();
            // 桶不為空不允許刪除
            if (item.size() > 0) {
                return false;
            }
        }
        minioClient.removeBucket(RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build());
        return true;
    }

    /**
     * 列出所有存儲桶
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 列出所有存儲桶名稱
     */
    public List<String> listBucketNames() {
        List<Bucket> bucketList = listBuckets();

        if (ObjectUtil.isEmpty(bucketList))
            return null;
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 列出存儲桶中的所有對象名稱
     *
     * @param bucketName 存儲桶名稱
     */
    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return null;

        List<String> listObjectNames = new ArrayList<>();
        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        for (Result<Item> result : results) {
            Item item = result.get();
            listObjectNames.add(item.objectName());
        }
        return listObjectNames;
    }

    /**
     * 查看文件對象
     *
     * @param bucketName 存儲bucket名稱
     * @return 存儲bucket內文件對象信息
     */
    public Map<String, Object> listObjects(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return null;

        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        Map<String, Object> map = new HashMap<>();
        try {
            for (Result<Item> result : results) {
                Item item = result.get();
                map.put(item.objectName(), item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    /**
     * 文件訪問路徑
     *
     * @param bucketName 存儲桶名稱
     * @param objectName 存儲桶裡的對象名稱
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return null;

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(2, TimeUnit.MINUTES)
                        .build());
    }

    /**
     * 刪除一個對象
     *
     * @param bucketName 存儲桶名稱
     * @param objectName 存儲桶裡的對象名稱
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        boolean exist = existBucket(bucketName);
        if (!exist) return false;

        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        return true;
    }

    /**
     * 刪除指定桶的多個文件對象
     *
     * @param bucketName  存儲桶名稱
     * @param objectNames 含有要刪除的多個object名稱的迭代器對象
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, List<String> objectNames) {
        boolean exist = existBucket(bucketName);
        if (!exist) return false;

        List<DeleteObject> objects = new LinkedList<>();
        for (String objectName : objectNames) {
            objects.add(new DeleteObject(objectName));
        }
        minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
        return true;
    }

    /**
     * 批量刪除文件對象
     *
     * @param bucketName 存儲bucket名稱
     * @param objects    對象名稱集合
     */
    public Iterable<Result<DeleteError>> removeObjects(String bucketName, List<String> objects) {
        List<DeleteObject> dos = objects.stream().map(DeleteObject::new).collect(Collectors.toList());
        return minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());
    }

    /**
     * 文件上傳
     */
    public String upload(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        // 注意，這裡需要加上 \\ 將 特殊字符 . 轉意 \\. ,否則異常
        assert fileName != null;
        String[] fileArray = fileName.split("\\.");

        // 獲取當前日期
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(now);

        fileName = format + "/" + fileArray[0] + System.currentTimeMillis() + "." + fileArray[1];

        try {
            InputStream inputStream = multipartFile.getInputStream();
            // 上傳到minio服務器
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(inputStream, -1L, 10485760L)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回地址
        return fileName;
    }

    /**
     * 文件下載
     *
     * @param fileName 文件名
     * @param delete   是否刪除
     */
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (StringUtils.isBlank(fileName)) {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                String data = "文件下載失敗";
                OutputStream ps = response.getOutputStream();
                ps.write(data.getBytes(StandardCharsets.UTF_8));
                return;
            }

            outputStream = response.getOutputStream();
            // 獲取文件對象
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build());
            byte[] buf = new byte[1024];
            int length = 0;
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName.substring(fileName.lastIndexOf("/") + 1), "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            // 輸出文件
            while ((length = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            inputStream.close();
            // 判斷：下載後是否同時刪除minio上的存儲文件
            if (BooleanUtils.isTrue(delete)) {
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
            }
        } catch (Throwable ex) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "文件下載失敗";
            try {
                OutputStream ps = response.getOutputStream();
                ps.write(data.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                outputStream.close();
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

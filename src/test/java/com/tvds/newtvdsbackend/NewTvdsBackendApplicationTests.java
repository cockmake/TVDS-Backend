package com.tvds.newtvdsbackend;

import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class NewTvdsBackendApplicationTests {

    @Test
    void contextLoads() {

    }

    @Autowired
    private MinioClient minioClient;

    @Test
    void minioDownloadTest() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 下载文件测试
        String bucketName = "new-tvds"; // 替换为你的桶名称
        String objectName = "a/b.png"; // 替换为你要下载的对象名称
        String filePath = "C:\\Users\\make\\Desktop\\b.png"; // 替换为你要保存的文件路径
        DownloadObjectArgs downloadObjectArgs = new DownloadObjectArgs.Builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(filePath)
                .build();
        minioClient.downloadObject(downloadObjectArgs);
        System.out.println("文件下载成功");
    }

    @Test
    void minioUploadTest() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketName = "new-tvds";
        String objectName = "a/c.png";
        String filePath = "C:\\Users\\make\\Desktop\\b.png";
        minioClient.uploadObject(
                io.minio.UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filePath)
                        .build()
        );
        System.out.println("文件上传成功");
    }

}

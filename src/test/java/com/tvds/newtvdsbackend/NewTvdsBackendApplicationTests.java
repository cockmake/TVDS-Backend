package com.tvds.newtvdsbackend;

import com.tvds.newtvdsbackend.domain.mq.ComponentLocationResult;
import com.tvds.newtvdsbackend.utils.RabbitMqUtil;
import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@SpringBootTest
class NewTvdsBackendApplicationTests {

    @Test
    void contextLoads() {

    }

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    void sendRabbitMqMessageTest() {
        // 定义交换机名称和路由键 (与消费者配置一致)
        String exchangeName = RabbitMqUtil.COMPONENT_LOCATION_EXCHANGE_NAME;
        String routingKey = RabbitMqUtil.PRODUCER_COMPONENT_LOCATION_ROUTING_KEY;


        // 发送消息
        // RabbitTemplate 会使用配置的 Jackson2JsonMessageConverter 将对象转换为 JSON
        for (int i = 0; i < 10; i++) {
            // 复杂一点的信息
            Map<String, Object> message = Map.of(
                    "id", "123456",
                    "name", "测试组件",
                    "location", Map.of(
                            "x", 100,
                            "y", 200
                    ),
                    "status", "active",
                    "index", i
            );
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
            System.out.println("消息已发送到交换机 '" + exchangeName + "'，路由键为 '" + routingKey + "'");
        }

    }

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

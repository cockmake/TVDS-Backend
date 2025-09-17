package com.tvds.newtvdsbackend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tvds.newtvdsbackend.domain.entity.Component;
import com.tvds.newtvdsbackend.domain.mq.ComponentLocationResult;
import com.tvds.newtvdsbackend.domain.vo.ComponentVO;
import com.tvds.newtvdsbackend.domain.vo.VisualPromptVO;
import com.tvds.newtvdsbackend.mapper.ComponentMapper;
import com.tvds.newtvdsbackend.utils.RabbitMqUtil;
import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
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
    @Autowired
    ComponentMapper componentMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    void redisTest() {
        // 测试 Redis 存储
        String key = "a";
        String value = "b";
        redisTemplate.opsForValue().set(key, value);
        System.out.println("存储到 Redis: " + key + " -> " + value);

        // 测试 Redis 获取
        Object retrievedValue = redisTemplate.opsForValue().get(key);
        System.out.println("从 Redis 获取: " + key + " -> " + retrievedValue);
    }
    @Test
    void sendRabbitMqMessageTest() {
        // 定义交换机名称和路由键 (与消费者配置一致)
        String exchangeName = RabbitMqUtil.COMPONENT_LOCATION_EXCHANGE_NAME;
        String routingKey = RabbitMqUtil.PRODUCER_COMPONENT_LOCATION_ROUTING_KEY;

        // 发送消息
        // RabbitTemplate 会使用配置的 Jackson2JsonMessageConverter 将对象转换为 JSON
        for (int i = 0; i < 10; i++) {
            // 复杂一点的信息
//            Map<String, Object> message = Map.of(
//                    "id", "123456",
//                    "name", "测试组件",
//                    "location", Map.of(
//                            "x", 100,
//                            "y", 200
//                    ),
//                    "status", "active",
//                    "index", i
//            );
            String message = "钱依然" + i;
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

    @Test
    void testComponentMapper() {
        // 获取所有行，并按照创建时间排序
        LambdaQueryWrapper<Component> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Component::getCreatedAt);
        List<Component> list = componentMapper.selectList(queryWrapper);
        System.out.println(list);
        System.out.println(list.size());
    }

}

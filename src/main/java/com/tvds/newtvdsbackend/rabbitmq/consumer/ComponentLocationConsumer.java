package com.tvds.newtvdsbackend.rabbitmq.consumer;

import com.tvds.newtvdsbackend.domain.mq.ComponentLocationResult;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class ComponentLocationConsumer {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "consumer.component.location.queue", durable = "true"),
                    exchange = @Exchange(value = "component.location.exchange", type = "direct", durable = "true"),
                    key = "consumer.component.location.key"
            )
    )
    public void receiveMessage(Map componentLocationResult) {
        // 处理接收到的消息
        System.out.println("Received message: " + componentLocationResult);
    }
}

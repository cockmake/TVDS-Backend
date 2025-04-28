package com.tvds.newtvdsbackend.configuration;

import com.tvds.newtvdsbackend.utils.RabbitMqUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //设置Json转换器
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // --- 声明生产者相关的交换机、队列以及绑定 ---
    // --- 消费者相关的使用注解生成 ---

    @Bean
    public DirectExchange componentLocationExchange() {
        return new DirectExchange(RabbitMqUtil.COMPONENT_LOCATION_EXCHANGE_NAME, true, false);
    }


    @Bean
    public Queue producerComponentLocationQueue() {
        return QueueBuilder.durable(RabbitMqUtil.PRODUCER_COMPONENT_LOCATION_QUEUE_NAME).build();
    }

    @Bean
    public Binding producerBinding(Queue producerComponentLocationQueue, DirectExchange componentLocationExchange) {
        return BindingBuilder.bind(producerComponentLocationQueue)
                .to(componentLocationExchange)
                .with(RabbitMqUtil.PRODUCER_COMPONENT_LOCATION_ROUTING_KEY);
    }

}

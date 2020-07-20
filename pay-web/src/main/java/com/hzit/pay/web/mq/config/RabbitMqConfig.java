package com.hzit.pay.web.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * mq配置
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 延时交换机的name
     */
    public static final String DELAY_EXCHANGE_NAME = "delay.queue.demo.business.exchange";

    /**
     * 死信交换机
     */
    public static final String DEAD_LETTER_EXCHANGE = "delay.queue.demo.deadletter.exchange";

    /**
     * 死信交换机的路由key
     */
    public static final String DEAD_LETTER_QUEUEA_ROUTING_KEY = "delay.queue.demo.deadletter.delay_10s.routingkey";

    /**
     * 死信交换机的路由key
     */
    public static final String DEAD_LETTER_QUEUEB_ROUTING_KEY = "delay.queue.demo.deadletter.delay_15s.routingkey";



    /**
     * 延时队列A的name
     */
    public static final String DELAY_QUEUEA_NAME = "delay.queue.demo.business.queuea";

    /**
     * 延时队列B的name
     */
    public static final String DELAY_QUEUEB_NAME = "delay.queue.demo.business.queueb";

    /**
     * 死信队列A
     */
    public static final String DEAD_LETTER_QUEUEA_NAME = "delay.queue.demo.deadletter.queuea";

    /**
     * 死信队列B
     */
    public static final String DEAD_LETTER_QUEUEB_NAME = "delay.queue.demo.deadletter.queueb";


    public static final String DELAY_QUEUEA_ROUTING_KEY = "delay.queue.demo.business.queuea.routingkey";


    public static final String DELAY_QUEUEB_ROUTING_KEY = "delay.queue.demo.business.queueb.routingkey";



    // 声明延时Exchange
    @Bean("delayExchange")
    public DirectExchange delayExchange(){

        //Direct 直连交换 路由key要全部匹配
        return new DirectExchange(DELAY_EXCHANGE_NAME);
    }

    // 声明死信Exchange
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }


    // 声明延时队列A 延时10s
    // 并绑定到对应的死信交换机
    @Bean("delayQueueA")
    public Queue delayQueueA(){
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);

        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEA_ROUTING_KEY);

        // x-message-ttl  声明队列的TTL 整个队列中的消息都是10秒后进入死信交换机
        args.put("x-message-ttl", 30000);

        return QueueBuilder.durable(DELAY_QUEUEA_NAME).withArguments(args).build();
    }


    // 声明延时队列A 延时10s
    // 并绑定到对应的死信交换机
    @Bean("delayQueueB")
    public Queue delayQueueB(){
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);

        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEB_ROUTING_KEY);

        // x-message-ttl  声明队列的TTL 整个队列中的消息都是10秒后进入死信交换机
        args.put("x-message-ttl", 9000);

        return QueueBuilder.durable(DELAY_QUEUEB_NAME).withArguments(args).build();
    }


    // 声明延时队列A绑定关系
    @Bean
    public Binding delayBindingA(@Qualifier("delayQueueA") Queue queue,
                                 @Qualifier("delayExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEA_ROUTING_KEY);
    }

    // 声明业务队列B绑定关系
    @Bean
    public Binding delayBindingB(@Qualifier("delayQueueB") Queue queue,
                                 @Qualifier("delayExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEB_ROUTING_KEY);
    }


    // 声明死信队列A 用于接收延时10s处理的消息
    @Bean("deadLetterQueueA")
    public Queue deadLetterQueueA(){
        return new Queue(DEAD_LETTER_QUEUEA_NAME);
    }

    // 声明死信队列B 用于接收延时15s处理的消息
    @Bean("deadLetterQueueB")
    public Queue deadLetterQueueB(){
        return new Queue(DEAD_LETTER_QUEUEB_NAME);
    }


    // 声明死信队列A绑定关系
    @Bean
    public Binding deadLetterBindingA(@Qualifier("deadLetterQueueA") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEA_ROUTING_KEY);
    }

    // 声明死信队列B绑定关系
    @Bean
    public Binding deadLetterBindingB(@Qualifier("deadLetterQueueB") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEB_ROUTING_KEY);
    }



}

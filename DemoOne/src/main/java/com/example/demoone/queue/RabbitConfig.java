package com.example.demoone.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue myQueue() {
        System.out.println("first queue met");
        return new Queue("first_queue", true); // true for a durable queue
    }
}
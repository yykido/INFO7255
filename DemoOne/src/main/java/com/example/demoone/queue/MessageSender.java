package com.example.demoone.queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String queueName, String message) {
        System.out.println("method in sender---------");
        rabbitTemplate.convertAndSend(queueName, message);
    }
}